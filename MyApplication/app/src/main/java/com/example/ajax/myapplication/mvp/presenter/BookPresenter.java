package com.example.ajax.myapplication.mvp.presenter;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.ajax.myapplication.database.DBHelper;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.download.impl.callback.NotifyResultCallback;
import com.example.ajax.myapplication.download.impl.callback.OnNetworkResultCallBack;
import com.example.ajax.myapplication.download.operations.LoadOperation;
import com.example.ajax.myapplication.download.operations.ParseSimilarOperation;
import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.settings.impl.Settings;
import com.example.ajax.myapplication.utils.API;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;
import com.example.ajax.myapplication.utils.FileHelper;
    import com.example.ajax.myapplication.utils.HashHelper;

import java.util.List;

public class BookPresenter implements BasePresenter {

    private static final String DELIM = "&";
    private final LoadOperation mLoadOperation;
    private final ParseSimilarOperation mParseSimilarOperation;
    private final ResultView mView;
    private final Handler mHandler;
    private final Loader mLoader;
    private final Settings mSettings;
    private final DBHelper mDBHelper;

    public BookPresenter(final ResultView pView) {
        mView = pView;
        mLoader = new Loader();
        mHandler = new Handler();
        mLoadOperation = new LoadOperation();
        mParseSimilarOperation = new ParseSimilarOperation();
        mSettings = new Settings();
        mDBHelper = new DBHelper(ContextHolder.get(), null, Constants.DATABASE_VERSION);
    }

    public void addToWatchList(final BookModel mBook, final Bitmap image) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final FileHelper helper = new FileHelper();
                final ContentValues bookContentValues = new ContentValues();
                bookContentValues.put(Book.ID, mBook.getId());
                bookContentValues.put(Book.AUTHOR_ID, mBook.getAuthorId());
                bookContentValues.put(Book.DESCRIPTION, mBook.getDescription());
                bookContentValues.put(Book.RATING, mBook.getRating());
                bookContentValues.put(Book.TITLE, mBook.getTitle());
                final ContentValues authorContentValues = new ContentValues();
                authorContentValues.put(Author.ID, mBook.getAuthorId());
                authorContentValues.put(Author.NAME, mBook.getAuthorName());
                helper.saveToInternalStorage(image, mBook.getImageUrl());
                bookContentValues.put(Book.IMAGE_URL, HashHelper.sha256(mBook.getImageUrl()));
                mDBHelper.insert(Book.class, bookContentValues);
                mDBHelper.insert(Author.class, authorContentValues);
            }
        }).start();
    }

    @Override
    public void download(final String query) {
        String request = API.getBookInfo(query);
        if (mSettings.downloadLarge()) {
            request += DELIM + Constants.DOWNLOAD_LARGE + "=true";
        }
        mLoader.execute(mLoadOperation, request, new OnNetworkResultCallBack() {

            @Override
            public void onSuccess(final String result) {
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
