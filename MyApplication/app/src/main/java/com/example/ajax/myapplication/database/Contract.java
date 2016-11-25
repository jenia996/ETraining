package com.example.ajax.myapplication.database;

import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;

final class Contract {

    static final Class<?>[] MODELS = {
            Book.class,
            Author.class,
    };
}
