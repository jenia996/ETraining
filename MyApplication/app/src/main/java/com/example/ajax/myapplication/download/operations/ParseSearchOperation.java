package com.example.ajax.myapplication.download.operations;

import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.utils.XMLParser;

import java.util.List;

public class ParseSearchOperation implements OwnAsyncTask<String, Void, List<BookModel>> {

    @Override
    public List<BookModel> doInBackground(final String response, final ProgressCallback<Void> progressCallback) throws Exception {
        return XMLParser.parseSearch(response);
    }
}
