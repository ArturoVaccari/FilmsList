package com.example.projectworkv02.ui.home;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.utility.EndlessScrollListener;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.fragments.ConfirmDialogListener;
import com.example.projectworkv02.internet.InternetCalls;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private EndlessScrollListener scrollListener;
    Cursor c;
    private CursorLoader cursorLoader;
    private int count = 0;

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
        getActivity().getSupportLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, null, this);

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
        c = data;
        if(recyclerView.getAdapter() == null){
            filmsAdapter = new FilmsAdapter(getActivity(), c);
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
}
