package com.example.ajax.myapplication.settings.impl;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ajax.myapplication.settings.ISettings;
import com.example.ajax.myapplication.utils.Constants;
import com.example.ajax.myapplication.utils.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public final class Settings implements ISettings {

    private final SharedPreferences mSharedPreferences;
    private int mUpdateTime;
    private Boolean mDownloadLarge;
    private double mMinMark;
    private Boolean mDownloadWithoutCover;
    private List<Integer> mUpdateDays;

    public Settings() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextHolder.get());
        mDownloadLarge = mSharedPreferences.getBoolean(Constants.DOWNLOAD_LARGE, false);
        mUpdateTime = mSharedPreferences.getInt(Constants.UPDATE_TIME, 0);
        mUpdateDays = getDays(mSharedPreferences);
    }

    @Override
    public void setDownloadLarge(final Boolean pDownloadLarge) {
        mDownloadLarge = pDownloadLarge;
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.DOWNLOAD_LARGE, pDownloadLarge).apply();
    }

    @Override
    public boolean downloadLarge() {
        return mDownloadLarge;
    }

    @Override
    public int getUpdateTime() {
        return mUpdateTime;
    }

    @Override
    public void setUpdateTime(final int pUpdateInterval) {
        mUpdateTime = pUpdateInterval;
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(Constants.DOWNLOAD_LARGE, pUpdateInterval).apply();
    }

    @Override
    public List<Integer> getUpdateDays() {
        return mUpdateDays;
    }

    @Override
    public void setUpdateDays(final List<Integer> days) {
        mUpdateDays = new ArrayList<>(days);
    }

    @Override
    public void save() {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Constants.DOWNLOAD_LARGE, downloadLarge());
        editor.putInt(Constants.UPDATE_TIME, getUpdateTime());
        final StringBuilder builder = new StringBuilder();
        for (final Integer updateDay : mUpdateDays) {
            builder.append(updateDay).append("_");
        }
        editor.putString(Constants.UPDATE_DAYS, builder.toString());
        editor.apply();
    }

    private List<Integer> getDays(final SharedPreferences pSharedPreferences) {
        final List<Integer> days = new ArrayList<>();
        final String line = pSharedPreferences.getString(Constants.UPDATE_DAYS, "");
        final String[] values = line.split("_");
        for (final String value : values) {
            try {
                final Integer day = Integer.parseInt(value);
                days.add(day);
            } catch (final NumberFormatException ignore) {

            }
        }
        return days;
    }

}


