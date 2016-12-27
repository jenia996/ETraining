package com.example.ajax.myapplication.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileHelper {

    private final File mDirectory;

    public FileHelper() {
        final ContextWrapper cw = new ContextWrapper(ContextHolder.get());
        mDirectory = cw.getDir(Constants.IMAGE_DIRECTORY, Context.MODE_PRIVATE);

    }

    public String saveToInternalStorage(final Bitmap bitmapImage, final String name) {

        final File path = new File(mDirectory, HashHelper.sha256(name));
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (final Exception e) {
            throw new RuntimeException();
        } finally {
            IOUtils.close(fos);
        }
        return path.getAbsolutePath();
    }

    public Bitmap getBitmapFromStorage(final String name) {
        try {
            final File f = new File(mDirectory, name);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (final FileNotFoundException ignore) {
        }
        return null;
    }
}
