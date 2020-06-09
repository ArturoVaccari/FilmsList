package com.example.projectworkv02.ui.searchFilms;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.projectworkv02.R;
import com.example.projectworkv02.internet.InternetCalls;
import com.example.projectworkv02.internet.ServerCallback;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.adapters.SearchedFilmsAdapter;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// activity che permette di fare la ricerca in internet e nel db locale
public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String string;
    private RecyclerView recyclerView;
    private SearchedFilmsAdapter adapter;
    private ArrayList<Film> films = new ArrayList<>();
    private SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try {
            if (getLastCustomNonConfigurationInstance() != null) {
                films = (ArrayList<Film>) getLastCustomNonConfigurationInstance();
            }
        } catch (NullPointerException e) {
            Log.e("moooona", "onCreate: " + e);
        }

        recyclerView = findViewById(R.id.searchedFilms);
        GridLayoutManager manager;
        // controllo sull'orientamento del telefono per decidere se mostrare due o tre film sulla stessa riga
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            manager = new GridLayoutManager(this, 3);
        } else {
            manager = new GridLayoutManager(this, 2);
        }
        recyclerView.setLayoutManager(manager);
        adapter = new SearchedFilmsAdapter(this, films);
        recyclerView.setAdapter(adapter);

        // toolbar custom per mettere la barra della ricerca
        Toolbar toolbar = findViewById(R.id.detailsToolbar2);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle(getText(R.string.title_find_films));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // metodo necessario a controllare e cancellare eventuali doppioni trovati nel momento del loading di dati
    // visto che la ricerca viene effettuata sia nel db locale che con una richiesta all'api
    private void runDuplicateControl() {
        for (int j = 0; j < films.size(); j++)
            for (int k = j + 1; k < films.size(); k++) {
                Film f1 = films.get(k);
                Film f2 = films.get(j);
                if (k != j && f1.getFilm_id() == f2.getFilm_id()) {
                    films.remove(k);
                }
            }
    }

    // metodo per fare la chiamata internet con la stringa ricercata dall'utente. Viene mostrata sempre solo la prima pagina dei risultati.
    public void searchFilm(String query, final Context context){
        InternetCalls internetCalls = new InternetCalls();

        String url ="https://api.themoviedb.org/3/" + StaticValues.SEARCH + "/" + StaticValues.FILM + "?api_key=649482baeb3f20188d5cabbd5d83f466" +
                "&language=" + StaticValues.ITALIAN + "&query=" + query + "&page=1&region=" + StaticValues.REGION_ITALIAN;

        internetCalls.chiamataInternet(url, context, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray result = object.getJSONArray("results");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject obj = result.getJSONObject(i);

                        Film f = new Film();
                        f.setFilm_id(obj.getLong("id"));
                        f.setName(obj.getString("title"));
                        f.setDescription(obj.getString("overview"));
                        f.setImgCardboard(obj.getString("poster_path"));
                        f.setImgLarge(obj.getString("backdrop_path"));
                        f.setVote(Float.valueOf(obj.getString("vote_average")));
                        f.setReleaseDate(obj.getString("release_date"));

                        films.add(f);
                        runDuplicateControl();
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String name = args.getString("query");
        return new CursorLoader(SearchActivity.this, FilmProvider.FILMS_URI, null, FilmTableHelper.NAME + " LIKE '%" + name + "%' ", null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        // filtra nel db per cercare film che hanno il titolo uguale in qualche modo alla stringa cercata dall'utente
        // e crea una istranza di film che aggiunge all'array assieme ai dati cercati in internet
        if (films.size() == 0) {
            for (int i = 0; i < data.getCount(); i++) {
                data.moveToNext();
                Film f = new Film();
                f.setFilm_id(data.getInt(data.getColumnIndex(FilmTableHelper.FILM_ID)));
                f.setName(data.getString(data.getColumnIndex(FilmTableHelper.NAME)));
                f.setDescription(data.getString(data.getColumnIndex(FilmTableHelper.DESCRIPTION)));
                f.setImgLarge(data.getString(data.getColumnIndex(FilmTableHelper.IMGLARGE)));
                f.setImgCardboard(data.getString(data.getColumnIndex(FilmTableHelper.IMGCARDBOARD)));
                f.setWatch(data.getInt(data.getColumnIndex(FilmTableHelper.WATCH)));
                f.setVote(data.getFloat(data.getColumnIndex(FilmTableHelper.API_VOTE)));
                f.setPersonalVote(data.getFloat(data.getColumnIndex(FilmTableHelper.PERSONAL_VOTE)));
                f.setReleaseDate(data.getString(data.getColumnIndex(FilmTableHelper.RELEASE_DATE)));

                films.add(f);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return films;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_search, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        // apertura obbligata all'avvio dell'activity della searchview
        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                films.clear();
                adapter.notifyDataSetChanged();
                searchView.onActionViewCollapsed();
                string = query.replaceAll("\\s+", "%20");
                Bundle bundle = new Bundle();
                bundle.putString("query", query);
                // controllo se il cursor loader è già stato inizializzato e in caso lo sia lo fa restartare.
                if (getSupportLoaderManager().getLoader(StaticValues.CURSOR_SEARCH_ID) == null) {
                    getSupportLoaderManager().initLoader(StaticValues.CURSOR_SEARCH_ID, bundle, SearchActivity.this);
                } else {
                    getSupportLoaderManager().restartLoader(StaticValues.CURSOR_SEARCH_ID, bundle, SearchActivity.this);
                }
                // chiamata al metodo che fa partire una ricerca all'api
                searchFilm(string, SearchActivity.this);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
}