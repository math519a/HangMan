package com.example.hangman.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.hangman.Models.Highscore;
import com.example.hangman.Models.Word;

import java.util.ArrayList;
import java.util.List;

public class DbContext extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Hangman.db";

    public DbContext(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Words (id INTEGER PRIMARY KEY AUTOINCREMENT, word TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Highscores (word_id INTEGER PRIMARY KEY, name TEXT, tries INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Words");
        db.execSQL("DROP TABLE IF EXISTS Highscores");
        onCreate(db);
    }


    public boolean insertWords(ArrayList<String> words){
        SQLiteDatabase _db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(String word : words){
            values.put("word", word);
            _db.insert("Words",null, values);
        }
        return true;
    }

    public boolean insertHighscore(Highscore highscore){
        SQLiteDatabase _db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word_id", highscore.getWordId());
        values.put("name", highscore.getName());
        values.put("tries", highscore.getTries());

        long result = _db.insert("Highscores",null, values);
        if (result == -1){
            return false;
        } else {
            return true;
        }

    }

    public int removeHighscore(String word){
        SQLiteDatabase _db = this.getWritableDatabase();
        Cursor response = _db.rawQuery("SELECT id FROM Words WHERE word="+word, null);
        int iid = response.getColumnIndex("id");
        int id = response.getInt(iid);
        return _db.delete("Highscores","ID = ?", new String[]{Integer.toString(id)});

    }

    public ArrayList<Word> getWords(){
        SQLiteDatabase _db = this.getWritableDatabase();
        Cursor response = _db.rawQuery("SELECT * FROM Words ORDER BY word", null);

        int iid = response.getColumnIndex("id");
        int iword = response.getColumnIndex("word");

        ArrayList<Word> words = new ArrayList<>();
        for (response.moveToFirst(); !response.isAfterLast(); response.moveToNext()){
            Word word = new Word(response.getInt(iid),response.getString(iword));
            words.add(word);
        }
        return words;
    }

    public ArrayList<Highscore> getHighscores(){
        SQLiteDatabase _db = this.getWritableDatabase();
        Cursor response = _db.rawQuery("SELECT Words.id,Words.word,Highscores.name,Highscores.tries FROM Words INNER JOIN Highscores ON Words.id=Highscores.word_id ORDER BY word", null);

        int iid = response.getColumnIndex("id");
        int iword = response.getColumnIndex("word");
        int iname = response.getColumnIndex("name");
        int itries = response.getColumnIndex("tries");

        ArrayList<Highscore> highscores = new ArrayList<>();
        for (response.moveToFirst(); !response.isAfterLast(); response.moveToNext()){
            Highscore highscore = new Highscore(new Word(response.getInt(iid), response.getString(iword)),response.getString(iname),response.getInt(itries));
            highscores.add(highscore);
        }
        return highscores;
    }

}
