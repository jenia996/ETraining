package com.example.ajax.myapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.mvp.presenter.BookPresenter;
import com.example.ajax.myapplication.ui.dialog.BigImageDialog;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;

import java.util.List;

public class BookTitleCardActivity extends BaseActivity implements ResultView<List<BookModel>>, View.OnClickListener {

    private OnItemClickListener mOnItemClickListener;
    private TextView mDescription;
    private ImageView mCover;
    private View mSimilar;

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
        mCover.setTag(bookToSkip.getImageUrl());
        mDescription.setText(bookToSkip.getDescription());
        mCover.setOnClickListener(this);
        response.remove(0);
        final SimilarBookAdapter adapter = new SimilarBookAdapter(this, response, mOnItemClickListener);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.similar_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();
        mSimilar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(final View v) {
        final int id = v.getId();
        if (id == R.id.book_cover) {
            final BigImageDialog dialog = new BigImageDialog(this);
            dialog.show((String) v.getTag(), R.style.BigImageDialogStyle);
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnItemClickListener = createOnItemClickListener();
        setContentView(R.layout.activity_book_title_card);
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle(Constants.BOOK_EXTRA);
        }
        final BasePresenter presenter = new BookPresenter(this);
        final BookModel book = getIntent().getParcelableExtra(Constants.BOOK_EXTRA);
        mCover = (ImageView) findViewById(R.id.book_cover);
        final KnightOfTheBrush loader = new ImageLoader();
        loader.drawBitmap(mCover, book.getImageUrl());
        final TextView author = (TextView) findViewById(R.id.author_name);
        final TextView title = (TextView) findViewById(R.id.book_title);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        mDescription = (TextView) findViewById(R.id.description);
        mSimilar = findViewById(R.id.similar);
        title.setText(book.getTitle());
        author.setText(book.getAuthorName());
        ratingBar.setRating(book.getRating());
        presenter.download(String.valueOf(book.getId()));
    }

    private OnItemClickListener createOnItemClickListener() {
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
