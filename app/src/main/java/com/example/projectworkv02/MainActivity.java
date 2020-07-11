package com.example.projectworkv02;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.internet.BulkDownloadSupport;
import com.example.projectworkv02.internet.InternetCalls;
import com.example.projectworkv02.internet.ServerCallback;
import com.example.projectworkv02.ui.searchFilms.SearchActivity;
import com.example.projectworkv02.utility.StaticValues;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);

        NavigationUI.setupWithNavController(navView, navController);

        BulkDownloadSupport bulkDownloadSupport = new BulkDownloadSupport();
        bulkDownloadSupport.askFilms(this);

        c = getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper.UPDATE + " = " + StaticValues.UPDATE_TRUE, null, null);
        c.moveToNext();
        updateNecessaryFilms();
    }

    // creazione di un custom actionbar con un pulsante che avvia l'activity per la ricerca di film per titolo
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search_icon);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            }
        });
       return true;
    }

    private void updateNecessaryFilms() {
        for (int i = 0; i < c.getCount(); i++) {
            final long api_id = c.getLong(c.getColumnIndex(FilmTableHelper.FILM_ID));

            InternetCalls internetCalls = new InternetCalls();

            String url ="https://api.themoviedb.org/3/" + StaticValues.FILM + "/" + api_id + "?api_key=649482baeb3f20188d5cabbd5d83f466" +
                    "&language=" + StaticValues.ITALIAN + "&region=" + StaticValues.REGION_ITALIAN;

            internetCalls.chiamataInternet(url, MainActivity.this, new ServerCallback() {
                @Override
                public void onSuccess(JSONObject object) {
                    try {
                        JSONArray result = object.getJSONArray("results");
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject obj = result.getJSONObject(i);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(FilmTableHelper.FILM_ID, api_id);
                            contentValues.put(FilmTableHelper.NAME, obj.getString("title"));
                            contentValues.put(FilmTableHelper.DESCRIPTION, obj.getString("overview"));
                            contentValues.put(FilmTableHelper.IMGCARDBOARD, obj.getString("poster_path"));
                            contentValues.put(FilmTableHelper.IMGLARGE, obj.getString("backdrop_path"));
                            contentValues.put(FilmTableHelper.API_VOTE, obj.getString("vote_average"));
                            contentValues.put(FilmTableHelper.RELEASE_DATE, obj.getString("release_date"));
                            getContentResolver().update(FilmProvider.FILMS_URI, contentValues, FilmTableHelper.FILM_ID + " = " + api_id, null);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            c.moveToNext();
        }
    }
}