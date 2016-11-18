package com.example.ajax.myapplication.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.ajax.myapplication.PageData;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.impl.LoadParseOpearion;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.mvp.SearchView;
import com.example.ajax.myapplication.mvp.SearchBookPresenter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SearchPresenter implements SearchBookPresenter {

    private final LoadParseOpearion mLoadParseOpearion;
    private final SearchView view;
    private final Handler handler;
    private final Loader mLoader;
    private String lastRequest;

    public SearchPresenter(final SearchView view) {
        this.view = view;
        handler = new Handler(Looper.getMainLooper());
        mLoader = new Loader();
        mLoadParseOpearion = new LoadParseOpearion();
    }

    @Override
    public void update(final int page) {
        loadData(lastRequest, page);
    }

    @Override
    public void download(final String query) {
        lastRequest = encode(query);
        loadData(lastRequest, 1);
    }

    @Override
    public void onReady() {
        view.showProgressDialog();

    }

    private void loadData(final String query, final int page) {

        mLoader.execute(mLoadParseOpearion, new PageData(query, page), new OnResultCallback<List<Book>, Void>() {

            @Override
            public void onSucess(final List<Book> books) {
                notifyResponse(books);
                ;
            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onProgressChange(final Void aVoid) {

            }
        });

    }

    private void notifyError(final IOException e) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.hideProgressDialog();
            }
        });
    }

    private void notifyResponse(final List<Book> response) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.hideProgressDialog();
                view.showResponse(response);
            }
        });

    }

    private void notifyResponce(final String response) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.hideProgressDialog();
            }
        });
    }

    private String encode(final String param) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(param, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }

}
