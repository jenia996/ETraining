package com.example.ajax.myapplication.model.entity;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;

import java.util.List;

@Table(name = "AUTHORS")
public class Author {

    List<Book> mBooks;
    @dbLong
    private Long id;
    @dbString
    private String mName;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }
}
