package com.example.ajax.myapplication.imageloader;

import android.widget.ImageView;

import com.example.ajax.myapplication.imageloader.impl.KnightOfTheBrushImpl;

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
