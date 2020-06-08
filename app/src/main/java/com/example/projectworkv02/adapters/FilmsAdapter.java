package com.example.projectworkv02.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.projectworkv02.R;
import com.example.projectworkv02.utility.StaticValues;

import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.ui.filmDetailes.FilmDetailes;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.MyHolder> {

    private Context context;
    private Cursor film;
    private LongItemClickListener longItemClickListener;

    public FilmsAdapter(Context context, Cursor film) {
        this.context = context;
        this.film = film;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (!film.moveToPosition(position)) {
            return;
        }
        final Film f = new Film();
        f.setFilm_id(film.getInt(film.getColumnIndex(FilmTableHelper.FILM_ID)));
        f.setName(film.getString(film.getColumnIndex(FilmTableHelper.NAME)));
        f.setImgLarge(film.getString(film.getColumnIndex(FilmTableHelper.IMGLARGE)));
        f.setImgCardboard(film.getString(film.getColumnIndex(FilmTableHelper.IMGCARDBOARD)));
        f.setWatch(film.getInt(film.getColumnIndex(FilmTableHelper.WATCH)));
        f.setDescription(film.getString(film.getColumnIndex(FilmTableHelper.DESCRIPTION)));
        f.setVote(film.getFloat(film.getColumnIndex(FilmTableHelper.API_VOTE)));
        f.setPersonalVote(film.getFloat(film.getColumnIndex(FilmTableHelper.PERSONAL_VOTE)));

        if (f.getImgCardboard() == null || f.getImgCardboard().equals("null") ) {
            Glide.with(context).load(R.drawable.img_placeholder).into(holder.image);
        } else {
            Glide.with(context).load(StaticValues.IMGPREFIX + f.getImgCardboard()).into(holder.image);
        }

        if (f.getName() == null || f.getName().equals("")) {
            holder.title.setText(context.getText(R.string.text_no_title).toString());
        } else {
            holder.title.setText(f.getName());
        }

        if (f.getPersonalVote() == 0 && f.getVote() == 0) {
            holder.vote.setText(context.getText(R.string.not_available).toString());
        } else if (f.getPersonalVote() == 0 && f.getVote() != 0) {
            holder.vote.setText(f.getVote() + "");
        } else {
            holder.vote.setText(f.getPersonalVote() + "");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilmDetailes.class);
                i.putExtra("calling", StaticValues.LOCAL_DETAILES);
                i.putExtra("film_id", f.getFilm_id());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (film == null) {
            return 0;
        } else return film.getCount();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView title;
        TextView vote;
        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.list_title);
            vote = itemView.findViewById(R.id.vote);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            if(longItemClickListener != null) {
                longItemClickListener.onLongItemClick(v,getAdapterPosition());
            }
            return true;
        }
    }

    public void setCursor (Cursor cursor) {
        this.film = cursor;
    }

    public interface LongItemClickListener{
        void onLongItemClick(View view, int position);
    }

    public void setLongItemClickListener(LongItemClickListener longItemClickListener) {
        this.longItemClickListener = longItemClickListener;
    }
}
