package com.example.ajax.myapplication.imageloader.cache;

import android.support.annotation.NonNull;

import com.example.ajax.myapplication.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ajax on 01.11.2016.
 */

final class DiskLruCache implements Closeable {

    private static final String JOURNAL_FILE = "journal";
    private static final String JOURNAL_FILE_TMP = "journal.tmp";
    private static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String VERSION_1 = "1";
    private static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    private static final String REMOVE = "REMOVE";
    private static final String READ = "READ";

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    private final File directory;
    private final File journalFile;
    private final File journalFileTmp;
    private final int appVersion;
    private final long maxSize;
    private final int valueCount;
    private final Map<String, Entry> lruEntries
            = new LinkedHashMap<>(0, 0.75f, true);
    private final ExecutorService executorService = new ThreadPoolExecutor(0, 1,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    private long size = 0;
    private Writer journalWriter;
    private int redundantOpCount;
    private final Callable<Void> cleanupCallable = new Callable<Void>() {

        @Override
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (journalWriter == null) {
                    return null;
                }
                trimToSize();
                if (journalRebuildRequired()) {
                    rebuildJournal();
                    redundantOpCount = 0;
                }
            }
            return null;
        }
    };
    private long nextSequenceNumber = 0;

    private DiskLruCache(final File directory, final int appVersion, final int valueCount, final long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TMP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    /* From java.util.Arrays */
    @SuppressWarnings("unchecked")
    private static <T> T[] copyOfRange(final T[] original, final int start, final int end) {
        final int originalLength = original.length; // For exception priority compatibility.
        if (start > end) {
            throw new IllegalArgumentException();
        }
        if (start < 0 || start > originalLength) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int resultLength = end - start;
        final int copyLength = Math.min(resultLength, originalLength - start);
        final T[] result = (T[]) Array
                .newInstance(original.getClass().getComponentType(), resultLength);
        System.arraycopy(original, start, result, 0, copyLength);
        return result;
    }

    private static String readFully(final Reader reader) throws IOException {
        try {
            final StringWriter writer = new StringWriter();
            final char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            IOUtils.close(reader);
        }
    }

    private static String readAsciiLine(final InputStream in) throws IOException {
        final StringBuilder result = new StringBuilder(80);
        while (true) {
            final int c = in.read();
            if (c == -1) {
                throw new EOFException();
            } else if (c == '\n') {
                break;
            }

            result.append((char) c);
        }
        final int length = result.length();
        if (length > 0 && result.charAt(length - 1) == '\r') {
            result.setLength(length - 1);
        }
        return result.toString();
    }

    private static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final RuntimeException rethrown) {
                throw rethrown;
            } catch (final Exception ignored) {
            }
        }
    }

    private static void deleteContents(final File dir) throws IOException {
        final File[] files = dir.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("not a directory: " + dir);
        }
        for (final File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to deleteItem file: " + file);
            }
        }
    }

    public static DiskLruCache open(final File directory, final int appVersion, final int valueCount, final long
            maxSize)
            throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }

        final DiskLruCache cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        if (cache.journalFile.exists()) {
            try {
                cache.readJournal();
                cache.processJournal();
                cache.journalWriter = new BufferedWriter(new FileWriter(cache.journalFile, true),
                        IO_BUFFER_SIZE);
                return cache;
            } catch (final IOException journalIsCorrupt) {
                cache.delete();
            }
        }

        directory.mkdirs();
        final DiskLruCache diskLruCache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        diskLruCache.rebuildJournal();
        return diskLruCache;
    }

    private static void deleteIfExists(final File file) throws IOException {
        file.delete();
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static String inputStreamToString(final InputStream in) throws IOException {
        return readFully(new InputStreamReader(in, UTF_8));
    }

    Editor edit(final String key) throws IOException {
        return edit(key, ANY_SEQUENCE_NUMBER);
    }

    /**
     * Returns true if this cache has been closed.
     */
    boolean isClosed() {
        return journalWriter == null;
    }

    /**
     * Force buffered operations to the filesystem.
     */
    synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        journalWriter.flush();
    }

    /**
     * Closes the cache and deletes all of its stored values. This will deleteItem
     * all files in the cache directory including files that weren't created by
     * the cache.
     */
    void delete() throws IOException {
        close();
        deleteContents(directory);
    }

    public synchronized Snapshot get(final String key) throws IOException {
        checkNotClosed();
        validateKey(key);
        final Entry entry = lruEntries.get(key);
        if (entry == null) {
            return null;
        }

        if (!entry.readable) {
            return null;
        }

        final InputStream[] ins = new InputStream[valueCount];
        try {
            for (int i = 0; i < valueCount; i++) {
                ins[i] = new FileInputStream(entry.getCleanFile(i));
            }
        } catch (final FileNotFoundException e) {
            // a file must have been deleted manually!
            return null;
        }

        redundantOpCount++;
        journalWriter.append(READ + ' ').append(key).append('\n');
        if (journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }

        return new Snapshot(key, entry.sequenceNumber, ins);
    }

    public File getDirectory() {
        return directory;
    }

    public long maxSize() {
        return maxSize;
    }

    public synchronized long size() {
        return size;
    }

    /**
     * Closes this cache. Stored values will remain on the filesystem.
     */
    public synchronized void close() throws IOException {
        if (journalWriter == null) {
            return; // already closed
        }
        for (final Entry entry : new ArrayList<>(lruEntries.values())) {
            if (entry.currentEditor != null) {
                entry.currentEditor.abort();
            }
        }
        trimToSize();
        journalWriter.close();
        journalWriter = null;
    }

    private void readJournal() throws IOException {
        final InputStream in = new BufferedInputStream(new FileInputStream(journalFile), IO_BUFFER_SIZE);
        try {
            final String magic = readAsciiLine(in);
            final String version = readAsciiLine(in);
            final String appVersionString = readAsciiLine(in);
            final String valueCountString = readAsciiLine(in);
            final String blank = readAsciiLine(in);
            if (!MAGIC.equals(magic)
                    || !VERSION_1.equals(version)
                    || !Integer.toString(appVersion).equals(appVersionString)
                    || !Integer.toString(valueCount).equals(valueCountString)
                    || !"".equals(blank)) {
                throw new IOException("unexpected journal header: ["
                        + magic + ", " + version + ", " + valueCountString + ", " + blank + "]");
            }

            while (true) {
                try {
                    readJournalLine(readAsciiLine(in));
                } catch (final EOFException endOfJournal) {
                    break;
                }
            }
        } finally {
            closeQuietly(in);
        }
    }

    private void readJournalLine(final String line) throws IOException {
        final String[] parts = line.split(" ");
        if (parts.length < 2) {
            throw new IOException("unexpected journal line: " + line);
        }

        final String key = parts[1];
        if (parts[0].equals(REMOVE) && parts.length == 2) {
            lruEntries.remove(key);
            return;
        }

        Entry entry = lruEntries.get(key);
        if (entry == null) {
            entry = new Entry(key);
            lruEntries.put(key, entry);
        }

        if (parts[0].equals(CLEAN) && parts.length == 2 + valueCount) {
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(copyOfRange(parts, 2, parts.length));
        } else if (parts[0].equals(DIRTY) && parts.length == 2) {
            entry.currentEditor = new Editor(entry);

        } else {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    private void processJournal() throws IOException {
        deleteIfExists(journalFileTmp);
        for (final Iterator<Entry> i = lruEntries.values().iterator(); i.hasNext(); ) {
            final Entry entry = i.next();
            if (entry.currentEditor == null) {
                for (int t = 0; t < valueCount; t++) {
                    size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (int t = 0; t < valueCount; t++) {
                    deleteIfExists(entry.getCleanFile(t));
                    deleteIfExists(entry.getDirtyFile(t));
                }
                i.remove();
            }
        }
    }

    private synchronized void rebuildJournal() throws IOException {
        if (journalWriter != null) {
            journalWriter.close();
        }

        final Writer writer = new BufferedWriter(new FileWriter(journalFileTmp), IO_BUFFER_SIZE);
        writer.write(MAGIC);
        writer.write("\n");
        writer.write(VERSION_1);
        writer.write("\n");
        writer.write(Integer.toString(appVersion));
        writer.write("\n");
        writer.write(Integer.toString(valueCount));
        writer.write("\n");
        writer.write("\n");

        for (final Entry entry : lruEntries.values()) {
            if (entry.currentEditor != null) {
                writer.write(DIRTY + ' ' + entry.key + '\n');
            } else {
                writer.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
            }
        }

        writer.close();
        journalFileTmp.renameTo(journalFile);
        journalWriter = new BufferedWriter(new FileWriter(journalFile, true), IO_BUFFER_SIZE);
    }

    private synchronized Editor edit(final String key, final long expectedSequenceNumber) throws IOException {
        checkNotClosed();
        validateKey(key);
        Entry entry = lruEntries.get(key);
        if (expectedSequenceNumber != ANY_SEQUENCE_NUMBER
                && (entry == null || entry.sequenceNumber != expectedSequenceNumber)) {
            return null; // snapshot is stale
        }
        if (entry == null) {
            entry = new Entry(key);
            lruEntries.put(key, entry);
        } else if (entry.currentEditor != null) {
            return null; // another edit is in progress
        }

        final Editor editor = new Editor(entry);
        entry.currentEditor = editor;

        journalWriter.write(DIRTY + ' ' + key + '\n');
        journalWriter.flush();
        return editor;
    }

    private synchronized void completeEdit(final Editor editor, final boolean success) throws IOException {
        final Entry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }

        // if this edit is creating the entry for the first time, every index must have a value
        if (success && !entry.readable) {
            for (int i = 0; i < valueCount; i++) {
                if (!entry.getDirtyFile(i).exists()) {
                    editor.abort();
                    throw new IllegalStateException("edit didn't create file " + i);
                }
            }
        }

        for (int i = 0; i < valueCount; i++) {
            final File dirty = entry.getDirtyFile(i);
            if (success) {
                if (dirty.exists()) {
                    final File clean = entry.getCleanFile(i);
                    dirty.renameTo(clean);
                    final long oldLength = entry.lengths[i];
                    final long newLength = clean.length();
                    entry.lengths[i] = newLength;
                    size = size - oldLength + newLength;
                }
            } else {
                deleteIfExists(dirty);
            }
        }

        redundantOpCount++;
        entry.currentEditor = null;
        if (entry.readable | success) {
            entry.readable = true;
            journalWriter.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
            if (success) {
                entry.sequenceNumber = nextSequenceNumber;
                nextSequenceNumber++;
            }
        } else {
            lruEntries.remove(entry.key);
            journalWriter.write(REMOVE + ' ' + entry.key + '\n');
        }

        if (size > maxSize || journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }
    }

    /**
     * We only rebuild the journal when it will halve the size of the journal
     * and eliminate at least 2000 ops.
     */
    private boolean journalRebuildRequired() {
        final int REDUNDANT_OP_COMPACT_THRESHOLD = 2000;
        return redundantOpCount >= REDUNDANT_OP_COMPACT_THRESHOLD
                && redundantOpCount >= lruEntries.size();
    }

    /**
     * Drops the entry for {@code key} if it exists and can be removed. Entries
     * actively being edited cannot be removed.
     *
     * @return true if an entry was removed.
     */
    private synchronized boolean remove(final String key) throws IOException {
        checkNotClosed();
        validateKey(key);
        final Entry entry = lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            return false;
        }

        for (int i = 0; i < valueCount; i++) {
            final File file = entry.getCleanFile(i);
            if (!file.delete()) {
                throw new IOException("failed to deleteItem " + file);
            }
            size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }

        redundantOpCount++;
        journalWriter.append(REMOVE + ' ').append(key).append('\n');
        lruEntries.remove(key);

        if (journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }

        return true;
    }

    private void checkNotClosed() {
        if (journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    private void trimToSize() throws IOException {
        while (size > maxSize) {
//            Map.Entry<String, Entry> toEvict = lruEntries.eldest();
            final Map.Entry<String, Entry> toEvict = lruEntries.entrySet().iterator().next();
            remove(toEvict.getKey());
        }
    }

    private void validateKey(final String key) {
        if (key.contains(" ") || key.contains("\n") || key.contains("\r")) {
            throw new IllegalArgumentException(
                    "keys must not contain spaces or newlines: \"" + key + "\"");
        }
    }

    /**
     * A snapshot of the values for an entry.
     */
    final class Snapshot implements Closeable {

        private final String key;
        private final long sequenceNumber;
        private final InputStream[] ins;

        private Snapshot(final String key, final long sequenceNumber, final InputStream[] ins) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.ins = ins;
        }

        /**
         * Returns the unbuffered stream with the value for {@code index}.
         */
        InputStream getInputStream(final int index) {
            return ins[index];
        }

        /**
         * Returns an editor for this snapshot's entry, or null if either the
         * entry has changed since this snapshot was created or if another edit
         * is in progress.
         */
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(key, sequenceNumber);
        }

        /**
         * Returns the string value for {@code index}.
         */
        public String getString(final int index) throws IOException {
            return inputStreamToString(getInputStream(index));
        }

        @Override
        public void close() {
            for (final InputStream in : ins) {
                closeQuietly(in);
            }
        }
    }

    /**
     * Edits the values for an entry.
     */
    final class Editor {

        private final Entry entry;
        private boolean hasErrors;

        private Editor(final Entry entry) {
            this.entry = entry;
        }

        /**
         * Returns an unbuffered input stream to read the last committed value,
         * or null if no value has been committed.
         */
        InputStream newInputStream(final int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!entry.readable) {
                    return null;
                }
                return new FileInputStream(entry.getCleanFile(index));
            }
        }

        /**
         * Returns a new unbuffered output stream to write the value at
         * {@code index}. If the underlying output stream encounters errors
         * when writing to the filesystem, this edit will be aborted when
         * {@link #commit} is called. The returned output stream does not throw
         * IOExceptions.
         */
        OutputStream newOutputStream(final int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                return new FaultHidingOutputStream(new FileOutputStream(entry.getDirtyFile(index)));
            }
        }

        /**
         * Commits this edit so it is visible to readers.  This releases the
         * edit lock so another edit may be started on the same key.
         */
        void commit() throws IOException {
            if (hasErrors) {
                completeEdit(this, false);
                remove(entry.key); // the previous entry is stale
            } else {
                completeEdit(this, true);
            }
        }

        /**
         * Aborts this edit. This releases the edit lock so another edit may be
         * started on the same key.
         */
        void abort() throws IOException {
            completeEdit(this, false);
        }

        /**
         * Returns the last committed value as a string, or null if no value
         * has been committed.
         */
        public String getString(final int index) throws IOException {
            final InputStream in = newInputStream(index);
            return in != null ? inputStreamToString(in) : null;
        }

        /**
         * Sets the value at {@code index} to {@code value}.
         */
        public void set(final int index, final String value) throws IOException {
            Writer writer = null;
            try {
                writer = new OutputStreamWriter(newOutputStream(index), UTF_8);
                writer.write(value);
            } finally {
                closeQuietly(writer);
            }
        }

        private final class FaultHidingOutputStream extends FilterOutputStream {

            private FaultHidingOutputStream(final OutputStream out) {
                super(out);
            }

            @Override
            public void write(final int oneByte) {
                try {
                    out.write(oneByte);
                } catch (final IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void write(@NonNull final byte[] buffer, final int offset, final int length) {
                try {
                    out.write(buffer, offset, length);
                } catch (final IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void close() {
                try {
                    out.close();
                } catch (final IOException e) {
                    hasErrors = true;
                }
            }

            @Override
            public void flush() {
                try {
                    out.flush();
                } catch (final IOException e) {
                    hasErrors = true;
                }
            }
        }
    }

    private final class Entry {

        private final String key;

        /**
         * Lengths of this entry's files.
         */
        private final long[] lengths;

        /**
         * True if this entry has ever been published
         */
        private boolean readable;

        /**
         * The ongoing edit or null if this entry is not being edited.
         */
        private Editor currentEditor;

        /**
         * The sequence number of the most recently committed edit to this entry.
         */
        private long sequenceNumber;

        private Entry(final String key) {
            this.key = key;
            this.lengths = new long[valueCount];
        }

        String getLengths() throws IOException {
            final StringBuilder result = new StringBuilder();
            for (final long size : lengths) {
                result.append(' ').append(size);
            }
            return result.toString();
        }

        /**
         * Set lengths using decimal numbers like "10123".
         */
        private void setLengths(final String[] strings) throws IOException {
            if (strings.length != valueCount) {
                throw invalidLengths(strings);
            }

            try {
                for (int i = 0; i < strings.length; i++) {
                    lengths[i] = Long.parseLong(strings[i]);
                }
            } catch (final NumberFormatException e) {
                throw invalidLengths(strings);
            }
        }

        File getCleanFile(final int i) {
            return new File(directory, key + "." + i);
        }

        File getDirtyFile(final int i) {
            return new File(directory, key + "." + i + ".tmp");
        }

        private IOException invalidLengths(final String[] strings) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strings));
        }
    }

}
