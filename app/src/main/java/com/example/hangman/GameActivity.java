package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] words;
    private Random randomNumber = new Random();
    private String currentWord;
    private String correctWord;
    private LinearLayout wordLayout;
    private TextView[] characterViews;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Resources resources = getResources();
        words = resources.getStringArray(R.array.words);
        wordLayout = findViewById(R.id.word);
        play();
    }

    private void play(){
        /* new random word from string array resource
        * random number generated bound by the size of the resource array */
        currentWord = words[randomNumber.nextInt(words.length)];

        /* TextView array is used to store individual character of the current word
        * and enables manipulation of the alpha etc. on a correct guess later */
        characterViews = new TextView[currentWord.length()];

        for (int i = 0; i<currentWord.length(); i++){
            TextView view = new TextView(this);
            view.setText(""+currentWord.charAt(i));
            view.setGravity(Gravity.CENTER);
            view.setTextSize(36);
            /* had to use alpha instead of colour because default background colour _can_ vary
            * required to hide the word upon new game */
            view.setAlpha(0);
            characterViews[i] = view;
            wordLayout.addView(view);
        }

    }
}
