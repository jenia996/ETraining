package com.example.ajax.myapplication.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbFloat;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;

@Table(name = "BOOKS")
public class Book implements Parcelable {

    protected Book(Parcel in) {
        isbn = in.readString();
        description = in.readString();
        title = in.readString();
        image = in.readString();
        authorId = in.readParcelable(Author.class.getClassLoader());
        rating = in.readFloat();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {

        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Author getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Author pAuthorId) {
        authorId = pAuthorId;
    }

    @dbString
    private String isbn;
    @dbString
    private String description;
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



    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(final String pIsbn) {
        isbn = pIsbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String pDescription) {
        description = pDescription;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.isbn);
        dest.writeString(this.description);
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeValue(this.authorId);
        dest.writeFloat(this.rating);
    }
}
