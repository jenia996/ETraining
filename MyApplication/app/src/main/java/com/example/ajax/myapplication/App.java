package com.example.ajax.myapplication;

import android.app.Application;

import com.example.ajax.myapplication.utils.ContextHolder;

/**
 * Created by Ajax on 21.10.2016.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        ContextHolder.set(this);

    }
}
