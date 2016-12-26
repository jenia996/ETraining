package com.example.ajax.myapplication.download.impl.callback;

import com.example.ajax.myapplication.download.OnResultCallback;

/**
 * Created by Ajax on 25.12.2016.
 */

public abstract class NotifyResultCallback<T> implements OnResultCallback<T, Void> {

    @Override
    public void onError(final Exception e) {

    }

    @Override
    public void onProgressChange(final Void pVoid) {

    }
}
