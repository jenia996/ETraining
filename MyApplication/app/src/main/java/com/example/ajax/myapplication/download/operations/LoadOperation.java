package com.example.ajax.myapplication.download.operations;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.utils.RequestCache;

import java.io.IOException;

public class LoadOperation implements OwnAsyncTask<String, Void, String> {

    private final RequestCache mCache;

    public LoadOperation() {
        mCache = RequestCache.getInstance();
    }

    @Override
    public String doInBackground(final String pRequest, final ProgressCallback<Void> progressCallback) throws
            IOException {
        final String cached = mCache.get(pRequest);

        if (cached != null) {
            return cached;
        }
        final String response = HttpClient.get(pRequest);
        mCache.put(pRequest, response);
        return response;
    }
}
