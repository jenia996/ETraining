package com.example.ajax.myapplication.adapters.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.adapters.OnItemClickListener;
import com.example.ajax.myapplication.imageloader.impl.ImageLoader;
import com.example.ajax.myapplication.model.viewmodel.BookModel;

import java.util.ArrayList;
import java.util.List;

public class SimilarBookAdapter extends RecyclerView.Adapter<SimilarBookAdapter.ViewHolder> {

    private final Context mContext;
    private final ImageLoader mImageLoader;
    private final View.OnClickListener mListener;
    private final List<BookModel> mBooks;

    public SimilarBookAdapter(final Context pContext, final List<BookModel> pBooks, final OnItemClickListener pItemClickListener) {
        mContext = pContext;
        mBooks = new ArrayList<>(pBooks);
        mImageLoader = new ImageLoader();
        mListener = new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                pItemClickListener.onClick(mBooks.get((Integer) v.getTag()));
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.similar_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BookModel bookModel = mBooks.get(position);
        holder.title.setText(bookModel.getTitle());
        holder.container.setOnClickListener(mListener);
        holder.container.setTag(position);
        mImageLoader.drawBitmap(holder.cover, bookModel.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View container;
        ImageView cover;
        TextView title;

        ViewHolder(final View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            title = (TextView) itemView.findViewById(R.id.title);
            cover = (ImageView) itemView.findViewById(R.id.book_cover);
        }
    }
}
