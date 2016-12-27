package com.example.ajax.myapplication.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ajax.myapplication.R;
import com.example.ajax.myapplication.settings.impl.Settings;
import com.example.ajax.myapplication.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditScheduleDialog extends DialogFragment implements View.OnClickListener {

    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;
    private CheckBox mAllDays;
    private TextView mHour;
    private TextView mMinute;
    private List<CheckBox> mDaysCheckboxes;
    private Settings mSettings;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = new Settings();
        mDaysCheckboxes = new ArrayList<>();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.EditDialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        setCancelable(false);
        final View view = inflater.inflate(R.layout.edit_schedule_dialog, container, false);
        final List<Integer> checkBoxesIds = Arrays.asList(R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday, R.id.friday, R.id.saturday, R.id.sunday);
        final View save = view.findViewById(R.id.save_changes);
        final View cancel = view.findViewById(R.id.cancel_changes);
        mHour = (TextView) view.findViewById(R.id.hour);
        mMinute = (TextView) view.findViewById(R.id.minute);
        final int updateTime = mSettings.getUpdateTime();
        mHour.setText(String.valueOf(updateTime / MINUTES_IN_HOUR));
        mMinute.setText(String.valueOf(updateTime % MINUTES_IN_HOUR));
        for (final Integer checkBoxId : checkBoxesIds) {
            final CheckBox checkBox = (CheckBox) view.findViewById(checkBoxId);
            mDaysCheckboxes.add(checkBox);
        }
        mAllDays = (CheckBox) view.findViewById(R.id.all_days);
        view.findViewById(R.id.hour_up).setOnClickListener(this);
        view.findViewById(R.id.hour_down).setOnClickListener(this);
        view.findViewById(R.id.minute_up).setOnClickListener(this);
        view.findViewById(R.id.minute_down).setOnClickListener(this);
        mAllDays.setOnClickListener(this);
        final Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                mSettings.save();
                dialog.dismiss();
            }
        });

        final Window window = dialog.getWindow();

        if (window != null) {
            final WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            layoutParams.dimAmount = Constants.DIM_AMOUNT;
        }
        return view;
    }

    @Override
    public void onClick(final View v) {
        final int id = v.getId();
        if (id == R.id.hour_up) {
            addToHour(1);
        } else if (id == R.id.hour_down) {
            addToHour(-1);
        } else if (id == R.id.minute_up) {
            addToMinute(1);
        } else if (id == R.id.minute_down) {
            addToMinute(-1);
        } else if (id == R.id.all_days) {
            for (final CheckBox checkbox : mDaysCheckboxes) {
                checkbox.setChecked(mAllDays.isChecked());
            }
        }
    }

    private void addToMinute(final int value) {
        int minuteValue = Integer.parseInt(mMinute.getText().toString());
        minuteValue += value;
        minuteValue %= MINUTES_IN_HOUR;

        if (minuteValue < 0) {
            minuteValue += MINUTES_IN_HOUR;
        }

        mMinute.setText(String.valueOf(minuteValue));
    }

    private void addToHour(final int value) {
        int hourValue = Integer.parseInt(mHour.getText().toString());
        hourValue += value;
        hourValue %= HOURS_IN_DAY;

        if (hourValue < 0) {
            hourValue += HOURS_IN_DAY;
        }

        mHour.setText(String.valueOf(hourValue));
    }
}
