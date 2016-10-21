package com.example.ajax.myapplication.interfaces;

import com.example.ajax.myapplication.model.entity.Book;

import java.util.List;

public interface MainView extends BaseView {
    void showProgressDialog();

    void hideProgressDialog();

    void showResponce(String responce);

    void showResponce(List<Book> responce);

}
