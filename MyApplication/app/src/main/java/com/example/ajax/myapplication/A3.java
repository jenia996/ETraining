package com.example.ajax.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Ajax on 04.10.2016.
 */

public class A3 extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simple);
        editText = (EditText) findViewById(R.id.edit_text);
        Button btnNext = (Button) findViewById(R.id.btn_next);
        Button setText = ((Button) findViewById(R.id.set_text));
        setText.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.set_text) {
            editText.setText(R.string.a3_activity);
        }
        if (i == R.id.btn_next) {
            startActivity(new Intent(this, A4.class));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Text", editText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        editText.setText(savedInstanceState.getString("Text"));
    }
}
