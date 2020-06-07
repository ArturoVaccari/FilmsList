package com.example.projectworkv02.fragments;

public interface ConfirmDialogListener {
    void onPositivePressed(long film_id, int title_resource);
    void onNegativePressed();
}
