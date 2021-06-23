package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.exercises.AnimalSounds;
import com.example.coachlea.exercises.ImageRecognition;
import com.example.coachlea.exercises.MinimalPairs;
import com.example.coachlea.R;
import com.example.coachlea.exercises.SnailRace;

public class ExercisesMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_main);
    }

    public void minimalPairsBTNClick(View view){
        Intent intent = new Intent(this, MinimalPairs.class);
        startActivity(intent);
    }

    public void imageRecognitionBTNClick(View view){
        Intent intent = new Intent(this, ImageRecognition.class);
        startActivity(intent);

    }

    public void snailRaceBTNClick(View view){
        Intent intent = new Intent(this, SnailRace.class);
        startActivity(intent);
        //TODO
    }


    public void animalSoundsBTNClick(View view){
        Intent intent = new Intent(this, AnimalSounds.class);
        startActivity(intent);
    }
}
