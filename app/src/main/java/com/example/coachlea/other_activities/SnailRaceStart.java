package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.exercises.SnailRace;

import java.util.Random;

public class SnailRaceStart extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snail_race_start);

        //initialize
        ImageButton start = findViewById(R.id.playBTNsnailRace);
        TextView v = findViewById(R.id.textVowel);
        String vowel;
        String[] vowels= new String[]{"A","E","I","O","U"};

        //set vowel
        Random rand = new Random();
        vowel=vowels[rand.nextInt(vowels.length)];

        v.setText(vowel);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SnailRaceStart.this, SnailRace.class);
                intent.putExtra("Vowel",vowel);
                v.getContext().startActivity(intent);
            }
        });
    }
}