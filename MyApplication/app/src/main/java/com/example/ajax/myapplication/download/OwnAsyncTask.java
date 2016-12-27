package com.example.ajax.myapplication.download;

public interface OwnAsyncTask<Params, Progress, Result> {

    Result doInBackground(Params params, ProgressCallback<Progress> progressCallback) throws Exception;

}
