package com.example.ajax.myapplication;

import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

class MyObject {

    @Visibility
    int isVisible() {
        return Calendar.getInstance().get(Calendar.MINUTE) % 2 == 0 ? View.VISIBLE : View.INVISIBLE;
    }

    @IntDef({View.VISIBLE, View.INVISIBLE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Visibility {
    }

}
