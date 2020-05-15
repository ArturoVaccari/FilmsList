package com.example.projectworkv02.ui.dashboard;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.R;
import com.example.projectworkv02.StaticValues;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.fragments.ConfirmDialog;
import com.example.projectworkv02.fragments.ConfirmDialogListener;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ConfirmDialogListener {

    private RecyclerView listWatchFilms;
    private FilmsAdapter adapter;
    private static final int LOADER_ID = 2;
    Cursor c;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.title_dashboard));

        listWatchFilms = view.findViewById(R.id.listWatchFilms);
        GridLayoutManager manager;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new GridLayoutManager(getActivity(),3);
        }else{
            manager = new GridLayoutManager(getActivity(),2);
        }
        listWatchFilms.setLayoutManager(manager);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, FilmTableHelper.WATCH + " = " + StaticValues.WATCH_TRUE, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("ciao", "preferiti: " + data.getCount());
        if(listWatchFilms.getAdapter() == null){
            adapter = new FilmsAdapter(getActivity(), data, this, getString(R.string.title_confirm_remove));
            listWatchFilms.setAdapter(adapter);
        } else {
            adapter.setCursor(data);
        }
        adapter.notifyDataSetChanged();

        c = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onPositivePressed(long id) {
        ContentValues values = new ContentValues();
        values.put(FilmTableHelper.WATCH, StaticValues.WATCH_FALSE);
        getActivity().getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper._ID + " = " + id, null);
        Toast.makeText(getActivity(), "Rimosso dai film da guardare", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativePressed() {

    }
}