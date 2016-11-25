package com.example.ajax.myapplication;

import android.app.Application;

import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.utils.ContextHolder;

public class App extends Application {

    @Override
    public void onCreate() {
        ContextHolder.set(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        KnightOfTheBrush.Impl.getInstance().clearCache();
    }

}
