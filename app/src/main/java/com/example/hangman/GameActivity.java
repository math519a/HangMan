package com.example.hangman;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
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
            view.setTextSize(46);
            view.setWidth(140);
            view.setBackgroundResource(R.drawable.back);
            view.setTextColor(Color.parseColor("#FAFAFA"));
            characterViews[i] = view;
            wordLayout.addView(view);
        }

        /* Lets an adapter handle the generation of letter input keys*/
        lettersAdapter = new InputKeysAdapter(this);
        letters.setAdapter(lettersAdapter);

        /* Just before the game starts, initialize variables and hide every hangman part */
        currentPart = 0;
        numberCharacters = currentWord.length();
        numberCorrect = 0;
        for (ImageView iv : hangman){
            iv.setVisibility(View.INVISIBLE);
        }

    }

    public void letterButtonPressed(View view){
        /* have to cast to TextView to get .getText() method */
        char letter = ((TextView)view).getText().toString().charAt(0);

        /* When a button is pressed, you should no longer be able to press it */
        view.setEnabled(false);
        /* Change alpha to make it clear which buttons can still be pressed */
        view.setAlpha((float)0.2);


        /* Check if the letter pressed is in the current word */
        boolean correct = false;
        for (int i = 0; i<currentWord.length(); i++){
            if (currentWord.charAt(i) == letter){
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
            to play again.
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(":(");
            builder.setTitle("You've lost\nThe answer was "+currentWord);
            builder.setPositiveButton("Play again?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.play();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.finish();
                }
            });
            builder.show();
        }

        if (numberCorrect == numberCharacters){
            /* User has won */
            disableAllButton();

            /* Build a new dialog window to assert that the user has won, and ask if the user wants
            to play again.
             */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Congratulations");
            builder.setTitle("You've won\nThe answer was "+currentWord);
            builder.setPositiveButton("Play again?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.play();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GameActivity.this.finish();
                }
            });
            builder.show();
        }

    }

    public void disableAllButton(){
        for(int i = 0; i<letters.getChildCount(); i++){
            letters.getChildAt(i).setEnabled(false);
        }

    }
}
