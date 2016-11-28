package com.example.ajax.myapplication.download;

public interface OnResultCallback<Result, Progress> extends ProgressCallback<Progress> {

    void onSuccess(Result result);

    void onError(Exception e);
}
