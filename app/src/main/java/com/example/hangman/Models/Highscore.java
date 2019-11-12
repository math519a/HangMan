package com.example.hangman.Models;

public class Highscore {
    private String word;
    private int tries;

    public Highscore(String word, int tries) {
        this.word = word;
        this.tries = tries;
    }

    public String getWord() {
        return word;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries){
        this.tries = tries;
    }
}
