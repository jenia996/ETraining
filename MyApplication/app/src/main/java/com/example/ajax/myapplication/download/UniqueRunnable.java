package com.example.ajax.myapplication.download;

/**
 * Created by Ajax on 27.12.2016.
 */

public abstract class UniqueRunnable implements Runnable {

    private String id;

    public void setId(final String pId) {
        id = pId;
    }

    public String getId() {
        return id;
    }
}
