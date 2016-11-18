package com.example.ajax.myapplication.adapters;

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
import java.util.Collection;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {


    private List<Book> books;

    public BookAdapter() {
        books = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_book_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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

    public void setBooks(final List<Book> response) {
        books = new ArrayList<>(response);
    }

    public void addBooks(final Collection<Book> response) {
        books.addAll(response);
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
