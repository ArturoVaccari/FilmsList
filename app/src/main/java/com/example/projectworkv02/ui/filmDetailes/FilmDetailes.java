package com.example.projectworkv02.ui.filmDetailes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectworkv02.R;
import com.example.projectworkv02.Strings;
import com.example.projectworkv02.database.FilmDB;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

public class FilmDetailes extends AppCompatActivity {

    private ImageView imageView;
    private TextView name, description;
    private SQLiteDatabase database;
    private long filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_film_detailes);

        imageView = findViewById(R.id.largeImage);
        name = findViewById(R.id.textName);
        description = findViewById(R.id.textDescription);

        filmId = getIntent().getLongExtra("film_id", 0);
        Log.d("film", "onViewCreated: " + filmId);

        Cursor c = getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper._ID + " = " + filmId, null, null, null);
        c.moveToNext();

        Glide.with(this).load(Strings.IMGPREFIX + c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE))).into(imageView);
        name.setText(c.getString(c.getColumnIndex(FilmTableHelper.NAME)));
        description.setText(c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION)));
    }
}
