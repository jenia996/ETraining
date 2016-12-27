package com.example.ajax.myapplication.mvp.presenter;

import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.ajax.myapplication.database.DBHelper;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;

public class WatchListPresenter implements BasePresenter {

    private final ResultView mView;
    private final Handler mHandler;
    private final String SQL = "SELECT * FROM " + Constants.BOOK_TABLE_NAME + " join " + Constants.AUTHOR_TABLE_NAME
            + " on " + Constants.BOOK_TABLE_NAME + "." + Book.AUTHOR_ID + "=" + Constants.AUTHOR_TABLE_NAME + "." +
            Author.ID;
    private final DBHelper mDBHelper;
    private final Loader mLoader;

    public WatchListPresenter(final ResultView pView) {
        mView = pView;
        mHandler = new Handler(Looper.getMainLooper());
        mLoader = new Loader();
        mDBHelper = new DBHelper(ContextHolder.get(), null, Constants.DATABASE_VERSION);
    }

    @Override
    public void download(final String query) {
        mLoader.execute(new OwnAsyncTask<Void, Void, Cursor>() {

            @Override
            public Cursor doInBackground(final Void pVoid, final ProgressCallback<Void> progressCallback) throws
                    Exception {
                return mDBHelper.query(SQL, "");
            }
        }, null, new OnResultCallback<Cursor, Void>() {

            @Override
            public void onSuccess(final Cursor pCursor) {
                notifyResponse(pCursor);
            }

            @Override
            public void onError(final Exception e) {
                Log.e("DB", e.getMessage());
            }

            @Override
            public void onProgressChange(final Void pVoid) {

            }
        });
    }

    public void deleteItem(final Integer pId,final Integer authorId) {
        mDBHelper.delete(Author.class, Author.ID + "=?", String.valueOf(authorId));
        mDBHelper.delete(Book.class, Book.ID + "=?", String.valueOf(pId));
        download("");
    }

    private void notifyResponse(final Cursor pCursor) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mView.hideProgressDialog();
                mView.showResponse(pCursor);
            }
        });
    }
}

