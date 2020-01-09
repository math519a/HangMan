package com.example.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        final EditText editText = view.findViewById(R.id.enterName);

        Button startGameButton = (Button) view.findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString() == null || editText.getText().length()<5 ){
                    editText.setHint("Name should not be null or <5");
                    return;
                }
                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra("name",editText.getText().toString());

                startActivity(intent);
            }
        });
        return view;
    }
}
