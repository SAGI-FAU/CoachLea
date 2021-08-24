package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.exercises.AnimalSounds;
import com.example.coachlea.exercises.ImageRecognition;
import com.example.coachlea.exercises.MinimalPairs;

import java.util.Objects;
import java.util.Random;

public class SpeakingExerciseFinished extends AppCompatActivity {

    private TextView motivation;
    private ImageButton againBTN;
    private ImageButton backBTN;
    private ImageButton homeBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaking_exercise_finished);

        //initialize
        motivation = findViewById(R.id.motivationTXT);
        againBTN = findViewById(R.id.againBTN1);
        backBTN = findViewById(R.id.backBTN1);
        homeBTN = findViewById(R.id.homeBTN1);

        //set motivational text
        String[] motivations = getResources().getStringArray(R.array.motivations);
        Random r = new Random();
        motivation = findViewById(R.id.motivationTXT);
        motivation.setText(motivations[r.nextInt(motivations.length)]);

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ExercisesMain.class);
                v.getContext().startActivity(intent);
            }
        });

        againBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent ;
                switch (Objects.requireNonNull(getIntent().getExtras().getString("exercise"))){
                    case "MinimalPairs":
                        intent = new Intent(v.getContext(), MinimalPairs.class);
                        break;
                    case "AnimalSounds":
                        intent = new Intent(v.getContext(), AnimalSounds.class);
                        break;
                    case "ImageRecognition":
                        intent = new Intent(v.getContext(), ImageRecognition.class);
                        break;
                    default:
                        intent = new Intent(v.getContext(), MainActivity.class);
                        break;

                }
                v.getContext().startActivity(intent);
            }
        });
    }


}