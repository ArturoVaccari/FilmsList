package com.example.projectworkv02;

import android.os.Bundle;

import com.example.projectworkv02.database.Film;
import com.example.projectworkv02.database.FilmDB;
import com.example.projectworkv02.internet.InternetCalls;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Film> listFilms;
    private InternetCalls internetCalls = new InternetCalls();
    public static FilmDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        db = new FilmDB(this);

        listFilms = new ArrayList<>();
        internetCalls.chiamataInternet(Strings.FILM, Strings.UPCOMING, Strings.ITALIAN, this);


    }

}
