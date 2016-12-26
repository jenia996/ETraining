package com.example.ajax.myapplication.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.settings.impl.Settings;
import com.example.ajax.myapplication.ui.dialog.EditScheduleDialog;
import com.example.ajax.myapplication.utils.Constants;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    Switch mDownloadLarge;
    View mRunSync;
    View mEditSchedule;
    Settings mSettings;
    TextView lastSyncDate;
    TextView updateSync;

    @Override
    public void onClick(final View v) {
        final int id = v.getId();
        if (id == R.id.edit_schedule) {
            final EditScheduleDialog test = new EditScheduleDialog();
            test.show(getFragmentManager(), Constants.EDIT_SCHEDULE_DIALOG);
        }
        if (id == R.id.download_large) {
            mSettings.setDownloadLarge(mDownloadLarge.isChecked());
        }
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSettings.save();
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setTitle(getResources().getString(R.string.settings));
        }

        mDownloadLarge = (Switch) findViewById(R.id.download_large);
        mRunSync = findViewById(R.id.run_sync);
        mEditSchedule = findViewById(R.id.edit_schedule);
        mEditSchedule.setOnClickListener(this);
        mSettings = new Settings();
        mDownloadLarge.setOnClickListener(this);
        mDownloadLarge.setChecked(mSettings.downloadLarge());
    }
}
