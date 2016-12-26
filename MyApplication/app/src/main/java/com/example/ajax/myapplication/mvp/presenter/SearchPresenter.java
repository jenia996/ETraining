package com.example.ajax.myapplication.mvp.presenter;

import android.os.Handler;
import android.os.Looper;

import com.example.ajax.myapplication.download.operations.LoadOperation;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.download.operations.ParseSearchOperation;
import com.example.ajax.myapplication.download.impl.callback.NotifyResultCallback;
import com.example.ajax.myapplication.download.impl.callback.OnNetworkResultCallBack;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.mvp.SearchBookPresenter;
import com.example.ajax.myapplication.utils.API;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class SearchPresenter implements SearchBookPresenter {

    private final LoadOperation mLoadOperation;
    private final ParseSearchOperation mParseSearchOperation;
    private final ResultView mView;
    private final Handler mHandler;
    private final Loader mLoader;
    private String mLastRequest;

    public SearchPresenter(final ResultView view) {
        this.mView = view;
        mHandler = new Handler(Looper.getMainLooper());
        mLoader = new Loader();
        mLoadOperation = new LoadOperation();

        mParseSearchOperation = new ParseSearchOperation();
    }

    @Override
    public void update(final int page) {
        loadData(mLastRequest, page);
    }

    @Override
    public void download(final String query) {
        mLastRequest = encode(query);
        loadData(mLastRequest, 1);
    }

    private void loadData(final String query, final int page) {

        mLoader.execute(mLoadOperation, API.getSearchUrl(query, page), new OnNetworkResultCallBack() {

            @Override
            public void onError(final Exception e) {
                super.onError(e);
                mView.hideProgressDialog();
            }

            @Override
            public void onSuccess(final String pS) {
                mLoader.execute(mParseSearchOperation, pS, new NotifyResultCallback<List<BookModel>>() {

                    @Override
                    public void onSuccess(final List<BookModel> pBookModels) {
                        notifyResponse(pBookModels);
                    }
                });

            }
        });

    }

    private void notifyResponse(final List<BookModel> response) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mView.hideProgressDialog();
                mView.showResponse(response);
            }
        });

    }

    private String encode(final String param) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(param, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("");
        }
        return encoded;
    }

}
