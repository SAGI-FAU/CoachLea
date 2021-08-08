package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.exercises.AnimalSounds;

import java.util.Random;

public class SpeakingExerciseFinished extends AppCompatActivity {

    private TextView motivation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_exercise_finished);

        String[] motivations = getResources().getStringArray(R.array.motivations);
        Random r = new Random();
        motivation = findViewById(R.id.motivationTXT);
        motivation.setText(motivations[r.nextInt(motivations.length)]);
    }

    public void DoneBTNClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void AgainBTNClick(View view){
        Intent intent = new Intent(this, AnimalSounds.class);
        startActivity(intent);
    }
}