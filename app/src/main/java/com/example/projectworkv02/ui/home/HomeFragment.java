package com.example.projectworkv02.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.EndlessScrollListener;
import com.example.projectworkv02.StaticValues;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.fragments.ConfirmDialog;
import com.example.projectworkv02.fragments.ConfirmDialogListener;
import com.example.projectworkv02.internet.InternetCalls;
import com.example.projectworkv02.SearchActivity;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ConfirmDialogListener {

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private EndlessScrollListener scrollListener;
    private static final int LOADER_ID = 1;
    Cursor c;
    private SearchView searchView;
    private CursorLoader cursorLoader;
    private int count = 0;
    private MenuItem searchItem;

    public HomeFragment (){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getString(R.string.title_home));

        recyclerView = view.findViewById(R.id.listAllFilms);
        GridLayoutManager manager;
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new GridLayoutManager(getActivity(),3);
        }else{
            manager = new GridLayoutManager(getActivity(),2);
        }
        recyclerView.setLayoutManager(manager);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        scrollListener = new EndlessScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("ciao", "chiamata" + page);
                InternetCalls i = new InternetCalls();
                i.chiamataInternet(StaticValues.FILM, StaticValues.POPULAR, StaticValues.ITALIAN, page, StaticValues.REGION_ITALIAN, getActivity(), false);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == 1) {
            cursorLoader = new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, null, null, null);
        } else if (args != null){
            String name = args.getString("filter");
            cursorLoader = new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, FilmTableHelper.NAME + " LIKE '%" + name + "%' ", null, null);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("ciao", "onLoadFinished: " + data.getCount());
        c = data;
        if(recyclerView.getAdapter() == null){
            filmsAdapter = new FilmsAdapter(getActivity(), c, this, getString(R.string.title_confirm_add));
            recyclerView.setAdapter(filmsAdapter);
        } else {
            filmsAdapter.setCursor(c);
        }
        filmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        c = null;
        filmsAdapter.notifyDataSetChanged();
    }



    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(1, null, HomeFragment.this);

    }

    @Override
    public void onPositivePressed(long id) {
        ContentValues values = new ContentValues();
        values.put(FilmTableHelper.WATCH, StaticValues.WATCH_TRUE);
        getActivity().getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper._ID + " = " + id, null);
        Toast.makeText(getActivity(), "Aggiunto ai film da guardare", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativePressed() {

    }
}
