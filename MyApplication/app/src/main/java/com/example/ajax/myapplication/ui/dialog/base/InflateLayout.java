package com.example.ajax.myapplication.ui.dialog.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public abstract class InflateLayout extends FrameLayout {

    public InflateLayout(final Context context) {
        this(context, null);
    }

    public InflateLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InflateLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected abstract void onCreateView(final Context pContext, final AttributeSet pAttrs);

    @LayoutRes
    protected abstract int getViewLayout();

    private void init(final Context pContext, final AttributeSet pAttrs) {
        View.inflate(pContext, getViewLayout(), this);
        onCreateView(pContext, pAttrs);
    }
}
