package com.example.ajax.myapplication.model.entity;

/**
 * Created by Ajax on 14.10.2016.
 */

public class Book {
    private String title;
    private String author;
    private String smallImage;

    public Book() {

    }

    public Book(String title, String author, String smallImage) {
        this.title = title;
        this.author = author;
        this.smallImage = smallImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }
}
