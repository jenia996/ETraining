package com.example.ajax.myapplication.view;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.example.ajax.myapplication.interfaces.BaseView;
import com.example.ajax.myapplication.interfaces.MainView;

public abstract class BaseActivity extends AppCompatActivity implements MainView {
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
