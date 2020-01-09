package com.example.hangman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hangman.DAL.DbContext;
import com.example.hangman.Models.Highscore;
import com.example.hangman.Models.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    DbContext dbHelper = new DbContext(this);

    private ArrayList<Word> words;
    private Random randomNumber = new Random();
    private Word currentWord;
    private LinearLayout wordLayout;
    private TextView[] characterViews;
    private GridView letters;
    private InputKeysAdapter lettersAdapter;

    private ImageView[] hangman;
    private int numberPart = 6;
    private int currentPart;
    private int numberCharacters;
    private int numberCorrect;

    private String name;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* Tell the activity to fetch resources */
        Resources resources = getResources();
        /* Initialize the words markup array into memory array */
        words = dbHelper.getWords();
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

        name = getIntent().getExtras().getString("name");

        play();
    }

    private void play(){
        /* new random word from string array resource
        * random number generated bound by the size of the resource array */
        currentWord = words.get(randomNumber.nextInt(words.size()));

        /* TextView array is used to store individual characters of the current word
        * and enables manipulation of the alpha attribute etc. on a correct guess later */
        characterViews = new TextView[currentWord.getWord().length()];

        /* Remove current word view to prepare for new game
        * This was missing in Galgeleg 1 */
        wordLayout.removeAllViews();

        /* Splitting up the word into single-character TextViews */
        for (int i = 0; i<currentWord.getWord().length(); i++){
            TextView view = new TextView(this);
            view.setText(""+currentWord.getWord().charAt(i));
            view.setGravity(Gravity.CENTER);
            view.setTextSize(46);
            view.setWidth(140);
            view.setBackgroundResource(R.drawable.back);


            /* TODO Udkommenter den her linje får at gøre ordet der skal gættes synligt, så det ikke
            *   er svært at teste funktionalitet*/
            //view.setTextColor(Color.parseColor("#FAFAFA"));


            characterViews[i] = view;
            wordLayout.addView(view);
        }

        /* Lets an adapter handle the generation of letter input keys*/
        lettersAdapter = new InputKeysAdapter(this);
        letters.setAdapter(lettersAdapter);

        /* Just before the game starts, initialize variables and hide every hangman part */
        currentPart = 0;
        numberCharacters = currentWord.getWord().length();
        numberCorrect = 0;
        for (ImageView iv : hangman){
            iv.setVisibility(View.INVISIBLE);
        }

    }

    public void letterButtonPressed(View view){
        /* Have to cast to TextView to get .getText() method */
        char letter = ((TextView)view).getText().toString().charAt(0);

        /* When a button is pressed, you should no longer be able to press it */
        view.setEnabled(false);
        /* Change alpha to make it clear which buttons can still be pressed */
        view.setAlpha((float)0.2);


        /* Check if the letter pressed is in the current word */
        boolean correct = false;
        for (int i = 0; i<currentWord.getWord().length(); i++){
            if (currentWord.getWord().charAt(i) == letter){
                correct = true;
                numberCorrect++;
                /* if the letter indeed is in the word, change the letter's color back to black */
                characterViews[i].setTextColor(Color.BLACK);
            }
        }

        if (correct){
        /* Do nothing */

        } else if (currentPart < numberPart){
            /* While the current part hanged is less than the total number of parts,
            hang and go prepare next part to be hanged */
            hangman[currentPart].setVisibility(View.VISIBLE);
            currentPart++;
        } else {
            /* User has lost, start off by disabling all button to prevent the game to continue */
            disableAllButton();

            /* Build a new dialog window to assert that the user has lost, and ask if the user wants
            to play again. ***** NOW CHANGED TO NEW FRAGMENT INTENT *******
             */
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("messageLostWon", "You Lost\nThe word was "+currentWord+"\n"+numberCorrect+currentPart+" tries");
            startActivity(intent);

        }

        if (numberCorrect == numberCharacters){
            /* User has won */
            disableAllButton();

            /* Check if new highscore */
            ArrayList<Highscore> highscores = dbHelper.getHighscores();

            /* To avoid this taxing procedure, should probably use HashMap, but the use of HashMap
            * would cause problems in other parts of the app */
            boolean foundWord = false;
            for (int i = 0; i<highscores.size(); i++){
                Highscore hs = highscores.get(i);
                if (hs.getWord() == currentWord.getWord()){
                    foundWord = true;
                    if (hs.getTries() > numberCorrect+currentPart){
                        dbHelper.removeHighscore(currentWord.getWord());
                        dbHelper.insertHighscore(new Highscore(currentWord, name,numberCorrect+currentPart));
                        Log.i("GameActivity: ", "Saved Highscore["+currentWord+","+name+","+numberCorrect+currentPart+"] to Db.");
                    }
                    break;
                }
            }
            if (foundWord == false){
                dbHelper.insertHighscore(new Highscore(currentWord, name,numberCorrect+currentPart));
                Log.i("GameActivity: ", "Saved Highscore["+currentWord+","+name+","+numberCorrect+currentPart+"] to Db.");
            }


            /* Build a new dialog window to assert that the user has won, and ask if the user wants
            to play again. ***** NOW CHANGED TO NEW FRAGMENT INTENT *******
             */

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("showLostWon", true);
            intent.putExtra("messageLostWon", "You Won!\nThe word was "+currentWord.getWord()+"\n"+(numberCorrect+currentPart)+" tries");
            startActivity(intent);
        }

    }

    public void disableAllButton(){
        for(int i = 0; i<letters.getChildCount(); i++){
            letters.getChildAt(i).setEnabled(false);
        }

    }
}
