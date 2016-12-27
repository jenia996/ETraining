package com.example.ajax.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.OnItemClickListener;
import com.example.ajax.myapplication.adapters.impl.SimilarBookAdapter;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.imageloader.impl.ImageLoader;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.mvp.BasePresenter;
import com.example.ajax.myapplication.mvp.ResultView;
import com.example.ajax.myapplication.mvp.presenter.AuthorPresenter;
import com.example.ajax.myapplication.utils.Constants;

public class AuthorTitleCard extends BaseTitleCard implements ResultView<AuthorModel> {

    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnRelatedItemClickListener;
    private ImageView mAuthorImage;
    private TextView mAbout;
    private TextView mAuthorName;

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showResponse(final AuthorModel response) {
        if (response == null) {
            return;
        }

        final KnightOfTheBrush imageLoader = new ImageLoader();
        mAuthorName.setText(response.getName());
        mAbout.setText(Html.fromHtml(response.getAbout()));
        imageLoader.drawBitmap(mAuthorImage, response.getImage());

        if (response.getBooks() == null || response.getBooks().isEmpty()) {
            return;
        }

        final SimilarBookAdapter adapter = new SimilarBookAdapter(this, response.getBooks(), mOnRelatedItemClickListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        hideProgressDialog();
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_title_card);
        final AuthorModel authorModel = getIntent().getParcelableExtra(Constants.AUTHOR_EXTRA);
        final ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setTitle(authorModel.getName());
        }
        final BasePresenter presenter = new AuthorPresenter(this);
        mAuthorImage = (ImageView) findViewById(R.id.image);
        mAbout = (TextView) findViewById(R.id.about);
        mAuthorName = (TextView) findViewById(R.id.author_name);
        mOnRelatedItemClickListener = createOnRelatedItemClickListener();
        mRecyclerView = (RecyclerView) findViewById(R.id.books);
        presenter.download(String.valueOf(authorModel.getId()));
        showProgressDialog();
    }
}
