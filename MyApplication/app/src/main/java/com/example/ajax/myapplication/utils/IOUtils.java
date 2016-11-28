package com.example.ajax.myapplication.utils;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {

    public static void close(final Closeable pCloseable) {
        try {
            pCloseable.close();
        } catch (IOException pE) {
            pE.printStackTrace();
        }
    }
}
