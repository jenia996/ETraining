package com.example.ajax.myapplication.download;

/**
 * Created by Ajax on 17.10.2016.
 */

public interface ProgressCallback<Progress> {
    void onProgressChange(Progress progress);
}
