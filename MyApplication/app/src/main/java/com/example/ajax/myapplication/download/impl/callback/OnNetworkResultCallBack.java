package com.example.ajax.myapplication.download.impl.callback;

import android.widget.Toast;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.utils.ContextHolder;

public abstract class OnNetworkResultCallBack implements OnResultCallback<String, Void> {

    private final String mString;

    public OnNetworkResultCallBack(final String pMessage) {
        mString = pMessage;
    }

    public OnNetworkResultCallBack() {
        mString = ContextHolder.get().getString(R.string.internet_error);
    }

    @Override
    public void onError(final Exception e) {
        Toast.makeText(ContextHolder.get(), mString, Toast.LENGTH_LONG).show();//there must be a dialog
    }

    @Override
    public void onProgressChange(final Void pVoid) {

    }
}
