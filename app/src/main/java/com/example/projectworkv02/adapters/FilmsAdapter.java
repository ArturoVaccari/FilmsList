package com.example.projectworkv02.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectworkv02.MainActivity;
import com.example.projectworkv02.R;
import com.example.projectworkv02.Strings;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.ui.filmDetailes.FilmDetailes;

import java.util.ArrayList;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.MyHolder> {

    private Context context;
    private ArrayList<Film> listFilms;

    public FilmsAdapter(Context context, ArrayList<Film> listFilms) {
        this.context = context;
        this.listFilms = listFilms;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putLong("film_id", MainActivity.listFilms.get(position).getId());

                AppCompatActivity activity = (AppCompatActivity) context;
                Fragment myFragment = new FilmDetailes();
                FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.nav_host_fragment ,myFragment).addToBackStack(null);
                myFragment.setArguments(args);
                fragmentTransaction.commit();
            }
        });
        Glide.with(context).load(Strings.IMGPREFIX + MainActivity.listFilms.get(position).getImgCardboard()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listFilms.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
