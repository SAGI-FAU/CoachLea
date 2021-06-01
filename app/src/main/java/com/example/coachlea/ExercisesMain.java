package com.example.coachlea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.Exercises.MinimalPairs;

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
}
