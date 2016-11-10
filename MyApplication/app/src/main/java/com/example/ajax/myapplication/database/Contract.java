package com.example.ajax.myapplication.database;

import com.example.ajax.myapplication.model.entity.Author;
import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.model.enums.Genre;

/**
 * Created by Ajax on 01.11.2016.
 */

public final class Contract {

    public final static Class<?>[] MODELS = {
            Book.class,
            Author.class,
    };
}
