package com.example.projectworkv02.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectworkv02.R;
import com.example.projectworkv02.utility.StaticValues;

public class FilterFragment extends DialogFragment {

    private Button orderRandom, orderVoteDescending, orderVoteAscending, orderDateDescending, orderDateAscending, exit,
            orderNameDescending, orderNameAscending;
    private FilterFragmentListener listener;


    public FilterFragment() {
        // Required empty public constructor
    }

    public FilterFragment(FilterFragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_filter, container, false);

        orderRandom = v.findViewById(R.id.filterRandom);
        orderVoteDescending = v.findViewById(R.id.filterVoteDescending);
        orderVoteAscending = v.findViewById(R.id.filterVoteAscending);
        orderDateDescending = v.findViewById(R.id.filterDateDescending);
        orderDateAscending = v.findViewById(R.id.filterDateAscending);
        orderNameAscending = v.findViewById(R.id.filterNameAscending);
        orderNameDescending = v.findViewById(R.id.filterNameDescending);
        exit = v.findViewById(R.id.exitFilterDialog);

        orderRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_RANDOM);
                dismiss();
            }
        });

        orderVoteDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_VOTE_DESCENDING);
                dismiss();
            }
        });

        orderVoteAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_VOTE_ASCENDING);
                dismiss();
            }
        });

        orderDateDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_DATE_DESCENDING);
                dismiss();
            }
        });

        orderDateAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_DATE_ASCENDING);
                dismiss();
            }
        });

        orderNameAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_NAME_ASCENDING);
                dismiss();
            }
        });

        orderNameDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.orderBy(StaticValues.ORDER_BY_NAME_DESCENDING);
                dismiss();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }
}
