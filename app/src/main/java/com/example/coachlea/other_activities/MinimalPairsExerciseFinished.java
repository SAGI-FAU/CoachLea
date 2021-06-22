package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;

public class MinimalPairsExerciseFinished extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimal_pairs_exercise_finished);

        TextView message = findViewById(R.id.message);
        TextView result = findViewById(R.id.result);
        Button home = findViewById(R.id.home2);

        int[] results = getIntent().getIntArrayExtra("results");

        int score = 0;
        for(int i = 0; i < results.length; i++){
            if(results[i]== 1){
                score++;
            }
        }
        message.setText("Well done!");
        result.setText(score + " " + "/ 4");


        home.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                Intent intent = new Intent(MinimalPairsExerciseFinished.this, MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}