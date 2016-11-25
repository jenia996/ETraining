package com.example.ajax.myapplication.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.ajax.myapplication.PageData;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.impl.LoadParseOperation;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.mvp.SearchBookPresenter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SearchPresenter implements SearchBookPresenter {

    private final LoadParseOperation mLoadParseOperation;
    private final ResultView view;
    private final Handler handler;
    private final Loader mLoader;
    private String lastRequest;

    public SearchPresenter(final ResultView view) {
        this.view = view;
        handler = new Handler(Looper.getMainLooper());
        mLoader = new Loader();
        mLoadParseOperation = new LoadParseOperation();
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

    private void loadData(final String query, final int page) {

        mLoader.execute(mLoadParseOperation, new PageData(query, page), new OnResultCallback<List<BookModel>, Void>() {

            @Override
            public void onSucess(final List<BookModel> books) {
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

    private void notifyResponse(final List<BookModel> response) {
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
