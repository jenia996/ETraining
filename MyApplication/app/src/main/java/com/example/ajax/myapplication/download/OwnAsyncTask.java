package com.example.ajax.myapplication.download;

import java.io.IOException;

/**
 * Created by Ajax on 17.10.2016.
 */

public interface OwnAsyncTask<Params, Progress, Result> {

    Result doInBackground(Params params, ProgressCallback<Progress> progressCallback) throws IOException;

}
