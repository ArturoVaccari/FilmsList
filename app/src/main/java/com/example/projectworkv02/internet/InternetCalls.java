package com.example.projectworkv02.internet;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.models.Film;
import com.example.projectworkv02.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;

public class InternetCalls {

    public void chiamataInternet (String tipo, String chiamata, String language, Context context) {

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
                                Film film = new Film();
                                film.setName(obj.getString("original_title"));
                                film.setDescription(obj.getString("overview"));
                                film.setImgCardboard(obj.getString("poster_path"));
                                film.setImgLarge(obj.getString("backdrop_path"));
                                MainActivity.listFilms.add(film);
                            }

                            Log.d(TAG, "onResponse: " + MainActivity.listFilms.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "onResponse: " + response);
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
