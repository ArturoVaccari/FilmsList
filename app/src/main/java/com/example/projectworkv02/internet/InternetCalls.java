package com.example.projectworkv02.internet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectworkv02.FilmsApplication;
import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.Strings;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmDB;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class InternetCalls {

    public void chiamataInternet (String tipo, String chiamata, String language, int page, final Context context, final boolean applicationStart) {
        Log.d("ciao", "chiamataInternet" + page);

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://api.themoviedb.org/3/" + tipo + "/" + chiamata + "?api_key=649482baeb3f20188d5cabbd5d83f466" +
                "&language=" + language + "&page=" + page;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray films = object.getJSONArray("results");

                            int filmCounter = 0;

                            for(int i = 0; i<films.length(); i++) {
                                JSONObject obj = films.getJSONObject(i);

                                long id = obj.getLong("id");
                                Cursor c = context.getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper._ID + " = " + id, null, null);
                                if (c.getCount() == 0) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(FilmTableHelper._ID, id);
                                    contentValues.put(FilmTableHelper.NAME, obj.getString("title"));
                                    contentValues.put(FilmTableHelper.DESCRIPTION, obj.getString("overview"));
                                    contentValues.put(FilmTableHelper.IMGCARDBOARD, obj.getString("poster_path"));
                                    contentValues.put(FilmTableHelper.IMGLARGE, obj.getString("backdrop_path"));

                                    filmCounter ++;
                                    context.getContentResolver().insert(FilmProvider.FILMS_URI, contentValues);
                                }
                            }
                            if (filmCounter < 10 && !applicationStart) {
                                FilmsApplication.page ++;
                                chiamataInternet(Strings.FILM, Strings.UPCOMING, Strings.ITALIAN, FilmsApplication.page, context, false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
