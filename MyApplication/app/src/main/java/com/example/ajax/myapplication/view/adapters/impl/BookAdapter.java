package com.example.ajax.myapplication.view.adapters.impl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.loader.KnightOfTheBrush;
import com.example.ajax.myapplication.model.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajax on 21.10.2016.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {


    private List<Book> books;

    public BookAdapter() {
        books = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Book book = books.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthorId().getName());
        holder.rating.setText(String.valueOf(book.getRating()));
        KnightOfTheBrush.Impl.getInstance().drawBitmap(holder.cover, book.getImage());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setBooks(List<Book> response) {
        books = new ArrayList<>(response);
    }

    public void addBooks(List<Book> response) {
        books.addAll(response);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View container;
        private TextView title;
        private TextView author;
        private TextView rating;
        private ImageView cover;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.book_item_container);
            title = (TextView) itemView.findViewById(R.id.book_title);
            author = (TextView) itemView.findViewById(R.id.author_name);
            rating = (TextView) itemView.findViewById(R.id.rating);
            cover = (ImageView) itemView.findViewById(R.id.book_cover_small);
        }
    }
}
