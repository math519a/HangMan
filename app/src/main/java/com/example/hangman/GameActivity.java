package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] words;
    private Random randomNumber = new Random();
    private String currentWord;
    private String correctWord;
    private LinearLayout wordLayout;
    private TextView[] characterViews;
    private GridView letters;
    private InputKeysAdapter lettersAdapter;

    private ImageView[] hangman;
    private int numberPart = 6;
    private int currentPart;
    private int numberCharacters;
    private int numberCorrect;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* Tell the activity to fetch resources */
        Resources resources = getResources();
        /* Initialize the words markup array into memory array */
        words = resources.getStringArray(R.array.words);
        /* Get the word Layout to put new Views into */
        wordLayout = findViewById(R.id.word);

        letters = findViewById(R.id.input);
        hangman = new ImageView[numberPart];
        hangman[0] = findViewById(R.id.head);
        hangman[1] = findViewById(R.id.body);
        hangman[2] = findViewById(R.id.arm_left);
        hangman[3] = findViewById(R.id.arm_right);
        hangman[4] = findViewById(R.id.leg_left);
        hangman[5] = findViewById(R.id.leg_right);

        play();
    }

    private void play(){
        /* new random word from string array resource
        * random number generated bound by the size of the resource array */
        currentWord = words[randomNumber.nextInt(words.length)];

        /* TextView array is used to store individual characters of the current word
        * and enables manipulation of the alpha attribute etc. on a correct guess later */
        characterViews = new TextView[currentWord.length()];

        /* Splitting up the word into single-character TextViews */
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
        lettersAdapter = new InputKeysAdapter(this);
        letters.setAdapter(lettersAdapter);

        currentPart = 0;
        numberCharacters = currentWord.length();
        numberCorrect = 0;

        for (ImageView iv : hangman){
            iv.setVisibility(View.INVISIBLE);
        }

    }
}
