package com.example.projectworkv02.ui.home;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
import com.example.projectworkv02.internet.InternetCalls;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FilmsAdapter.LongItemClickListener{

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private EndlessScrollListener scrollListener;
    private static final int LOADER_ID = 1;
    Cursor c;

    public HomeFragment (){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



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
                i.chiamataInternet(StaticValues.FILM, StaticValues.UPCOMING, StaticValues.ITALIAN, page, getActivity(), false);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d("ciao", "onLoadFinished: " + data.getCount());
        if(recyclerView.getAdapter() == null){
            filmsAdapter = new FilmsAdapter(getActivity(), data);
            filmsAdapter.setLongItemClickListener(this);
            recyclerView.setAdapter(filmsAdapter);
        } else {
            filmsAdapter.setCursor(data);
        }
        filmsAdapter.notifyDataSetChanged();

        c = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onLongItemClick(View view, int position) {
        Log.d("dialog", "onLongItemClick: " + position);
        c.moveToPosition(position);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ConfirmDialog dialogFragment = new ConfirmDialog(c.getInt(c.getColumnIndex(FilmTableHelper._ID)), StaticValues.FRAGMENT_HOME);
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
    }
}
