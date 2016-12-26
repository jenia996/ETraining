package com.example.ajax.myapplication.utils;

import java.util.Locale;

public final class API {

    private static final String API_URL = "https://www.goodreads.com/";
    private static final String BOOK_INFO_ID = "book/show/%s?";
    private static final String API_KEY = "key=dcX1YH52UMjs6lznO2MuSg";
    private static final String SEARCH_QUERY = "&q=%s&page=%d";
    private static final String SEARCH = "search/index?";

    public static String getSearchUrl(final String query, final int page) {
        return API_URL + SEARCH + API_KEY + String.format(Locale.US, SEARCH_QUERY, query, page);
    }

    public static String getBookInfo(final String id) {
        return API_URL + String.format(Locale.US, BOOK_INFO_ID, id) + API_KEY;
    }

}
