package com.example.ajax.myapplication.utils;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {

    public static void close(final Closeable pCloseable) {
        if (pCloseable != null) {
            try {
                pCloseable.close();
            } catch (final IOException ignore) {

            }
        }
    }
}
