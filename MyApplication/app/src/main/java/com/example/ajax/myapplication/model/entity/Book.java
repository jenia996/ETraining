package com.example.ajax.myapplication.model.entity;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbFloat;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;

@Table(name = "BOOKS")
public class Book {

    @dbLong
    private Long id;
    @dbString
    private String title;
    @dbString
    private String image;
    @dbLong
    private Author authorId;
    @dbFloat
    private float rating;

    public Book() {

    }

    public float getRating() {
        return rating;
    }

    public void setRating(final float rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Author getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final Author authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }
}
