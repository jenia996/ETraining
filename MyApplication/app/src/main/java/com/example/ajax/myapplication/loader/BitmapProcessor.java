package com.example.ajax.myapplication.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ExifInterface;

import java.io.FileDescriptor;
import java.io.IOException;

public class BitmapProcessor {

    private static final int QAURTER = 90;
    private static final int HALF = 180;
    private static final int THREE_QARTER = 270;

    private static Bitmap rotateBitmapIfNeeded(final String pathName, final Bitmap bitmap) throws IOException {
        final ExifInterface exifInterface = new ExifInterface(pathName);

        final int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:

                return rotateBitmap(bitmap, QAURTER);

            case ExifInterface.ORIENTATION_ROTATE_180:

                return rotateBitmap(bitmap, HALF);

            case ExifInterface.ORIENTATION_ROTATE_270:

                return rotateBitmap(bitmap, THREE_QARTER);
        }

        return bitmap;
    }

    private static Bitmap rotateBitmap(final Bitmap bitmap, final int degree) {
        final Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap decodeSampledBitmapFromDescriptor(final FileDescriptor fd) {
        /*final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFileDescriptor(fd, null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;*/

        return BitmapFactory.decodeFileDescriptor(fd);

    }

    public Bitmap decodeSampledBitmapFromFile(final String pathName, final int reqWidth, final int reqHeight) throws IOException {
        final Bitmap bitmap;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathName, options);

        final int sampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;

        bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap != null ? rotateBitmapIfNeeded(pathName, bitmap) : null;
    }

    public Bitmap makeBitmapRound(final Bitmap src) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Canvas canvas = new Canvas(bitmap);

        final BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        final RectF rectF = new RectF(0.0f, 0.0f, width, height);

        // rect contains the bounds of the shape
        // radius is the radius in pixels of the rounded corners
        // paint contains the shader that will texture the shape

        final Canvas canvas = new Canvas(src);

        canvas.drawRoundRect(rectF, 30, 30, paint);

        return src;
    }

    private int calculateInSampleSize(final BitmapFactory.Options options,
                                      final int requiredWidth, final int requiredHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;

        int inSampleSize = 1;

        if (width > requiredWidth || height > requiredHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfWidth / inSampleSize) > requiredWidth
                    &&
                    (halfHeight / inSampleSize) > requiredHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
