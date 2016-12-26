package com.example.ajax.myapplication.mvp.presenter;

import android.os.Handler;

import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.download.impl.callback.NotifyResultCallback;
import com.example.ajax.myapplication.download.impl.callback.OnNetworkResultCallBack;
import com.example.ajax.myapplication.download.operations.LoadOperation;
import com.example.ajax.myapplication.download.operations.ParseSimilarOperation;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.settings.impl.Settings;
import com.example.ajax.myapplication.utils.API;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.RequestCache;

import java.util.List;

public class BookPresenter implements BasePresenter {

    private static final String DELIM = "&";
    private final LoadOperation mLoadOperation;
    private final ParseSimilarOperation mParseSimilarOperation;
    private final ResultView mView;
    private final Handler mHandler;
    private final Loader mLoader;
    private final RequestCache mRequestCache;
    private final Settings mSettings;

    public BookPresenter(final ResultView pView) {
        mView = pView;
        mLoader = new Loader();
        mHandler = new Handler();
        mLoadOperation = new LoadOperation();
        mParseSimilarOperation = new ParseSimilarOperation();
        mRequestCache = RequestCache.getInstance();
        mSettings = new Settings();
    }

    @Override
    public void download(final String query) {
        final String cached = mRequestCache.get(query + Constants.BOOK_TAG);
        if (cached != null) {
            mLoader.execute(mParseSimilarOperation, cached, new NotifyResultCallback<List<BookModel>>() {

                @Override
                public void onSuccess(final List<BookModel> pBookModels) {
                    notifyResponse(pBookModels);
                }
            });
            return;
        }
        String request = API.getBookInfo(query);
        if (mSettings.downloadLarge()) {
            request += DELIM + Constants.DOWNLOAD_LARGE + "=true";
        }
        mLoader.execute(mLoadOperation, request, new OnNetworkResultCallBack() {

            @Override
            public void onSuccess(final String result) {
                mRequestCache.put(query + Constants.BOOK_TAG, result);
                mLoader.execute(mParseSimilarOperation, result, new NotifyResultCallback<List<BookModel>>() {

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
}
