package com.example.ajax.myapplication.data.api;


import com.example.ajax.myapplication.BuildConfig;
import com.example.ajax.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

public class ApiManager {
    private static final String APP_ENGINE_BASE_URL = "http://10.0.3.2:8080/_ah/api/";

    private static ApiManager sInstance;
    private MyApi appEngineApi;

    private ApiManager() {
    }

    public static ApiManager getInstance() {
        if (sInstance == null) {
            sInstance = new ApiManager();
        }
        return sInstance;
    }

    public MyApi myApi() {
        if (appEngineApi == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(), null).setApplicationName(BuildConfig
                    .APPLICATION_ID).setRootUrl(APP_ENGINE_BASE_URL)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?>
                                               abstractGoogleClientRequest) throws IOException {
                    abstractGoogleClientRequest.setDisableGZipContent(true);
                }
            });
            appEngineApi = builder.build();
        }
        return appEngineApi;
    }
}
