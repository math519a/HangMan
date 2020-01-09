package com.example.hangman.Models;

public class Highscore {
    private Word word;
    private String name;
    private int tries;


    public Highscore(Word word, String name, int tries) {
        this.word = word;
        this.name = name;
        this.tries = tries;
    }

    public String getWord() {
        return word.getWord();
    }

    public int getWordId() {
        return word.getId();
    }

    public String getName(){
        return name;
    }

    public int getTries() {
        return tries;
    }


    public void setTries(int tries){
        this.tries = tries;
    }
}
