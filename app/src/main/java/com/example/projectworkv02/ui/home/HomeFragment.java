package com.example.projectworkv02.ui.home;

import android.content.res.Configuration;
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

import com.example.projectworkv02.internet.BulkDownloadSupport;
import com.example.projectworkv02.utility.EndlessScrollListener;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

// il fragment mostra tutti i film scaricati che non siano tra quelli da vedere o quelli visti
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private EndlessScrollListener scrollListener;
    Cursor c;

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
        // imposta il titolo nella toolbar della main_activity per farlo corrispondere al fragment
        getActivity().setTitle(getString(R.string.title_home));

        recyclerView = view.findViewById(R.id.listAllFilms);
        GridLayoutManager manager;
        // controllo sull'orientamento del telefono per decidere se mostrare due o tre film sulla stessa riga
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new GridLayoutManager(getActivity(),3);
        }else{
            manager = new GridLayoutManager(getActivity(),2);
        }
        recyclerView.setLayoutManager(manager);
        getActivity().getSupportLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, null, this);

        // metodo che permette di scaricare altri 20 film quando l'utente si avvicina alla fine della lista
        scrollListener = new EndlessScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                BulkDownloadSupport bulkDownloadSupport = new BulkDownloadSupport();
                Log.d("internet", "page " + page);
                bulkDownloadSupport.askFilms(getActivity());
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, FilmTableHelper.WATCH + " IS NOT " + StaticValues.WATCH_TRUE + " AND " + FilmTableHelper.WATCHED + " IS NOT " + StaticValues.WATCHED_TRUE, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //riempie il cursore con i dati presi dal db filtrando fuori i film che si trovano nella lista di film da vedere o visti
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(StaticValues.CURSOR_DISCOVER_ID, null, HomeFragment.this);
    }
}
