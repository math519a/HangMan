package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.hangman.Models.Highscore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;



// ******************* DEPRECATED ******************* //

public class HighscoreActivity extends AppCompatActivity {

    ArrayList<Highscore> mHighscores;

    private RecyclerView mRecyclerView;
    private HighscoreAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        loadHighscore();
        //mHighscores.add(new Highscore("BIRD", 4));
        //saveHighscores();


        mRecyclerView = findViewById(R.id.highscoreRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new HighscoreAdapter(mHighscores);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadHighscore(){
        SharedPreferences preferences = getSharedPreferences("shared", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("highscores", null);
        Type type = new TypeToken<ArrayList<Highscore>>(){}.getType();
        mHighscores = gson.fromJson(json, type);

        if (mHighscores == null){
            mHighscores = new ArrayList<>();
        }
    }

    private void saveHighscores(){
        SharedPreferences preferences = getSharedPreferences("shared", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mHighscores);
        editor.putString("highscores", json);
        editor.apply();
    }
}
