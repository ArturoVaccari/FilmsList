package com.example.projectworkv02;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.projectworkv02.internet.InternetCalls;
import com.example.projectworkv02.database.Film;

import java.util.ArrayList;

public class FilmsLoader extends AsyncTaskLoader<ArrayList<Film>> {

    private Context context;

    public FilmsLoader(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Nullable
    @Override
    public ArrayList<Film> loadInBackground() {
        InternetCalls internetCalls = new InternetCalls();
        internetCalls.chiamataInternet(Strings.FILM, Strings.UPCOMING, Strings.ITALIAN, context);

        return null;
    }
}
