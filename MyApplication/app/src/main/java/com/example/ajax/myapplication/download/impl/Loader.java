package com.example.ajax.myapplication.download.impl;

import android.os.Handler;

import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Loader {

    private final ExecutorService executorService;

    public Loader() {
        executorService = Executors.newFixedThreadPool(1);
    }

    public Loader(int pNumber) {
        executorService = Executors.newFixedThreadPool(pNumber);
    }

    public <Param, Progress, Result> void execute(final OwnAsyncTask<Param, Progress, Result> ownAsyncTask, final
    Param param, final OnResultCallback<Result, Progress> onResultCallback) {
        final Handler handler = new Handler();
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                final Result result;
                try {
                    result = ownAsyncTask.doInBackground(param, new ProgressCallback<Progress>() {

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
                            onResultCallback.onSuccess(result);
                        }
                    });
                } catch (final IOException e) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            onResultCallback.onError(e);
                        }
                    });
                }

            }
        });
    }
}
