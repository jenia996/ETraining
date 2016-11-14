package com.example.ajax.myapplication.download;

public interface OnResultCallback<Result, Progress> extends ProgressCallback<Progress> {

    void onSucess(Result result);

    void onError(Exception e);
}
