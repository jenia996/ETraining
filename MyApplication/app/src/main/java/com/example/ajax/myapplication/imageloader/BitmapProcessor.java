package com.example.ajax.myapplication.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.FileDescriptor;

public class BitmapProcessor {

    private static Bitmap rotateBitmap(final Bitmap bitmap, final int degree) {
        final Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap decodeSampledBitmapFromDescriptor(final FileDescriptor fd) {
        return BitmapFactory.decodeFileDescriptor(fd);

    }

}
