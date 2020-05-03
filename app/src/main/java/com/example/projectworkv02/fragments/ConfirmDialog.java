package com.example.projectworkv02.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectworkv02.R;
import com.example.projectworkv02.StaticValues;
import com.example.projectworkv02.database.FilmProvider;
import com.example.projectworkv02.database.FilmTableHelper;


public class ConfirmDialog extends DialogFragment {

    private long id;
    private int frag;

    public ConfirmDialog() {
        // Required empty public constructor
    }

    public ConfirmDialog(int id, int frag) {
        this.id = id;
        this.frag = frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Cursor c = getActivity().getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper._ID + " = " + id, null, null, null);
        c.moveToNext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        if (frag == StaticValues.FRAGMENT_HOME) {
            dialog.setTitle(getText(R.string.title_confirm_add).toString());
        } else {
            dialog.setTitle(getText(R.string.title_confirm_remove).toString());
        }
        dialog.setPositiveButton(getText(R.string.confirm).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues values = new ContentValues();
                values.put(FilmTableHelper.NAME, c.getString(c.getColumnIndex(FilmTableHelper.NAME)));
                values.put(FilmTableHelper.DESCRIPTION, c.getString(c.getColumnIndex(FilmTableHelper.DESCRIPTION)));
                values.put(FilmTableHelper.IMGCARDBOARD, c.getString(c.getColumnIndex(FilmTableHelper.IMGCARDBOARD)));
                values.put(FilmTableHelper.IMGLARGE, c.getString(c.getColumnIndex(FilmTableHelper.IMGLARGE)));
                if (frag == StaticValues.FRAGMENT_HOME) {
                    values.put(FilmTableHelper.WATCH, StaticValues.WATCH_TRUE);
                } else {
                    values.put(FilmTableHelper.WATCH, StaticValues.WATCH_FALSE);
                }
                getActivity().getContentResolver().update(FilmProvider.FILMS_URI, values, FilmTableHelper._ID + " = " + c.getString(c.getColumnIndex(FilmTableHelper._ID)), null);
                if (frag == StaticValues.FRAGMENT_HOME) {
                    Toast.makeText(getActivity(), "Aggiunto ai film da guardare", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Rimosso dai film da guardare", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.setNegativeButton(getText(R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return dialog.create();
    }
}
