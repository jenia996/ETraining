package com.example.ajax.myapplication;

/**
 * Created by Ajax on 11.11.2016.
 */
public class PageData {
    private String query;
    private int page;

    public PageData(String query, int page) {
        this.query = query;
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
