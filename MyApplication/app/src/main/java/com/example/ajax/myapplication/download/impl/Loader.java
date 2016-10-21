package com.example.ajax.myapplication.download.impl;

import android.os.Handler;

import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.download.OnResultCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ajax on 19.10.2016.
 */

public class Loader {
    private final ExecutorService executorService;

    public Loader() {
        executorService = Executors.newCachedThreadPool();
    }

    public <Param, Progress, Result> void execute(final OwnAsyncTask<Param, Progress, Result>
                                                          ownAsyncTask, final Param param, final OnResultCallback<Result, Progress> onResultCallback) {
        final Handler handler = new Handler();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final Result result = ownAsyncTask.doInBackground(param, new
                        ProgressCallback<Progress>() {

                    @Override
                    public void onProgressChange(final Progress progress) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResultCallback.onProgressChange(progress);
                            }
                        });
                    }

                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultCallback.onSucess(result);
                    }
                });

            }
        });
    }
}
