package com.example.projectworkv02.database;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "films";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMGCARDBOARD = "imgcardboard";
    public static final String IMGLARGE = "imglarge";
    public static final String WATCH="watch";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY , " +
            NAME + " TEXT , " +
            DESCRIPTION + " TEXT , " +
            IMGCARDBOARD + " TEXT , " +
            IMGLARGE + " TEXT , " +
            WATCH +" INTEGER ) ;";
}

