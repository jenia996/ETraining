package com.example.ajax.myapplication.download.impl;

import com.example.HttpClient;
import com.example.ajax.myapplication.PageData;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.utils.XMLHelper;

import java.io.IOException;
import java.util.List;

public class LoadParseOperation implements OwnAsyncTask<PageData, Void, List<BookModel>> {

    @Override
    public List<BookModel> doInBackground(final PageData pageData, final ProgressCallback<Void> progressCallback) throws IOException {
        final String result = HttpClient.get("https://www.goodreads.com/search/index" +
                ".xml?key=zKxs0huf91EZnEjZNpYg&q=" + pageData.getQuery() + "&page=" + pageData.getPage
                ());
        return XMLHelper.parseSearch(result);
    }
}
