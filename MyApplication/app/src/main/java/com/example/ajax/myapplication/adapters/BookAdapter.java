package com.example.ajax.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.model.viewmodel.BookModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private OnItemCLickListener mItemCLickListener;
    private List<BookModel> books;

    public BookAdapter(final OnItemCLickListener pItemCLickListener) {
        this();
        mItemCLickListener = pItemCLickListener;
    }

    private BookAdapter() {
        books = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final BookModel book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthorName());
        holder.rating.setText(String.valueOf(book.getRating()));
        KnightOfTheBrush.Impl.getInstance().drawBitmap(holder.cover, book.getImageUrl());
        holder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                mItemCLickListener.onClick(books.get((Integer) v.getTag()));
            }
        });
        holder.container.setTag(position);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(final List<BookModel> response) {
        books = new ArrayList<>(response);
    }

    public void addBooks(final Collection<BookModel> response) {
        books.addAll(response);
    }

    public interface OnItemCLickListener {

        void onClick(BookModel pBook);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final View container;
        private final TextView title;
        private final TextView author;
        private final TextView rating;
        private final ImageView cover;

        ViewHolder(final View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.book_item_container);
            title = (TextView) itemView.findViewById(R.id.book_title);
            author = (TextView) itemView.findViewById(R.id.author_name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            cover = (ImageView) itemView.findViewById(R.id.book_cover_small);
        }
    }
}
