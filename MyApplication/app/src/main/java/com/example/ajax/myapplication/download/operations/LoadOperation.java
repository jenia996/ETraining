package com.example.ajax.myapplication.download.operations;

import com.example.HttpClient;
import com.example.ajax.myapplication.PageData;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.utils.XMLHelper;

import java.io.IOException;
import java.util.List;

public class LoadOperation implements OwnAsyncTask<String, Void, String> {

    @Override
    public String doInBackground(final String pRequest, final ProgressCallback<Void> progressCallback) throws IOException {
        return HttpClient.get(pRequest);
    }
}
