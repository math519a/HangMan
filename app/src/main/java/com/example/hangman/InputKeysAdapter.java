package com.example.hangman;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class InputKeysAdapter extends BaseAdapter {
    private String[] letters;
    private LayoutInflater layoutInflater;

    public InputKeysAdapter(Context context){
        letters = new String[26];
        for(int i = 0; i<letters.length; i++){
            letters[i] = "" + (char)(i+'A');
        }
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return letters.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button letterButton;
        if(convertView == null){
            letterButton = (Button)layoutInflater.inflate(R.layout.letter_button, parent, false);
        } else {
            letterButton = (Button)convertView;
        }
        letterButton.setText(letters[position]);
        return letterButton;
    }
}
