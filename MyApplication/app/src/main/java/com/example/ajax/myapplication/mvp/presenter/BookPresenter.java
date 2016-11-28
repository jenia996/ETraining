package com.example.ajax.myapplication.mvp.presenter;

import android.os.Handler;

import com.example.ajax.myapplication.PageData;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.impl.LoadParseOperation;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;

import java.util.List;

/**
 * Created by Ajax on 18.11.2016.
 */

public class BookPresenter implements BasePresenter {

    private final LoadParseOperation mLoadParseOperation;
    private final ResultView view;
    private final Handler handler;
    private final Loader mLoader;

    public BookPresenter(final ResultView pView) {
        view = pView;
        mLoader = new Loader();
        handler = new Handler();
        mLoadParseOperation = new LoadParseOperation();
    }

    @Override
    public void download(final String query) {
        mLoader.execute(mLoadParseOperation, new PageData(query, 1), new OnResultCallback<List<BookModel>, Void>() {

            @Override
            public void onSuccess(final List<BookModel> books) {
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

    private void notifyResponse(final List<BookModel> response) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                view.hideProgressDialog();
                view.showResponse(response);
            }
        });

    }
}
