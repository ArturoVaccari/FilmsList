package com.example.projectworkv02;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.projectworkv02.internet.InternetCalls;
import com.example.projectworkv02.ui.searchFilms.SearchActivity;
import com.example.projectworkv02.utility.StaticValues;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Toolbar actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        NavigationUI.setupWithNavController(navView, navController);

        askFilms();
    }

    public void askFilms() {
        InternetCalls i = new InternetCalls();
        i.chiamataInternet(StaticValues.FILM, StaticValues.POPULAR, StaticValues.ITALIAN, StaticValues.page, StaticValues.REGION_ITALIAN, getApplicationContext(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.search_icon);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            }
        });


       return true;
    }
}