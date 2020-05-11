package com.example.projectworkv02.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.projectworkv02.R;
import com.example.projectworkv02.StaticValues;

import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmProvider;
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
        f.setId(film.getInt(film.getColumnIndex(FilmTableHelper._ID)));
        f.setName(film.getString(film.getColumnIndex(FilmTableHelper.NAME)));
        f.setImgLarge(film.getString(film.getColumnIndex(FilmTableHelper.IMGLARGE)));
        f.setImgCardboard(film.getString(film.getColumnIndex(FilmTableHelper.IMGCARDBOARD)));
        f.setWatch(film.getString(film.getColumnIndex(FilmTableHelper.WATCH)));
        f.setDescription(film.getString(film.getColumnIndex(FilmTableHelper.DESCRIPTION)));

        if (f.getImgCardboard().equals("null")) {
            Glide.with(context).load(R.drawable.img_placeholder).into(holder.image);
        } else {
            Glide.with(context).load(StaticValues.IMGPREFIX + f.getImgCardboard()).into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FilmDetailes.class);
                i.putExtra("film_id", f.getId());
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

        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
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
