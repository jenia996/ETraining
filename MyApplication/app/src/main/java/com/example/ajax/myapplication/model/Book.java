package com.example.ajax.myapplication.model;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbBoolean;
import com.example.ajax.myapplication.database.annotations.fields.dbFloat;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;
import com.example.ajax.myapplication.utils.Constants;

@Table(name = Constants.BOOK_TABLE_NAME)
public final class Book {

    @dbLong
    public static final String ID = "id";

    @dbString
    public static final String DESCRIPTION = "description";

    @dbString
    public static final String TITLE = "title";

    @dbString
    public static final String IMAGE_URL = "image_url";

    @dbFloat
    public static final String RATING = "average_rating";

    @dbLong
    public static final String AUTHOR_ID = "author_id";

}
