package com.example.hangman;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hangman.Models.Highscore;

import java.util.ArrayList;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreViewHolder> {

    private ArrayList<Highscore> mHighscores;

    public static class HighscoreViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public TextView mTextView;

        public HighscoreViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.highscoreImageView);
            mTextView = itemView.findViewById(R.id.highscoreTextView);
        }
    }

    public HighscoreAdapter(ArrayList<Highscore> highscores) {
        mHighscores = highscores;
    }

    @NonNull
    @Override
    public HighscoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_highscore, parent, false);
        HighscoreViewHolder viewHolder = new HighscoreViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HighscoreViewHolder holder, int position) {
        Highscore tempItem = mHighscores.get(position);
        holder.mImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
        holder.mTextView.setText(tempItem.getWord()+" guessed in: "+tempItem.getTries()+" tries");
    }

    @Override
    public int getItemCount() {
        return mHighscores.size();
    }




}
