package com.example.Ajax.myapplication.backend;

/**
 * The object model for the data we are sending through endpoints
 */
class MyBean {

    private String myData;

    public String getData() {
        return myData;
    }

    void setData(final String data) {
        myData = data;
    }
}