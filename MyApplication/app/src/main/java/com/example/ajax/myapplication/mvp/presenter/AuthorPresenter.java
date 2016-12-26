package com.example.ajax.myapplication.mvp.presenter;

import android.os.Handler;

import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.operations.LoadOperation;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.download.impl.callback.OnNetworkResultCallBack;
import com.example.ajax.myapplication.download.operations.ParseAuthorOperation;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;

public class AuthorPresenter implements BasePresenter {

    private final LoadOperation mLoadOperation;
    private final ResultView mView;
    private final Handler mHandler;
    private final Loader mLoader;
    private final ParseAuthorOperation mParseAuthorOperation;
    private final OnNetworkResultCallBack mOnResultCallback;

    public AuthorPresenter(final ResultView pView) {
        mView = pView;
        mHandler = new Handler();
        mLoader = new Loader();
        mLoadOperation = new LoadOperation();
        mParseAuthorOperation = new ParseAuthorOperation();
        mOnResultCallback = new OnNetworkResultCallBack() {

            @Override
            public void onSuccess(final String pS) {
                mLoader.execute(mParseAuthorOperation, pS, new OnResultCallback<AuthorModel, Void>() {

                    @Override
                    public void onSuccess(final AuthorModel pAuthorModel) {
                        notifyResponce(pAuthorModel);
                    }

                    @Override
                    public void onError(final Exception e) {

                    }

                    @Override
                    public void onProgressChange(final Void pVoid) {

                    }
                });
            }
        };
    }

    @Override
    public void download(final String query) {
        mLoader.execute(mLoadOperation, query, mOnResultCallback);

    }

    private void notifyResponce(final AuthorModel response) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mView.hideProgressDialog();
                mView.showResponse(response);
            }
        });
    }
}
