package com.example.ajax.myapplication;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.model.entity.Book;

import java.io.IOException;
import java.util.List;

/**
 * Created by Ajax on 11.11.2016.
 */

public class LoadParseOpearion implements OwnAsyncTask<PageData, Void, List<Book>> {
    @Override
    public List<Book> doInBackground(PageData pageData, ProgressCallback<Void> progressCallback) throws IOException {
        String result = HttpClient.get("https://www.goodreads.com/search/index" + "" +
                ".xml?key=zKxs0huf91EZnEjZNpYg&q=" + pageData.getQuery() + "&page=" + String.valueOf(pageData.getPage
                ()));
        XMLHelper helper = XMLHelper.getInstance();
        return helper.parse(result);
    }
}
