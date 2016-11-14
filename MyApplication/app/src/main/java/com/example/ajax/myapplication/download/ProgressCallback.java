package com.example.ajax.myapplication.download;

public interface ProgressCallback<Progress> {

    void onProgressChange(Progress progress);
}
