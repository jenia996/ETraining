package com.example.ajax.myapplication.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

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
    private List<BookModel> mBooks;

    public AuthorModel() {
    }

    private AuthorModel(final Parcel in) {
        this.mBooks = in.createTypedArrayList(BookModel.CREATOR);
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mName = in.readString();
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
        dest.writeTypedList(this.mBooks);
        dest.writeValue(this.mId);
        dest.writeString(this.mName);
    }
}
