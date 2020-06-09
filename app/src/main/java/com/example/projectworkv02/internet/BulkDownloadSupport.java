package com.example.projectworkv02.internet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.utility.StaticValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BulkDownloadSupport {

    // metodo che fa una chiamata ad internet per scaricare una pagina di film sia all'avvio dell'app, e sia quando l'utente si
    // avvicina alla fine della lista.
    public void askFilms(final Context context) {
        InternetCalls internetCalls = new InternetCalls();
        String url ="https://api.themoviedb.org/3/" + StaticValues.FILM + "/" + StaticValues.POPULAR + "?api_key=649482baeb3f20188d5cabbd5d83f466" +
                "&language=" + StaticValues.ITALIAN + "&page=" + StaticValues.page + "&region=" + StaticValues.REGION_ITALIAN;

        internetCalls.chiamataInternet(url, context, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONArray films = object.getJSONArray("results");
                    StaticValues.MAXPAGE = Integer.parseInt(object.getString("total_pages"));
                    Log.d("internet", "maxpages " + StaticValues.MAXPAGE);
                    saveData(films, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // metodo per salvare i film scaricati dalla schermata Discover
    private void saveData(JSONArray films, Context context){
        int filmCounter = 0;
        try {
            // salva un film alla volta nel db
            for(int i = 0; i<films.length(); i++) {
                JSONObject obj = films.getJSONObject(i);

                long id = obj.getLong("id");
                ContentValues contentValues = new ContentValues();
                contentValues.put(FilmTableHelper.FILM_ID, id);
                contentValues.put(FilmTableHelper.NAME, obj.getString("title"));
                contentValues.put(FilmTableHelper.DESCRIPTION, obj.getString("overview"));
                contentValues.put(FilmTableHelper.IMGCARDBOARD, obj.getString("poster_path"));
                contentValues.put(FilmTableHelper.IMGLARGE, obj.getString("backdrop_path"));
                contentValues.put(FilmTableHelper.API_VOTE, obj.getString("vote_average"));
                contentValues.put(FilmTableHelper.RELEASE_DATE, obj.getString("release_date"));
                Cursor c = context.getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper.FILM_ID + " = " + id, null, null);
                if (c.getCount() == 0) {
                    filmCounter++;
                    context.getContentResolver().insert(FilmProvider.FILMS_URI, contentValues);
                } else {
                    context.getContentResolver().update(FilmProvider.FILMS_URI, contentValues, FilmTableHelper.FILM_ID + " = " + id, null);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // controllo sul numero di film scaricati, se sono meno di venti significa che non è stata scaricata una pagina intera e quindi
        // il valore di StaticValues.page va incrementato.
        if (filmCounter < 20 && StaticValues.page <= StaticValues.MAXPAGE) {
            StaticValues.page ++;
            askFilms(context);
        }
    }
}
