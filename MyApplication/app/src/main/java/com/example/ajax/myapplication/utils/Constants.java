package com.example.ajax.myapplication.utils;

import android.text.format.DateUtils;

public interface Constants {

    String DATABASE = "bookDB";
    String BOOK_EXTRA = "Book";
    String AUTHOR_EXTRA = "AUTHOR";
    String AUTHOR_TAG = "author";
    String WORK_TAG = "work";
    String BOOK_TAG = "best_book";
    String BOOKS_TAG = "books";
    Integer DATABASE_VERSION = 3;
    String BOOK_TABLE_NAME = "books";
    String AUTHOR_TABLE_NAME = "authors";
    String SIMILAR_BOOK_TAG = "similar_books";
    Integer UPDATE_REQUEST_CODE = 1 << 1;
    String UPDATE_TIMESTAMT = "update_timestamp";
    String UPDATE_TIME = "update_interval";
    String UPDATE_DAYS = "update_days";
    String DOWNLOAD_LARGE = "_extras[book_covers_large]";
    Long WEEK_IN_MILLIS = DateUtils.WEEK_IN_MILLIS;
    String LARGE_IMAGE_URL = "large_image_url";
    String MIN_MARK = "min_mark";

    float DIM_AMOUNT = 0.75F;
    String EDIT_SCHEDULE_DIALOG = "edit_schedule_dialog";
}
