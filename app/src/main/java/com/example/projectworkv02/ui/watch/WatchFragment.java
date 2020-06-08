package com.example.projectworkv02.ui.watch;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.R;
import com.example.projectworkv02.fragments.ConfirmDialog;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.fragments.ConfirmDialogListener;

public class WatchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FilmsAdapter.LongItemClickListener, ConfirmDialogListener{

    private RecyclerView listWatchFilms;
    private FilmsAdapter adapter;
    private Cursor c;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_watch, container, false);
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
        getActivity().getSupportLoaderManager().initLoader(StaticValues.CURSOR_WATCH_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, FilmTableHelper.WATCH + " = " + StaticValues.WATCH_TRUE, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        c=data;

        if(listWatchFilms.getAdapter() == null){
            adapter = new FilmsAdapter(getActivity(), c);
            listWatchFilms.setAdapter(adapter);
            adapter.setLongItemClickListener(this);
        } else {
            adapter.setCursor(c);
        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onLongItemClick(View view, int position) {
        c.moveToPosition(position);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        ConfirmDialog dialogFragment = new ConfirmDialog(c.getLong(c.getColumnIndex(FilmTableHelper.FILM_ID)), R.string.remove_watch, this);

        dialogFragment.show(fragmentManager, null);
    }

    @Override
    public void onPositivePressed(long film_id, int title_resource) {
        ContentValues values = new ContentValues();
        values.put(FilmTableHelper.WATCH, StaticValues.WATCH_FALSE);
        getActivity().getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper.FILM_ID + " = " + film_id, null);
        Toast.makeText(getActivity(), "Rimosso dai film da guardare", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativePressed() {

    }
}