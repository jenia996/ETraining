package com.example.ajax.myapplication.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.model.viewmodel.AuthorModel;
import com.example.ajax.myapplication.model.viewmodel.BookModel;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookUpdater extends BroadcastReceiver {

    public Long newTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(ContextHolder.get()).edit();
        prefs.putLong(Constants.UPDATE_TIMESTAMT, newTimestamp()).apply();
        Loader loader = new Loader();

        List<BookModel> bookModelList = new ArrayList<>();
        List<AuthorModel> authorModels = new ArrayList<>();
        for (BookModel bookModel : bookModelList) {
        }
    }
}
