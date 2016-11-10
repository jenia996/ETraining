package com.example.ajax.myapplication.model.enums;

import com.example.ajax.myapplication.database.annotations.Table;

/**
 * Created by Ajax on 31.10.2016.
 */
@Table(name = "GENRES")
public enum Genre {
    FANTASY,
    HORROR,
    CLASSICS,
    ROMANCE,
    MANGA
}
