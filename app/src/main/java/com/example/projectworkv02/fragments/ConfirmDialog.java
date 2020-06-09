package com.example.projectworkv02.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projectworkv02.R;

//fragment che crea un dialog per chiedere conferma o annulla di operazioni
public class ConfirmDialog extends DialogFragment {

    private long id;
    private int message;
    private ConfirmDialogListener listener;

    public ConfirmDialog() {
        // Required empty public constructor
    }

    public ConfirmDialog(long id, int message, ConfirmDialogListener listener) {
        this.id = id;
        this.message = message;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // costruzione di un alert dialog che in caso di risposta positiva restituisce l'id del film.
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(message) + "?");
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
