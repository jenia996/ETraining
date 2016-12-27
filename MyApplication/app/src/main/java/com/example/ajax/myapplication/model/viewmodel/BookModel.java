package com.example.ajax.myapplication.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class BookModel implements Parcelable {

    public static final Creator<BookModel> CREATOR = new Creator<BookModel>() {

        @Override
        public BookModel createFromParcel(final Parcel source) {
            return new BookModel(source);
        }

        @Override
        public BookModel[] newArray(final int size) {
            return new BookModel[size];
        }
    };
    private Long id;
    private String description;
    private String title;
    private String imageUrl;
    private Float rating;
    private Long authorId;
    private String authorName;

    public BookModel() {
    }

    protected BookModel(final Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.description = in.readString();
        this.title = in.readString();
        this.imageUrl = in.readString();
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
        this.authorId = (Long) in.readValue(Long.class.getClassLoader());
        this.authorName = in.readString();
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(final String pAuthorName) {
        authorName = pAuthorName;
    }

    public void setImageUrl(final String image) {
        this.imageUrl = image;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final Long pAuthorId) {
        authorId = pAuthorId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.description);
        dest.writeString(this.title);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.rating);
        dest.writeValue(this.authorId);
        dest.writeString(this.authorName);
    }
}
