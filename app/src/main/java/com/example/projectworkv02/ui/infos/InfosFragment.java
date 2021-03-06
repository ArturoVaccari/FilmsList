package com.example.projectworkv02.ui.infos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projectworkv02.R;

// fragment contenente le informazioni dello sviluppatore, le istruzioni e i link per le icone usate nell'app
public class InfosFragment extends Fragment {

    public InfosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.title_infos));

        TextView freepik = getActivity().findViewById(R.id.freepikLink);
        TextView flaticon1 = getActivity().findViewById(R.id.flaticonLink1);
        TextView google = getActivity().findViewById(R.id.googleLink);
        TextView flaticon2 = getActivity().findViewById(R.id.flaticonLink2);
        TextView eyeCheckedIcon = getActivity().findViewById(R.id.eyeCheckedIconLink);
        TextView icons8 = getActivity().findViewById(R.id.icons8Link);
        TextView bqlqn = getActivity().findViewById(R.id.bqlqnLink);
        TextView flaticon3 = getActivity().findViewById(R.id.flaticonLink3);
        TextView freepik2 = getActivity().findViewById(R.id.freepikLink2);
        TextView flaticon4 = getActivity().findViewById(R.id.flaticonLink4);

        freepik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/authors/freepik";
                openWebPage(s);
            }
        });

        flaticon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/";
                openWebPage(s);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/authors/google";
                openWebPage(s);
            }
        });

        flaticon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/";
                openWebPage(s);
            }
        });

        eyeCheckedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://icons8.com/icons/set/eye-checked";
                openWebPage(s);
            }
        });

        icons8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://icons8.com";
                openWebPage(s);
            }
        });

        bqlqn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/authors/bqlqn";
                openWebPage(s);
            }
        });

        flaticon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/";
                openWebPage(s);
            }
        });

        freepik2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/authors/freepik";
                openWebPage(s);
            }
        });

        flaticon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "https://www.flaticon.com/";
                openWebPage(s);
            }
        });

    }

    // metodo per aprire una pagina internet con il link richiesti dagli autori delle icone
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
