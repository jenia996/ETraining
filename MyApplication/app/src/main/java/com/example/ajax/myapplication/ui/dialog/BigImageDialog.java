package com.example.ajax.myapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.imageloader.impl.ImageLoader;
import com.example.ajax.myapplication.ui.dialog.base.InflateLayout;
import com.example.ajax.myapplication.utils.Constants;

public class BigImageDialog extends InflateLayout {

    private ImageView mView;

    public BigImageDialog(final Context context) {
        super(context);
    }

    public void show(final String url, final int style) {
        final KnightOfTheBrush imageLoader = new ImageLoader();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), style);
        builder.setView(this);
        final Dialog dialog = builder.create();
        dialog.setCancelable(true);
        final Window window = dialog.getWindow();

        if (window != null) {
            final WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            layoutParams.dimAmount = Constants.DIM_AMOUNT;
        }
        dialog.show();
        imageLoader.drawBitmap(mView, url);

    }

    @Override
    protected void onCreateView(final Context pContext, final AttributeSet pAttrs) {
        mView = (ImageView) findViewById(R.id.big_image);

    }

    @Override
    protected int getViewLayout() {
        return R.layout.big_image_dialog;
    }
}
