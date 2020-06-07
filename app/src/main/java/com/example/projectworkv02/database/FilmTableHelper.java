package com.example.projectworkv02.database;

import android.provider.BaseColumns;

public class FilmTableHelper implements BaseColumns {

    public static final String TABLE_NAME = "films";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String IMGCARDBOARD = "imgcardboard";
    public static final String IMGLARGE = "imglarge";
    public static final String WATCH="watch";
    public static final String WATCHED="watched";
    public static final String FILM_ID = "film_id";
    public static final String API_VOTE = "api_vote";
    public static final String PERSONAL_VOTE = "personal_vote";
    public static final String RELEASE_DATE = "release_date";

    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT , " +
            DESCRIPTION + " TEXT , " +
            IMGCARDBOARD + " TEXT , " +
            IMGLARGE + " TEXT , " +
            API_VOTE + " REAL , " +
            PERSONAL_VOTE + " REAL , " +
            RELEASE_DATE + " TEXT , " +
            FILM_ID + " INTEGER , " +
            WATCHED + " INTEGER , " +
            WATCH + " INTEGER ) ;";
}

