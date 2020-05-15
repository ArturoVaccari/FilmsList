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
    private String message;
    private ConfirmDialogListener listener;

    public ConfirmDialog() {
        // Required empty public constructor
    }

    public ConfirmDialog(long id, String message, ConfirmDialogListener listener) {
        this.id = id;
        this.message = message;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Cursor c = getActivity().getContentResolver().query(FilmProvider.FILMS_URI, null, FilmTableHelper._ID + " = " + id, null, null, null);
        c.moveToNext();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(message);
        dialog.setPositiveButton(getText(R.string.confirm).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPositivePressed(id);
            }
        });
        dialog.setNegativeButton(getText(R.string.cancel).toString(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNegativePressed();
            }
        });
        return dialog.create();
    }
}
