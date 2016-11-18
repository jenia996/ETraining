package com.example.ajax.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.model.entity.Book;

public class BookTitleCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_title_card);
        Book book = getIntent().getParcelableExtra("Book");
    }
}
