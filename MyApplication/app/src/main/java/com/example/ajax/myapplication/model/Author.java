package com.example.ajax.myapplication.model;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;
import com.example.ajax.myapplication.utils.Constants;

@Table(name = Constants.AUTHOR_TABLE_NAME)
public final class Author {

    @dbLong
    public static final String ID = "a_id";

    @dbString
    public static final String ABOUT = "about";

    @dbString
    public static final String NAME = "name";
}
