package com.example.ajax.myapplication.ui.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.ajax.myapplication.mvp.BaseView;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public ProgressDialog mProgressDialog;

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
