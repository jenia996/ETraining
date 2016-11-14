package com.example.ajax.myapplication.download;

import java.io.IOException;

public interface OwnAsyncTask<Params, Progress, Result> {

    Result doInBackground(Params params, ProgressCallback<Progress> progressCallback) throws IOException;

}
