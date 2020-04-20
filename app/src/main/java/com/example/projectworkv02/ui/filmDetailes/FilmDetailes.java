package com.example.projectworkv02.ui.filmDetailes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.FilmDB;
import com.example.projectworkv02.database.FilmTableHelper;

public class FilmDetailes extends Fragment {

    private ImageView imageView;
    private TextView name, description;
    private SQLiteDatabase database;
    private int filmId;

    public FilmDetailes() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_film_detailes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.largeImage);
        name = view.findViewById(R.id.textName);
        description = view.findViewById(R.id.textDescription);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            filmId = bundle.getInt("film_id", 0);
            Log.d("film", "onViewCreated: " + filmId);
        }

        database = new FilmDB(getActivity()).getReadableDatabase();
        Cursor c = database.query(FilmTableHelper.TABLE_NAME, null, FilmTableHelper._ID + " = " + filmId, null, null, null, null);
        c.moveToNext();

        Glide.with(getActivity()).load(c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE))).into(imageView);
        name.setText(c.getString(c.getColumnIndex(FilmTableHelper.NAME)));
        description.setText(c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION)));
    }
}
