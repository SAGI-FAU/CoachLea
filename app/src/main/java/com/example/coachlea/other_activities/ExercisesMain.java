package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.Exercises.AnimalSounds;
import com.example.coachlea.Exercises.MinimalPairs;
import com.example.coachlea.R;

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

    public void minimalPairs2BTNClick(View view){
        //TODO
    }

    public void sustainedPhonationBTNClick(View view){
        //TODO
    }

    public void vowelSpaceBTNClick(View view){
        //TODO
    }

    public void animalSoundsBTNClick(View view){
        Intent intent = new Intent(this, AnimalSounds.class);
        startActivity(intent);
    }
}
