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
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        /**for (int i = 0; i<film.getCount(); i++) {
            film.moveToPosition(i);
            Log.d("filmadapter", "onLoadFinished: " + film.getString(film.getColumnIndex("name")));
        }*/
        film.moveToPosition(position);
        Glide.with(context).load(Strings.IMGPREFIX + film.getString(film.getColumnIndex("imgcardboard"))).into(holder.image);
        Log.d("filmadapter", "onBindViewHolder: " + position + " " + film.getString(film.getColumnIndex("_id")));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilmDetailes.class);
                i.putExtra("film_id", film.getLong(0));
                Log.d("filmadapter", "onClick: " + Long.valueOf(film.getString(film.getColumnIndex("_id"))));
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
            Log.d("filmadapter", "MyHolder: " + film.getCount());
        }
    }
}
