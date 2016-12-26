package com.example.ajax.myapplication.imageloader.impl;

import android.widget.ImageView;

import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.imageloader.impl.KnightOfTheBrushImpl;

public class ImageLoader implements KnightOfTheBrush {

    private final KnightOfTheBrush mKnightOfTheBrush;

    public ImageLoader(final KnightOfTheBrush pKnightOfTheBrush) {
        mKnightOfTheBrush = pKnightOfTheBrush;
    }

    public ImageLoader() {
        mKnightOfTheBrush = KnightOfTheBrushImpl.Impl.getInstance();
    }

    @Override
    public void drawBitmap(final ImageView imageView, final String imageUrl) {
        mKnightOfTheBrush.drawBitmap(imageView, imageUrl);
    }

    @Override
    public void clearCache() {
        mKnightOfTheBrush.clearCache();
    }
}
