package com.example.ajax.myapplication.database;

import com.example.ajax.myapplication.model.entity.Author;
import com.example.ajax.myapplication.model.entity.Book;

final class Contract {

    static final Class<?>[] MODELS = {
            Book.class,
            Author.class,
    };
}
