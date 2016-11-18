package com.example.ajax.myapplication.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ajax.myapplication.database.annotations.Table;
import com.example.ajax.myapplication.database.annotations.fields.dbLong;
import com.example.ajax.myapplication.database.annotations.fields.dbString;

import java.util.List;

@Table(name = "AUTHORS")
public class Author implements Parcelable {

    private List<Book> mBooks;
    @dbLong
    private Long id;
    @dbString
    private String mName;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(final String name) {
        mName = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeTypedList(this.mBooks);
        dest.writeValue(this.id);
        dest.writeString(this.mName);
    }

    public Author() {
    }

    private Author(final Parcel in) {
        this.mBooks = in.createTypedArrayList(Book.CREATOR);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mName = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {

        @Override
        public Author createFromParcel(final Parcel source) {
            return new Author(source);
        }

        @Override
        public Author[] newArray(final int size) {
            return new Author[size];
        }
    };
}
