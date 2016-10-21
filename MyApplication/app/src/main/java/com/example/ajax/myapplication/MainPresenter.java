package com.example.ajax.myapplication;

import android.os.Handler;
import android.os.Looper;

import com.example.HttpClient;
import com.example.ajax.myapplication.interfaces.BasePresenter;
import com.example.ajax.myapplication.interfaces.MainView;
import com.example.ajax.myapplication.model.entity.Book;

import java.io.IOException;
import java.util.List;

class MainPresenter implements BasePresenter {
    private MainView view;
    private Handler handler;

    MainPresenter(MainActivity view) {
        this.view = view;
        handler = new Handler(Looper.getMainLooper());
    }

    private void loadData() {

        new Thread() {
            @Override
            public void run() {
                String result = HttpClient.get("https://www.goodreads.com/search/index" + "" +
                        ".xml?key=zKxs0huf91EZnEjZNpYg&q=King");
                XMLHelper helper = new XMLHelper();
                notifyResponse(helper.parse(result));
            }
        }.start();
    }

    private void notifyError(final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                view.hideProgressDialog();
                view.showResponce(e.getMessage());

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
                view.showResponce(response);

            }
        });
    }

    @Override
    public void onReady() {
        loadData();
        view.showProgressDialog();

    }


    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }
}
