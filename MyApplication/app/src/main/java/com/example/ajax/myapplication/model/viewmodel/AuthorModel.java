package com.example.ajax.myapplication.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AuthorModel implements Parcelable {

    private Long mId;
    private Boolean mFollowed;
    private String mName;
    private String mImage;
    private String mDescription;
    private List<BookModel> mBooks;

    public String getImage() {
        return mImage;
    }

    public AuthorModel() {
    }

    public void setImage(String pImage) {
        mImage = pImage;
    }

    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public Boolean getFollowed() {
        return mFollowed;
    }

    public void setFollowed(final Boolean pFollowed) {
        mFollowed = pFollowed;
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
        dest.writeValue(this.mFollowed);
        dest.writeString(this.mName);
        dest.writeString(this.mImage);
        dest.writeString(this.mDescription);
        dest.writeTypedList(this.mBooks);
    }

    private AuthorModel(final Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mFollowed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mName = in.readString();
        this.mImage = in.readString();
        this.mDescription = in.readString();
        this.mBooks = in.createTypedArrayList(BookModel.CREATOR);
    }

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
}
