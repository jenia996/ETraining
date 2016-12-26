package com.example.ajax.myapplication.ui.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.example.ajax.myapplication.mvp.BaseView;

public abstract class BaseFragment extends Fragment implements BaseView {

    public ProgressDialog mProgressDialog;

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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
