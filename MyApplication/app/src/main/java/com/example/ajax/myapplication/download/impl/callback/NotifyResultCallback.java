package com.example.ajax.myapplication.download.impl.callback;

import com.example.ajax.myapplication.download.OnResultCallback;

public abstract class NotifyResultCallback<T> implements OnResultCallback<T, Void> {

    @Override
    public void onError(final Exception e) {
        throw new RuntimeException(e);
    }

    @Override
    public void onProgressChange(final Void pVoid) {

    }
}
