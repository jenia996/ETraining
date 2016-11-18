package com.example.ajax.myapplication.mvp;

import com.example.ajax.myapplication.model.entity.Book;

import java.util.List;

public interface SearchView extends BaseView {

    void showResponse(List<Book> responce);

}
