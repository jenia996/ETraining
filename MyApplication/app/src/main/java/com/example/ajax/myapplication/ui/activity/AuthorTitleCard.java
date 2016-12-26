package com.example.ajax.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.imageloader.impl.ImageLoader;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.utils.Constants;

public class AuthorTitleCard extends BaseActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_title_card);
        final AuthorModel authorModel = getIntent().getParcelableExtra(Constants.AUTHOR_EXTRA);
        final ImageView authorImage = (ImageView) findViewById(R.id.image);
        final KnightOfTheBrush loader = new ImageLoader();
        loader.drawBitmap(authorImage, authorModel.getImage());
        final TextView description = (TextView) findViewById(R.id.description);
        description.setText(authorModel.getDescription());
    }
}
