package com.example.ajax.myapplication.download.operations;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;

import java.io.IOException;

public class LoadOperation implements OwnAsyncTask<String, Void, String> {

    @Override
    public String doInBackground(final String pRequest, final ProgressCallback<Void> progressCallback) throws IOException {
        return HttpClient.get(pRequest);
    }
}
