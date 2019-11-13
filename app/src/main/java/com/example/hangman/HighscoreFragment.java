package com.example.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hangman.Models.Highscore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HighscoreFragment extends Fragment {

    ArrayList<Highscore> mHighscores;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore, container, false);
        loadHighscore();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.highscoreRecyclerView);
        HighscoreAdapter adapter = new HighscoreAdapter(mHighscores);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }






    private void loadHighscore(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("highscores", null);
        Type type = new TypeToken<ArrayList<Highscore>>(){}.getType();
        mHighscores = gson.fromJson(json, type);

        if (mHighscores == null){
            mHighscores = new ArrayList<>();
        }
    }

    private void saveHighscores(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mHighscores);
        editor.putString("highscores", json);
        editor.apply();
    }
}
