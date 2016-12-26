package com.example.ajax.myapplication.download.operations;

import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;

import java.io.IOException;

public class ParseAuthorOperation implements OwnAsyncTask<String, Void, AuthorModel> {

    @Override
    public AuthorModel doInBackground(final String pS, final ProgressCallback<Void> progressCallback) throws IOException {
        return null;
    }
}
