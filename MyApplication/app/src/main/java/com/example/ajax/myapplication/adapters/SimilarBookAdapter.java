package com.example.ajax.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.model.viewmodel.BookModel;

import java.util.ArrayList;
import java.util.List;

public class SimilarBookAdapter extends RecyclerView.Adapter<SimilarBookAdapter.ViewHolder> {

    private final Context mContext;
    private List<BookModel> mBooks;

    public SimilarBookAdapter(final Context pContext, final List<BookModel> pBooks) {
        mContext = pContext;
        mBooks = new ArrayList<>(pBooks);
    }

    public void setData(final List<BookModel> pBooks) {
        mBooks = new ArrayList<>(pBooks);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.similar_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        KnightOfTheBrush.Impl.getInstance().drawBitmap(holder.cover, mBooks.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;

        ViewHolder(final View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.book_cover);
        }
    }
}
