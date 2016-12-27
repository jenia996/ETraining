package com.example.ajax.myapplication.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.OnItemClickListener;
import com.example.ajax.myapplication.adapters.impl.SimilarBookAdapter;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.imageloader.impl.ImageLoader;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.mvp.presenter.BookPresenter;
import com.example.ajax.myapplication.ui.dialog.BigImageDialog;
import com.example.ajax.myapplication.utils.Constants;

import java.util.List;

public class BookTitleCardActivity extends BaseTitleCard implements ResultView<List<BookModel>>, View.OnClickListener {

    private OnItemClickListener mOnItemClickListener;
    private TextView mDescription;
    private ImageView mCover;
    private View mSimilar;
    private BookModel mBook;
    private RecyclerView mRecyclerView;
    private BookPresenter mPresenter;

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showResponse(final List<BookModel> response) {

        if (response == null || response.isEmpty()) {
            return;
        }

        final BookModel bookToSkip = response.get(0);
        mBook.setDescription(bookToSkip.getDescription());
        mCover.setTag(bookToSkip.getImageUrl());

        if (bookToSkip.getDescription() != null) {
            mDescription.setText(Html.fromHtml(bookToSkip.getDescription()));
        }

        mCover.setOnClickListener(this);
        response.remove(0);

        if (response.isEmpty()) {
            return;
        }
        final SimilarBookAdapter adapter = new SimilarBookAdapter(this, response, mOnItemClickListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mSimilar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(final View v) {
        final int id = v.getId();
        if (id == R.id.book_cover) {
            final BigImageDialog dialog = new BigImageDialog(this);
            dialog.show((String) v.getTag(), R.style.BigImageDialogStyle);
        } else if (id == R.id.author_name) {
            final Intent intent = new Intent(this, AuthorTitleCardActivity.class);
            final Parcelable model = new AuthorModel(mBook.getAuthorId(), mBook.getAuthorName());
            intent.putExtra(Constants.AUTHOR_EXTRA, model);
            startActivity(intent);
        } else if (id == R.id.add_to_watch_list) {
            v.setVisibility(View.GONE);
            mCover.buildDrawingCache();
            final Bitmap bitmap = mCover.getDrawingCache();
            mPresenter.addToWatchList(mBook, bitmap);
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_title_card);
        final ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle(Constants.BOOK_EXTRA);
        }

        mPresenter = new BookPresenter(this);
        final KnightOfTheBrush loader = new ImageLoader();
        mBook = getIntent().getParcelableExtra(Constants.BOOK_EXTRA);
        mRecyclerView = (RecyclerView) findViewById(R.id.similar_books);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.add_to_watch_list).setOnClickListener(this);
        final TextView author = (TextView) findViewById(R.id.author_name);
        final TextView title = (TextView) findViewById(R.id.book_title);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        mDescription = (TextView) findViewById(R.id.description);
        mCover = (ImageView) findViewById(R.id.book_cover);
        author.setOnClickListener(this);
        mSimilar = findViewById(R.id.similar);
        title.setText(mBook.getTitle());
        author.setText(mBook.getAuthorName());
        ratingBar.setRating(mBook.getRating());
        mOnItemClickListener = createOnRelatedItemClickListener();
        loader.drawBitmap(mCover, mBook.getImageUrl());
        mPresenter.download(String.valueOf(mBook.getId()));
    }

}
