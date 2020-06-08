package com.example.projectworkv02.ui.filmDetailes;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectworkv02.R;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;

public class FilmDetailes extends AppCompatActivity{

    private ImageView imageView;
    private TextView name, description, releaseDateView, voteView;
    private SeekBar voteBar;
    private int calling;
    private long filmId;
    private Cursor c;
    private String imgUrl;
    private String title;
    private String descriptionFilm;
    private String imgCardboard;
    private String releaseDate;
    private float vote;
    private float personalVote = 0;
    private int personalVoteTimesTen;
    private MenuItem watch;
    private MenuItem watched;
    private MenuItem remove_watch;
    private MenuItem remove_watched;
    private int isWatch;
    private int isWatched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detailes);

        imageView = findViewById(R.id.largeImage);
        name = findViewById(R.id.textName);
        description = findViewById(R.id.textDescription);
        releaseDateView = findViewById(R.id.textReleaseDate);
        voteView = findViewById(R.id.textVote);
        voteBar = findViewById(R.id.sliderVote);

        // custom toolbar con titolo e freccia per chiude l'activity
        Toolbar toolbar = findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        this.setTitle(getText(R.string.detailes));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // controllo per sapere da cosa è stata chiamata l'activity
                if (calling == StaticValues.LOCAL_DETAILES){
                    updateVote();
                } else {
                    finish();
                }
            }
        });

        calling = getIntent().getIntExtra("calling", 0);
        filmId = getIntent().getLongExtra("film_id", 0);

        // metodo che ottiene i dati da mostrare in base a cosa ha chiamato quest'activity
        if (calling == StaticValues.LOCAL_DETAILES) {

            c = getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper.FILM_ID + " = " + filmId, null, null, null);
            c.moveToNext();
            imgUrl = c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE));
            title = c.getString(c.getColumnIndex(FilmTableHelper.NAME));
            descriptionFilm = c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION));
            isWatch = c.getInt(c.getColumnIndex(FilmTableHelper.WATCH));
            isWatched = c.getInt(c.getColumnIndex(FilmTableHelper.WATCHED));
            releaseDate = c.getString(c.getColumnIndex(FilmTableHelper.RELEASE_DATE));
            vote = c.getFloat(c.getColumnIndex(FilmTableHelper.API_VOTE));
            personalVote = c.getFloat(c.getColumnIndex(FilmTableHelper.PERSONAL_VOTE));

        } else if (calling == StaticValues.INTERNET_DETAILES) {

            try {
                c = getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper.FILM_ID + " = " + filmId, null, null, null);
            } catch (Exception e){
                Log.e("errore", "" + e);
            }
            if (c.getCount() == 0) {
                imgUrl = getIntent().getStringExtra("img_url");
                title = getIntent().getStringExtra("title_film");
                descriptionFilm = getIntent().getStringExtra("description_film");
                imgCardboard = getIntent().getStringExtra("img_cardboard");
                vote = getIntent().getFloatExtra("vote", 0);
                releaseDate = getIntent().getStringExtra("release_date");
                isWatch = StaticValues.WATCH_FALSE;
                isWatched = StaticValues.WATCHED_FALSE;
            } else {
                c.moveToNext();
                imgUrl = c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE));
                imgCardboard = c.getString(c.getColumnIndex(FilmTableHelper.IMGCARDBOARD));
                title = c.getString(c.getColumnIndex(FilmTableHelper.NAME));
                descriptionFilm = c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION));
                isWatch = c.getInt(c.getColumnIndex(FilmTableHelper.WATCH));
                isWatched = c.getInt(c.getColumnIndex(FilmTableHelper.WATCHED));
                vote = c.getFloat(c.getColumnIndex(FilmTableHelper.API_VOTE));
                releaseDate = c.getString(c.getColumnIndex(FilmTableHelper.RELEASE_DATE));
                personalVote = c.getFloat(c.getColumnIndex(FilmTableHelper.PERSONAL_VOTE));
            }
        }

        // controllo se il link dell'immagine da mostrare è nullo o vuoto ed in caso carica l'immagine
        if (imgUrl == null || imgUrl.equals("null")) {
            Glide.with(this).load(R.drawable.img_placeholder).into(imageView);
        } else {
            Glide.with(this).load(StaticValues.IMGPREFIX + imgUrl).into(imageView);
        }

        // controllo se il titolo del film da mostrare è nullo o vuoto ed in caso lo scrive
        if (title == null || title.equals("")) {
            name.setText(getText(R.string.text_no_title).toString());
        } else {
            name.setText(title);
        }

        // controllo se la trama del film da mostrare è nullo o vuoto ed in caso lo scrive
        if (description == null || descriptionFilm.equals("")) {
            description.setText(getText(R.string.text_no_description).toString());
        } else {
            description.setText(descriptionFilm);
        }

        // controllo se il release_date del film da mostrare è nullo o vuoto ed in caso lo scrive
        if (releaseDate == null || releaseDate.equals("")) {
            releaseDateView.setText(getText(R.string.text_no_description).toString());
        } else {
            releaseDateView.setText(releaseDate);
        }

        // controllo se il voto del film da mostrare è nullo o vuoto ed in caso lo scrive quello dell'utente
        if (personalVote == 0 && vote == 0) {
            voteView.setText(getText(R.string.not_available).toString());
        } else if (personalVote == 0 && vote != 0) {
            voteView.setText(vote + "");
            personalVoteTimesTen = (int) vote*10;
            voteBar.setProgress(personalVoteTimesTen);
        } else {
            voteView.setText(personalVote + "");
            personalVoteTimesTen = (int) personalVote*10;
            voteBar.setProgress(personalVoteTimesTen);
        }

        // metodo per mostrare il voto del film e mostrare in maniera dinamila il valore che cambia al muoversi dello slider
        voteBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                personalVote = (float) progress/10;
                voteView.setText(personalVote + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                personalVote = (float) seekBar.getProgress()/10;
                voteView.setText(personalVote + "");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                personalVote = (float) seekBar.getProgress()/10;
                voteView.setText(personalVote + "");
            }
        });
    }

    // metodo per aggiornare il voto del film nel momento in cui l'utente vuole lasciare l'activity
    private void updateVote() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FilmTableHelper.PERSONAL_VOTE, personalVote);
        getContentResolver().update(FilmProvider.FILMS_URI, contentValues, FilmTableHelper.FILM_ID  + " = " + filmId, null);
        finish();
    }

    // metodo che controlla il valore delle variabili "isWatch" e "isWatched" e mostra le opzioni corrette per il caso
    private void controlWatchWatched(){
        if (isWatch == StaticValues.WATCH_FALSE){
            watch.setVisible(true);
            remove_watch.setVisible(false);
        }

        if (isWatch == StaticValues.WATCH_TRUE){
            watch.setVisible(false);
            remove_watch.setVisible(true);
        }

        if (isWatched == StaticValues.WATCHED_FALSE){
            watched.setVisible(true);
            remove_watched.setVisible(false);
        }

        if (isWatched == StaticValues.WATCHED_TRUE){
            watched.setVisible(false);
            remove_watched.setVisible(true);
        }
    }

    // metodo per salvare i dati di un film quando il film mostrato non è già presente nel db
    private void saveFilm(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FilmTableHelper.FILM_ID, filmId);
        contentValues.put(FilmTableHelper.NAME, title);
        contentValues.put(FilmTableHelper.DESCRIPTION, descriptionFilm);
        contentValues.put(FilmTableHelper.IMGCARDBOARD, imgCardboard);
        contentValues.put(FilmTableHelper.IMGLARGE, imgUrl);
        contentValues.put(FilmTableHelper.WATCH, isWatch);
        contentValues.put(FilmTableHelper.WATCHED, isWatched);
        contentValues.put(FilmTableHelper.API_VOTE, vote);
        contentValues.put(FilmTableHelper.RELEASE_DATE, releaseDate);
        contentValues.put(FilmTableHelper.PERSONAL_VOTE, personalVote);

        getContentResolver().insert(FilmProvider.FILMS_URI, contentValues);
    }

    // menu nella toolbar contenente le 4 opzioni per aggiungere/rimuovere i film dalla lista di watch e/o watched
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.watch_watched_menu, menu);
        watch = menu.findItem(R.id.watch_icon);
        watched = menu.findItem(R.id.watched_icon);
        remove_watch = menu.findItem(R.id.watch_false_icon);
        remove_watched = menu.findItem(R.id.watched_false_icon);
        return true;
    }

    // al click degli item del menu aggiorna i dati del film o lo salva nel db
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        controlWatchWatched();

        watch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(FilmDetailes.this, "Aggiunto ai film da guardare", Toast.LENGTH_LONG).show();
                isWatch = StaticValues.WATCH_TRUE;
                isWatched = StaticValues.WATCHED_FALSE;
                if (calling == StaticValues.LOCAL_DETAILES) {
                    ContentValues values = new ContentValues();
                    values.put(FilmTableHelper.WATCH, isWatch);
                    values.put(FilmTableHelper.WATCHED, isWatched);
                    getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper.FILM_ID + " = " + filmId, null);
                } else if (calling == StaticValues.INTERNET_DETAILES) {
                    saveFilm();
                }
                controlWatchWatched();
                return true;
            }
        });

        watched.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(FilmDetailes.this, "Aggiunto ai film visti", Toast.LENGTH_LONG).show();
                isWatched = StaticValues.WATCHED_TRUE;
                isWatch = StaticValues.WATCH_FALSE;
                if (calling == StaticValues.LOCAL_DETAILES) {
                    ContentValues values = new ContentValues();
                    values.put(FilmTableHelper.WATCHED, isWatched);
                    values.put(FilmTableHelper.WATCH, isWatch);
                    getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper.FILM_ID + " = " + filmId, null);
                } else if (calling == StaticValues.INTERNET_DETAILES){
                    saveFilm();
                }
                controlWatchWatched();
                return true;
            }
        });

        remove_watch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ContentValues values = new ContentValues();
                values.put(FilmTableHelper.WATCH, StaticValues.WATCH_FALSE);
                getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper.FILM_ID + " = " + filmId, null);
                Toast.makeText(FilmDetailes.this, "Rimosso dai film da guardare", Toast.LENGTH_LONG).show();
                isWatch = StaticValues.WATCH_FALSE;
                controlWatchWatched();
                return true;
            }
        });

        remove_watched.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ContentValues values = new ContentValues();
                values.put(FilmTableHelper.WATCHED, StaticValues.WATCHED_FALSE);
                getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper.FILM_ID + " = " + filmId, null);
                Toast.makeText(FilmDetailes.this, "Rimoso dai film visti", Toast.LENGTH_LONG).show();
                isWatched = StaticValues.WATCHED_FALSE;
                controlWatchWatched();
                return true;
            }
        });
        return true;
    }

    // nel caso le informazioni vengano prese dal db locale aggiorna il voto prima di uscire dalla schermata
    @Override
    public void onBackPressed() {
        if(calling == StaticValues.LOCAL_DETAILES) {
            updateVote();
        } else {
            super.onBackPressed();
        }
    }
}
