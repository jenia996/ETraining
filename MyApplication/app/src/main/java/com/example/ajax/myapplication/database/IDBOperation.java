package com.example.ajax.myapplication.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

/**
 * Created by Ajax on 31.10.2016.
 */

public interface IDBOperation {

    Cursor query(String sql, String... args);

    long insert(Class<?> table, ContentValues value);

    int bulkInsert(Class<?> table, List<ContentValues> values);

    int delete(Class<?> table, String sql, String... args);

}
