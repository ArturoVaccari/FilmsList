package com.example.projectworkv02.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.projectworkv02.R;
import com.example.projectworkv02.Strings;

import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.ui.filmDetailes.FilmDetailes;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.MyHolder> {

    private Context context;
    private Cursor film;

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
        final long id = film.getInt(film.getColumnIndex(FilmTableHelper._ID));
        final String img = film.getString(film.getColumnIndex(FilmTableHelper.IMGCARDBOARD));

        if (img.equals("null")) {
            Glide.with(context).load(R.drawable.img_placeholder).into(holder.image);
        } else {
            Glide.with(context).load(Strings.IMGPREFIX + img).into(holder.image);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilmDetailes.class);
                i.putExtra("film_id", id);
                context.startActivity(i);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (film == null) {
            return 0;
        } else return film.getCount();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
