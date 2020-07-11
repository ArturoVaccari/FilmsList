package com.example.projectworkv02.ui.home;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectworkv02.fragments.ConfirmDialog;
import com.example.projectworkv02.fragments.FilterFragment;
import com.example.projectworkv02.fragments.FilterFragmentListener;
import com.example.projectworkv02.internet.BulkDownloadSupport;
import com.example.projectworkv02.utility.EndlessScrollListener;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.adapters.FilmsAdapter;
import com.example.projectworkv02.R;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// il fragment mostra tutti i film scaricati che non siano tra quelli da vedere o quelli visti
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, FilterFragmentListener {

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private EndlessScrollListener scrollListener;
    private FloatingActionButton filter;
    private Cursor c;
    private Bundle args = new Bundle();
    private static final String ORDERBY = "order_by";
    private String orderby;

    public HomeFragment (){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("lifecycle", "onCreate: ");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // imposta il titolo nella toolbar della main_activity per farlo corrispondere al fragment
        getActivity().setTitle(getString(R.string.title_home));
        Log.d("lifecycle", "onViewCreated: ");

        recyclerView = view.findViewById(R.id.listAllFilms);
        filter = view.findViewById(R.id.filterDiscover);
        GridLayoutManager manager;
        // controllo sull'orientamento del telefono per decidere se mostrare due o tre film sulla stessa riga
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            manager = new GridLayoutManager(getActivity(),3);
        }else{
            manager = new GridLayoutManager(getActivity(),2);
        }
        recyclerView.setLayoutManager(manager);

        args.putString(ORDERBY, FilmTableHelper._ID);

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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FilterFragment dialogFragment = new FilterFragment(HomeFragment.this);

                dialogFragment.show(fragmentManager, null);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String order ;
        if (args == null || args.getString(ORDERBY).equals("null")){
             order = orderby;
        } else {
            order = args.getString(ORDERBY);
        }
        return new CursorLoader(getActivity(), FilmProvider.FILMS_URI, null, FilmTableHelper.WATCH + " IS NOT " + StaticValues.WATCH_TRUE + " AND " + FilmTableHelper.WATCHED + " IS NOT " + StaticValues.WATCHED_TRUE, null, order);
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
        Log.d("lifecycle", "onResume: ");
        if (getLoaderManager().getLoader(StaticValues.CURSOR_DISCOVER_ID) == null){
            getLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
        } else {
            getLoaderManager().restartLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
        }
    }

    @Override
    public void orderBy(int orderId) {
        switch (orderId) {
            case StaticValues.ORDER_BY_VOTE_DESCENDING:
                args.putString(ORDERBY, FilmTableHelper.API_VOTE +" DESC");
                getLoaderManager().destroyLoader(StaticValues.CURSOR_DISCOVER_ID);
                getLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
                break;
            case StaticValues.ORDER_BY_VOTE_ASCENDING:
                args.putString(ORDERBY, FilmTableHelper.API_VOTE);
                getLoaderManager().destroyLoader(StaticValues.CURSOR_DISCOVER_ID);
                getLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
                break;
            case StaticValues.ORDER_BY_DATE_DESCENDING:
                args.putString(ORDERBY, FilmTableHelper.RELEASE_DATE +" DESC");
                getLoaderManager().destroyLoader(StaticValues.CURSOR_DISCOVER_ID);
                getLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
                break;
            case StaticValues.ORDER_BY_DATE_ASCENDING:
                args.putString(ORDERBY, FilmTableHelper.RELEASE_DATE);
                getLoaderManager().destroyLoader(StaticValues.CURSOR_DISCOVER_ID);
                getLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
                break;
            default: args.putString(ORDERBY, FilmTableHelper._ID);
                getLoaderManager().destroyLoader(StaticValues.CURSOR_DISCOVER_ID);
                getLoaderManager().initLoader(StaticValues.CURSOR_DISCOVER_ID, args, HomeFragment.this);
        }
    }
}
