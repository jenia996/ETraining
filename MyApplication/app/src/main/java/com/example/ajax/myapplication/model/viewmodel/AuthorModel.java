package com.example.ajax.myapplication.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AuthorModel implements Parcelable {

    public static final Creator<AuthorModel> CREATOR = new Creator<AuthorModel>() {

        @Override
        public AuthorModel createFromParcel(final Parcel source) {
            return new AuthorModel(source);
        }

        @Override
        public AuthorModel[] newArray(final int size) {
            return new AuthorModel[size];
        }
    };
    private Long mId;
    private String mName;
    private String mImage;
    private String mAbout;
    private List<BookModel> mBooks;

    public AuthorModel() {
    }

    public AuthorModel(final long pAuthorId, final String pAuthorName) {
        mId = pAuthorId;
        mName = pAuthorName;
    }

    private AuthorModel(final Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mName = in.readString();
        this.mImage = in.readString();
        this.mAbout = in.readString();
        this.mBooks = in.createTypedArrayList(BookModel.CREATOR);
    }

    public String getAbout() {
        return mAbout;
    }

    public void setAbout(final String pAbout) {
        mAbout = pAbout;
    }

    public List<BookModel> getBooks() {
        return mBooks;
    }

    public void setBooks(final List<BookModel> pBooks) {
        mBooks = new ArrayList<>(pBooks);
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(final String pImage) {
        mImage = pImage;
    }

    public Long getId() {
        return mId;
    }

    public void setId(final Long id) {
        this.mId = id;
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
        dest.writeValue(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mImage);
        dest.writeString(this.mAbout);
        dest.writeTypedList(this.mBooks);
    }
}
