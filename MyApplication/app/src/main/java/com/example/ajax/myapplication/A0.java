package com.example.ajax.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ajax on 04.10.2016.
 */

public class A0 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        redirect();
    }

    private void redirect() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean start = p.getBoolean("flowDone", false);
        if (!start) {
            startActivity(new Intent(this, A1.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
            // finish();
        } else {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent
                    .FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            //finish();


        }
    }
}
