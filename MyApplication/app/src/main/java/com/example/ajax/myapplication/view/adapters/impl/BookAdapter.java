package com.example.ajax.myapplication.view.adapters.impl;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.model.entity.Book;
import com.example.ajax.myapplication.utils.ContextHolder;
import com.example.ajax.myapplication.view.adapters.AbstractAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajax on 21.10.2016.
 */

public class BookAdapter extends AbstractAdapter<Book> {

    private List<Book> books;

    public BookAdapter() {
        books = new ArrayList<>();
    }

    public BookAdapter(List<Book> books) {
        this.books = books;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AbstractViewHolder(LayoutInflater.from(ContextHolder.get()).inflate(R.layout
                .listview_book_item, null), R.id.book_title, R.id.author_name, R.id
                .book_cover_small);
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public void onBind(AbstractViewHolder holder, Book book, int position, int viewType) {
        holder.<TextView>get(R.id.book_title).setText(book.getTitle());
        holder.<TextView>get(R.id.author_name).setText(book.getAuthor());

    }

    @Override
    public Book getItem(int positoin) {
        return books.get(positoin);
    }
}
