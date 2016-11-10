package com.example.ajax.myapplication.model.entity;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;

import java.util.List;

/**
 * Created by Ajax on 31.10.2016.
 */
@Table(name = "AUTHORS")
public class Author {
    @dbLong
    Long id;

    @dbString
    String mName;

    List<Book> mBooks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
