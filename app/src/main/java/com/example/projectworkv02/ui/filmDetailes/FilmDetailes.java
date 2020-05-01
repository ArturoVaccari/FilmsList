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
    private long filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_film_detailes);

        imageView = findViewById(R.id.largeImage);
        name = findViewById(R.id.textName);
        description = findViewById(R.id.textDescription);

        filmId = getIntent().getLongExtra("film_id", 0);

        Cursor c = getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper._ID + " = " + filmId, null, null, null);
        c.moveToNext();
        if (c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE)).equals("null")) {
            Glide.with(this).load(R.drawable.img_placeholder).into(imageView);
        } else {
            Glide.with(this).load(Strings.IMGPREFIX + c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE))).into(imageView);
        }

        if (c.getString(c.getColumnIndex(FilmTableHelper.NAME)).equals("")) {
            name.setText(getText(R.string.text_no_title).toString());
        } else {
            name.setText(c.getString(c.getColumnIndex(FilmTableHelper.NAME)));
        }

        if (c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION)).equals("")) {
            description.setText(getText(R.string.text_no_description).toString());
        } else {
            description.setText(c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION)));
        }
    }
}
