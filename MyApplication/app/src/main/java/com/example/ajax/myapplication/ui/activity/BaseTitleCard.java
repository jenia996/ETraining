package com.example.ajax.myapplication.ui.activity;

import android.content.Intent;

import com.example.ajax.myapplication.adapters.OnItemClickListener;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;

public abstract class BaseTitleCard extends BaseActivity {

    public OnItemClickListener createOnRelatedItemClickListener() {
        return new OnItemClickListener() {

            @Override
            public void onClick(final BookModel pBook) {
                final Intent intent = new Intent(ContextHolder.get(), BookTitleCardActivity.class);
                intent.putExtra(Constants.BOOK_EXTRA, pBook);
                startActivity(intent);
            }
        };
    }
}
