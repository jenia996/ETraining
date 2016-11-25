package com.example.ajax.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.SimilarBookAdapter;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.utils.Constants;

import java.util.List;

public class BookTitleCardActivity extends BaseActivity implements ResultView<List<BookModel>> {

    @Override
    public void showResponse(final List<BookModel> response) {
        final SimilarBookAdapter adapter = new SimilarBookAdapter(this, response);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.similar_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_title_card);
        final BookModel book = getIntent().getParcelableExtra(Constants.BOOK_EXTRA);
        final ImageView cover = (ImageView) findViewById(R.id.book_cover);
        KnightOfTheBrush.Impl.getInstance().drawBitmap(cover, book.getImageUrl());
        final TextView author = (TextView) findViewById(R.id.author_name);
        final TextView title = (TextView) findViewById(R.id.book_title);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        title.setText(book.getTitle());
        author.setText(book.getAuthorName());
        ratingBar.setRating(book.getRating());

    }
}
