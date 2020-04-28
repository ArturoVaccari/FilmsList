package com.example.projectworkv02.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.R;
import com.example.projectworkv02.adapters.FilmsAdapter;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private RecyclerView listWatchFilms;
    private FilmsAdapter adapter;
    private ArrayList<String> lista = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lista.add("film1");
        lista.add("film2");
        lista.add("film3");
        lista.add("film4");
        lista.add("film5");
        lista.add("film6");

        listWatchFilms = view.findViewById(R.id.listWatchFilms);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(), 2);
        listWatchFilms.setLayoutManager(lm);
        adapter = new FilmsAdapter(getActivity(), null);
        listWatchFilms.setAdapter(adapter);
    }
}
