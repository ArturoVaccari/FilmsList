package com.example.projectworkv02.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectworkv02.R;
import com.example.projectworkv02.ui.filmDetailes.FilmDetailes;
import com.example.projectworkv02.utility.StaticValues;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.fragments.ConfirmDialog;
import com.example.projectworkv02.fragments.ConfirmDialogListener;

import java.util.ArrayList;

public class SearchedFilmsAdapter extends RecyclerView.Adapter<SearchedFilmsAdapter.MyHolder> {

    private Context context;
    private ArrayList<Film> films;

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
        if (film.getImgCardboard() == null || film.getImgCardboard().equals("null")) {
            Glide.with(context).load(R.drawable.img_placeholder).into(holder.image);
        } else {
            Glide.with(context).load(StaticValues.IMGPREFIX + film.getImgCardboard()).into(holder.image);
        }

        if (film.getName() == null || film.getName().equals("")) {
            holder.title.setText(context.getText(R.string.text_no_title).toString());
        } else {
            holder.title.setText(film.getName());
        }

        if (film.getPersonalVote() == 0 && film.getVote() == 0) {
            holder.vote.setText(context.getText(R.string.not_available).toString());
        } else if (film.getPersonalVote() == 0 && film.getVote() != 0) {
            holder.vote.setText(film.getVote() + "");
        } else {
            holder.vote.setText(film.getPersonalVote() + "");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("adapter", "onClick: " + film.getImgCardboard());
                Intent i = new Intent(context, FilmDetailes.class);
                i.putExtra("calling", StaticValues.INTERNET_DETAILES);
                i.putExtra("img_url", film.getImgLarge());
                i.putExtra("title_film", film.getName());
                i.putExtra("description_film", film.getDescription());
                i.putExtra("film_id", film.getFilm_id());
                i.putExtra("img_cardboard", film.getImgCardboard());
                i.putExtra("vote", film.getVote());
                i.putExtra("release_date", film.getReleaseDate());
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

        TextView title;
        ImageView image;
        TextView vote;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.list_title);
            vote = itemView.findViewById(R.id.vote);
        }
    }
}
