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

import com.example.projectworkv02.EndlessScrollListener;
import com.example.projectworkv02.FilmsApplication;
import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.Strings;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.internet.InternetCalls;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private EndlessScrollListener scrollListener;

    public HomeFragment (){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = view.findViewById(R.id.listAllFilms);
        GridLayoutManager lm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(lm);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);

        scrollListener = new EndlessScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d("ciao", "chiamata" + page);
                InternetCalls i = new InternetCalls();
                i.chiamataInternet(Strings.FILM, Strings.UPCOMING, Strings.ITALIAN, page, getActivity(), false);
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
        filmsAdapter = new FilmsAdapter(getActivity(), data);
        recyclerView.setAdapter(filmsAdapter);
        filmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}
