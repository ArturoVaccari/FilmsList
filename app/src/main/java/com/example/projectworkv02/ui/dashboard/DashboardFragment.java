package com.example.projectworkv02.ui.dashboard;

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
import com.example.projectworkv02.R;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView listWatchFilms;
    private FilmsAdapter adapter;
    private static final int LOADER_ID = 2;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listWatchFilms = view.findViewById(R.id.listWatchFilms);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
        listWatchFilms.setLayoutManager(lm);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, FilmTableHelper.WATCH + " LIKE 'true' ", null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("ciao", "preferiti: " + data.getCount());
        if(listWatchFilms.getAdapter() == null){
            adapter = new FilmsAdapter(getActivity(), data);
            listWatchFilms.setAdapter(adapter);
        } else {
            adapter.setCursor(data);
        }
        adapter.notifyDataSetChanged();

    for (int i = 0; i < data.getCount(); i++){
        data.moveToPosition(i);
        Log.d("film", "film: " + i + " nome: " + data.getString(data.getColumnIndex(FilmTableHelper.NAME)) + "--- watch: " + data.getString(data.getColumnIndex(FilmTableHelper.WATCH)));
    }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}