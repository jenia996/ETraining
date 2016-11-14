package com.example.ajax.myapplication.loader;

import android.widget.ImageView;

/**
 * Created by Ajax on 26.10.2016.
 */

public interface KnightOfTheBrush {

    void drawBitmap(final ImageView imageView, final String imageUrl);

    void clearCache();

    final class Impl {

        private static KnightOfTheBrush knightOfTheBrush;

        private static KnightOfTheBrush newInstance() {
            return new KnightOfTheBrushImpl();
        }


        public static KnightOfTheBrush getInstance() {
            if (knightOfTheBrush == null) {
                knightOfTheBrush = KnightOfTheBrush.Impl.newInstance();
            }
            return knightOfTheBrush;
        }
    }
}
