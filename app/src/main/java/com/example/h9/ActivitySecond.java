package com.example.h9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivitySecond extends MainActivity {
    RecyclerView recyclerView;
    TextView NameOfTheMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent i = getIntent();
        ArrayList<String> movies = i.getStringArrayListExtra("movies");
        for (String m : movies){
            System.out.println(m);
        }
        String movieName = i.getStringExtra("movieName");
        NameOfTheMovie = (TextView) findViewById(R.id.moviename);
        if (movieName.isEmpty()){
            NameOfTheMovie.setText("List of movies: ");
        }else{
            NameOfTheMovie.setText(movieName);
        }
        System.out.println("Activity Second: " + movieName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        MovieAdapter myAdapter = new MovieAdapter(movies);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));


    }
}