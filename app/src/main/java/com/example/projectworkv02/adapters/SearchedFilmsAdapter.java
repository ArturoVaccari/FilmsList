package com.example.projectworkv02.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.projectworkv02.StaticValues;
import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmTableHelper;
import com.example.projectworkv02.fragments.ConfirmDialog;
import com.example.projectworkv02.fragments.ConfirmDialogListener;
import com.example.projectworkv02.ui.filmDetailes.FilmDetailes;

import java.util.ArrayList;

public class SearchedFilmsAdapter extends RecyclerView.Adapter<SearchedFilmsAdapter.MyHolder> {

    private Context context;
    private ArrayList<Film> films;
    private ConfirmDialogListener listener;
    private String dialogMessage;
    private ItemClickListener itemClickListener;

    public SearchedFilmsAdapter(Context context, ArrayList<Film> films, ConfirmDialogListener listener, String dialogMessage) {
        this.context = context;
        this.films = films;
        this.listener = listener;
        this.dialogMessage = dialogMessage;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Film film = films.get(position);
        final long id = film.getId();
        if (film.getImgCardboard().equals("null")) {
            Glide.with(context).load(R.drawable.img_placeholder).into(holder.image);
        } else {
            Glide.with(context).load(StaticValues.IMGPREFIX + film.getImgCardboard()).into(holder.image);
        }

        if (film.getName().equals("")) {
            holder.title.setText(context.getText(R.string.text_no_title).toString());
        } else {
            holder.title.setText(film.getName());
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) context;
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                ConfirmDialog dialogFragment = new ConfirmDialog(id, dialogMessage, listener);

                dialogFragment.show(fragmentManager, null);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (films == null) {
            return 0;
        } else return films.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.list_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null){
                itemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public Film getItem(int id) {
        return films.get(id);
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
