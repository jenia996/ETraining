package com.example.ajax.myapplication.mvp;

public interface ResultView<T> extends BaseView {

    void showResponse(T responce);

}
