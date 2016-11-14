package com.example.ajax.myapplication;

import android.os.Handler;
import android.os.Looper;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.interfaces.BasePresenter;
import com.example.ajax.myapplication.interfaces.MainView;
import com.example.ajax.myapplication.model.entity.Book;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

class MainPresenter implements BasePresenter {
    private final LoadParseOpearion mLoadParseOpearion;
    private final MainView view;
    private final Handler handler;
    private String lastRequest;
    private final Loader mLoader;

    MainPresenter(final MainView view) {
        this.view = view;
        handler = new Handler(Looper.getMainLooper());
        mLoader = new Loader();
        mLoadParseOpearion = new LoadParseOpearion();
    }

    private void loadData(final String query) {

        mLoader.execute(mLoadParseOpearion, new PageData(query, 1), new OnResultCallback<List<Book>, Void>() {
            @Override
            public void onSucess(final List<Book> books) {
                view.showResponce(books);
            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onProgressChange(final Void aVoid) {

            }
        });
        new Thread() {
            @Override
            public void run() {
                final String result = HttpClient.get("https://www.goodreads.com/search/index" + "" +
                        ".xml?key=zKxs0huf91EZnEjZNpYg&q=" + query);
                final XMLHelper helper = XMLHelper.getInstance();
                notifyResponse(helper.parse(result));
            }
        }.start();
    }

    private void notifyError(final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.hideProgressDialog();
                view.showError(e.getMessage());

            }
        });
    }

    private void notifyResponse(final List<Book> response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.hideProgressDialog();
                view.showResponce(response);
            }
        });

    }

    private void notifyResponce(final String response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.hideProgressDialog();
                view.showError(response);

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

    @Override
    public void update(final int page) {
        mLoader.execute(mLoadParseOpearion, new PageData(lastRequest, page), new OnResultCallback<List<Book>, Void>() {
            @Override
            public void onSucess(final List<Book> books) {
                view.showResponce(books);
            }

            @Override
            public void onError(final Exception e) {

            }

            @Override
            public void onProgressChange(final Void aVoid) {

            }
        });
        new Thread() {
            @Override
            public void run() {
                final String result = HttpClient.get("https://www.goodreads.com/search/index" + "" +
                        ".xml?key=zKxs0huf91EZnEjZNpYg&q=" + lastRequest + "&page=" + page);
                final XMLHelper helper = XMLHelper.getInstance();
                notifyResponse(helper.parse(result));
            }
        }.start();
    }

    @Override
    public void download(final String query) {
        lastRequest = encode(query);
        loadData(lastRequest);
    }

    @Override
    public void onReady() {
        view.showProgressDialog();

    }

}
