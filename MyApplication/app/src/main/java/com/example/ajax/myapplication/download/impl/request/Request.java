package com.example.ajax.myapplication.download.impl.request;

import com.example.ajax.myapplication.download.IRequest;

public class Request implements IRequest {

    private final Priority mPriority;
    private final String mUri;

    public Request(final Priority pPriority, final String pUri) {
        mPriority = pPriority;
        mUri = pUri;
    }

    public Request(final String pUri) {
        mUri = pUri;
        mPriority = Priority.DEFAULT;
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    @Override
    public String getUri() {
        return mUri;
    }
}
