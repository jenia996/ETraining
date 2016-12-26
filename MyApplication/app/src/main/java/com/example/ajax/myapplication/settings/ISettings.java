package com.example.ajax.myapplication.settings;

import java.util.List;

public interface ISettings {

    void setDownloadLarge(Boolean pDownloadLarge);

    boolean downloadLarge();

    int getUpdateTime();

    void setUpdateTime(int pUpdateInterval);

    List<Integer> getUpdateDays();

    void setUpdateDays(List<Integer> days);

    void save();
}
