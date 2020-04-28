package com.example.projectworkv02.internet;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmDB;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class InternetCalls {

    private SQLiteDatabase db;

    public void chiamataInternet (String tipo, String chiamata, String language, final Context context) {

        db = new FilmDB(context).getWritableDatabase();

        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://api.themoviedb.org/3/" + tipo + "/" + chiamata + "?api_key=649482baeb3f20188d5cabbd5d83f466" +
                "&language=" + language;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray films = object.getJSONArray("results");
                            for(int i = 0; i<films.length(); i++) {
                                JSONObject obj = films.getJSONObject(i);

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(FilmTableHelper._ID, obj.getLong("id"));
                                contentValues.put(FilmTableHelper.NAME, obj.getString("title"));
                                contentValues.put(FilmTableHelper.DESCRIPTION, obj.getString("overview"));
                                contentValues.put(FilmTableHelper.IMGCARDBOARD, obj.getString("poster_path"));
                                contentValues.put(FilmTableHelper.IMGLARGE, obj.getString("backdrop_path"));

                                context.getContentResolver().insert(FilmProvider.FILMS_URI, contentValues);
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
