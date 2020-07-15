package com.example.projectworkv02.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projectworkv02.R;
import com.example.projectworkv02.ui.filmDetailes.FilmDetailes;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.database.Film;

import java.util.ArrayList;

public class SearchedFilmsAdapter extends RecyclerView.Adapter<SearchedFilmsAdapter.MyHolder> {

    private Context context;
    private ArrayList<Film> films;

    //costruttore che prende contesto e dati dall'activity
    public SearchedFilmsAdapter(Context context, ArrayList<Film> films) {
        this.context = context;
        this.films = films;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final Film film = films.get(position);
        final long id = film.getFilm_id();

        // controllo che il contenuto della colonna con il link dell'immagine non sia nulla
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder)).
                load(StaticValues.IMGPREFIX + film.getImgCardboard()).into(holder.image);

        // controllo se il le colonne coi voti sono vuote, poi se il voto dell'utente è 0. Il voto dell'utente ha la priorità
        // in caso ci siano entrambi.
        if (film.getPersonalVote() == 0 && film.getVote() == 0) {
            holder.vote.setText(context.getText(R.string.not_available).toString());
        } else if (film.getPersonalVote() == 0 && film.getVote() != 0) {
            holder.vote.setText(film.getVote() + "");
        } else {
            holder.vote.setText(film.getPersonalVote() + "");
        }

        // al click lancia l'activity FilmDetailes e le fornisce i dati necessari ad eventualmente salvare il film nel database locale
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilmDetailes.class);
                i.putExtra("img_url", film.getImgLarge());
                i.putExtra("title_film", film.getName());
                i.putExtra("description_film", film.getDescription());
                i.putExtra("film_id", film.getFilm_id());
                i.putExtra("img_cardboard", film.getImgCardboard());
                i.putExtra("vote", film.getVote());
                i.putExtra("release_date", film.getReleaseDate());
                i.putExtra("update", StaticValues.UPDATE_TRUE);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (films == null) {
            return 0;
        } else return films.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView vote;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            vote = itemView.findViewById(R.id.vote);
        }
    }
}
