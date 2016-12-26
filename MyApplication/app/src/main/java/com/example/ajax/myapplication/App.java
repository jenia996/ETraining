package com.example.ajax.myapplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ajax.myapplication.database.DBHelper;
import com.example.ajax.myapplication.imageloader.KnightOfTheBrush;
import com.example.ajax.myapplication.tasks.BookUpdater;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;

import java.util.Calendar;

public class App extends Application {

    public long defaultTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public void restartUpdateTask() {
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final Intent updateIntent = new Intent(this, BookUpdater.class);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Long timestamp = prefs.getLong(Constants.UPDATE_TIMESTAMT, defaultTime());
        updateIntent.putExtra(Constants.UPDATE_TIMESTAMT, timestamp);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Constants.UPDATE_REQUEST_CODE, updateIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

    @Override
    public void onCreate() {
        ContextHolder.set(this);
        final DBHelper helper = new DBHelper(this, null, Constants.DATABASE_VERSION);
        helper.getWritableDatabase();
        new Thread(new Runnable() {

            @Override
            public void run() {
                restartUpdateTask();
            }
        }).start();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        KnightOfTheBrush.Impl.getInstance().clearCache();
    }

}
