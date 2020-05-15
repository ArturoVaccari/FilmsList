package com.example.projectworkv02;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectworkv02.adapters.SearchedFilmsAdapter;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.fragments.ConfirmDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity implements SearchedFilmsAdapter.ItemClickListener, ConfirmDialogListener {

    private String string;
    private RecyclerView recyclerView;
    private SearchedFilmsAdapter adapter;
    private ArrayList<Film> films = new ArrayList<>();
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;
    private ImageView imageView;
    private TextView title, description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
        Log.d("ciao", "sono stato creatoooo");

        linearLayout = findViewById(R.id.layoutSearch);
        constraintLayout = findViewById(R.id.layoutDeatiles);
        imageView = findViewById(R.id.largeImage2);
        title = findViewById(R.id.textName2);
        description = findViewById(R.id.textDescription2);

        recyclerView = findViewById(R.id.searchedFilms);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SearchedFilmsAdapter(this, films, this, getString(R.string.title_confirm_add));
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.detailsToolbar2);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle(getText(R.string.title_find_films));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        SearchView searchView = findViewById(R.id.search);
        searchView.onActionViewExpanded();
        Log.d("ciao", "focus: " + searchView.hasFocus());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(SearchActivity.this, "trova=" + query, Toast.LENGTH_SHORT).show();
                films.clear();
                adapter.notifyDataSetChanged();
                string = query.replaceAll("\\s+", "%20");
                sendInternetRequest(SearchActivity.this, string);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(SearchActivity.this, "input=" + newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void sendInternetRequest (final Context context, String request) {
        Log.d("search", "filtro " + request);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://api.themoviedb.org/3/search/movie?api_key=649482baeb3f20188d5cabbd5d83f466&language=it-IT&query=" + request + "&page=1&region=IT";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray film = object.getJSONArray("results");
                            int maxPages = Integer.parseInt(object.getString("total_pages"));
                            Log.d("search", "onResponse: " + maxPages);

                            for (int i = 0; i < film.length(); i++) {
                                JSONObject obj = film.getJSONObject(i);

                                Film f = new Film();
                                f.setId(obj.getLong("id"));
                                f.setName(obj.getString("title"));
                                f.setDescription(obj.getString("overview"));
                                f.setImgCardboard(obj.getString("poster_path"));
                                f.setImgLarge(obj.getString("backdrop_path"));
                                f.setWatch(StaticValues.WATCH_FALSE);

                                films.add(f);
                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("search", "onErrorResponse: ", error);
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
}

    @Override
    public void onItemClick(View view, int position) {
        Film f = adapter.getItem(position);
        showDetailes(f);
    }

    private void showDetailes(Film f) {
        linearLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);

        if (f.getImgLarge().equals("null")) {
            Glide.with(this).load(R.drawable.img_placeholder).into(imageView);
        } else {
            Glide.with(this).load(StaticValues.IMGPREFIX + f.getImgLarge()).into(imageView);
        }

        if (f.getName().equals("")) {
            title.setText(getText(R.string.text_no_title).toString());
        } else {
            title.setText(f.getName());
        }

        if (f.getDescription().equals("")) {
            description.setText(getText(R.string.text_no_description).toString());
        } else {
            description.setText(f.getDescription());
        }
    }

    private void showList() {
        constraintLayout.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (constraintLayout.getVisibility() == View.VISIBLE) {
            showList();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPositivePressed(long id) {
        int position = 0;
        for (int i = 0; i < films.size(); i++) {
            Film f = films.get(i);
            if (f.getId() == id){
                position = i;
            }
        }

        Film f = films.get(position);
        ContentValues contentValues = new ContentValues();
        contentValues.put(FilmTableHelper._ID, id);
        contentValues.put(FilmTableHelper.NAME, f.getName());
        contentValues.put(FilmTableHelper.DESCRIPTION, f.getDescription());
        contentValues.put(FilmTableHelper.IMGCARDBOARD, f.getImgCardboard());
        contentValues.put(FilmTableHelper.IMGLARGE, f.getImgLarge());
        contentValues.put(FilmTableHelper.WATCH, StaticValues.WATCH_TRUE);

        Cursor c = getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper._ID + " = " + id, null, null, null);
        if (c.getCount() == 0) {
            getContentResolver().insert(FilmProvider.FILMS_URI, contentValues);
        } else {
            getContentResolver().update(FilmProvider.FILMS_URI, contentValues, FilmTableHelper._ID + " = " + id, null);
        }
        Toast.makeText(this, "Aggiunto ai film da guardare", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativePressed() {

    }
}
