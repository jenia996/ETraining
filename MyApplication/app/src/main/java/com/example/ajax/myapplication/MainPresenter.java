package com.example.ajax.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.ajax.myapplication.backend.myApi.MyApi;
import com.example.ajax.myapplication.data.api.ApiManager;
import com.example.ajax.myapplication.interfaces.BasePresenter;
import com.example.ajax.myapplication.interfaces.MainView;

import java.io.IOException;

class MainPresenter implements BasePresenter {
    private MainView view;
    private Handler handler;

    MainPresenter(MainActivity view) {
        this.view = view;
        handler = new Handler(Looper.getMainLooper());
    }

    private void loadData() {
        Log.d("Load", "Before thread");

        new Thread() {
            @Override
            public void run() {
                try {
                    MyApi.GetAuthorInfoById call = ApiManager.getInstance().myApi()
                            .getAuthorInfoById(BuildConfig.API_URL, BuildConfig.API_KEY, 3389);
                    String response = call.execute().getData();
                    notifyResponce(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void notifyError(final IOException e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //   view.hideProgressDialog();
                view.showResponce(e.getMessage());

            }
        });
    }

    private void notifyResponce(final String response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //   view.hideProgressDialog();
                view.showResponce(response);

            }
        });
    }

    @Override
    public void onReady() {
        Log.d("Load", "Entered Method");
        loadData();
        //   view.showProgressDialog();
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
