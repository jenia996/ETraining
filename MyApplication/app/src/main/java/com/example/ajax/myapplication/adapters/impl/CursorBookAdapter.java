package com.example.ajax.myapplication.adapters.impl;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.CursorBaseAdapter;
import com.example.ajax.myapplication.adapters.OnItemClickListener;
import com.example.ajax.myapplication.model.Author;
import com.example.ajax.myapplication.model.Book;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.utils.FileHelper;

public class CursorBookAdapter extends CursorBaseAdapter<CursorBookAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final View.OnClickListener mClickListener;
    private final FileHelper mFileHelper;
    private final View.OnClickListener mDeleteListener;

    public CursorBookAdapter(final Context pContext, final OnItemClickListener pItemClickListener, final View
            .OnClickListener deleteListener) {
        mInflater = LayoutInflater.from(pContext);
        mFileHelper = new FileHelper();
        mDeleteListener = deleteListener;
        mClickListener = new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                final Cursor item = getItem((Integer) v.getTag());
                final BookModel model = new BookModel();
                model.setAuthorName(item.getString(item.getColumnIndex(Author.NAME)));
                model.setId(item.getLong(item.getColumnIndex(Book.ID)));
                model.setDescription(item.getString(item.getColumnIndex(Book.DESCRIPTION)));
                model.setRating(item.getFloat(item.getColumnIndex(Book.RATING)));
                model.setTitle(item.getString(item.getColumnIndex(Book.TITLE)));
                model.setAuthorId(item.getLong(item.getColumnIndex(Book.AUTHOR_ID)));
                pItemClickListener.onClick(model);
            }
        };
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {
        holder.container.setOnClickListener(mClickListener);
        holder.delete.setOnClickListener(mDeleteListener);
        holder.bindData(cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = mInflater.inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View container;
        private final TextView title;
        private final TextView author;
        private final TextView rating;
        private final ImageView cover;
        private final View delete;

        ViewHolder(final View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.book_item_container);
            title = (TextView) itemView.findViewById(R.id.book_title);
            author = (TextView) itemView.findViewById(R.id.author_name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            delete = itemView.findViewById(R.id.delete);
            delete.setVisibility(View.VISIBLE);
            cover = (ImageView) itemView.findViewById(R.id.book_cover_small);

        }

        void bindData(final Cursor pCursor) {
            rating.setText(String.valueOf(pCursor.getFloat(pCursor.getColumnIndex(Book.RATING))));
            title.setText(pCursor.getString(pCursor.getColumnIndex(Book.TITLE)));
            author.setText(pCursor.getString(pCursor.getColumnIndex(Author.NAME)));
            container.setTag(pCursor.getPosition());
            cover.setImageBitmap(mFileHelper.getBitmapFromStorage(pCursor.getString(pCursor.getColumnIndex(Book
                    .IMAGE_URL))));
            delete.setTag(pCursor.getPosition());

        }
    }
}
