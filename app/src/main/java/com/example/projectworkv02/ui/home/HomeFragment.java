package com.example.projectworkv02.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.FilmsLoader;
import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.Film;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Film>>{

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;

    public HomeFragment (){
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.listAllFilms);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(lm);
        filmsAdapter = new FilmsAdapter(getActivity(), MainActivity.listFilms);
        recyclerView.setAdapter(filmsAdapter);
    }


    @NonNull
    @Override
    public Loader<ArrayList<Film>> onCreateLoader(int id, @Nullable Bundle args) {
        //FilmsLoader filmsLoader = new FilmsLoader(getActivity());
        //return filmsLoader;
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Film>> loader, ArrayList<Film> data) {
        filmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Film>> loader) {

    }
}
