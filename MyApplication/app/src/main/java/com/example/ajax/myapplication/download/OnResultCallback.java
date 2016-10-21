package com.example.ajax.myapplication.download;

/**
 * Created by Ajax on 18.10.2016.
 */

public interface OnResultCallback<Result, Progress> extends ProgressCallback<Progress> {
    void onSucess(Result result);

    void onError(Exception e);
}
