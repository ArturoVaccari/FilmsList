package com.example.projectworkv02.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmProvider;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    Cursor cursor;

    public HomeFragment (){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);


        recyclerView = view.findViewById(R.id.listAllFilms);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(lm);

    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursor = data;
        Log.d("homefragment", "onLoadFinished: " + data.getCount());
        filmsAdapter = new FilmsAdapter(getActivity(), cursor);
        recyclerView.setAdapter(filmsAdapter);
        for (int i = 0; i<data.getCount(); i++) {
            cursor.moveToPosition(i);
            Log.d("homefragment", "onLoadFinished: " + cursor.getString(cursor.getColumnIndex("name")));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursor.close();
    }
}
