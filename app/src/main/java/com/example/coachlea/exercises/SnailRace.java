package com.example.coachlea.exercises;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.other_activities.ExercisesMain;
import com.example.coachlea.other_activities.MainActivity;
import com.example.coachlea.other_activities.SnailRaceStart;
import com.example.coachlea.other_activities.TrainingsetExerciseFinished;
import com.example.coachlea.tools.SnailRaceGame;

public class SnailRace extends AppCompatActivity {

    private SnailRaceGame snailRaceGame;
    private int exerciseCounter;

    //all the stuff happens in SnailRaceGame.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //make game fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.snail_race);

        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);

        ImageButton againBTN = findViewById(R.id.againBTN2);
        ImageButton homeBTN = findViewById(R.id.homeBTN);
        ImageButton backBTN = findViewById(R.id.backBTN);
        ImageButton nextBTN = findViewById(R.id.snailRace_nextBTN);
        TextView countdown = findViewById(R.id.countdown);

        //make Buttons invisible, they will appear when the game is over
        againBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);
        backBTN.setVisibility(View.GONE);
        nextBTN.setVisibility(View.GONE);


        snailRaceGame = findViewById(R.id.snailRaceGame);
        snailRaceGame.setButtons(homeBTN,againBTN,backBTN,nextBTN,countdown);

        //get and set vowel
        String vowel = (String) getIntent().getExtras().get("Vowel");
        snailRaceGame.setVowel(vowel);

        if (getIntent().getBooleanExtra("trainingset", false)) {
            snailRaceGame.setDailySession(true);
            homeBTN.setForeground(getResources().getDrawable(R.drawable.ic_next));
        } else {
            snailRaceGame.setDailySession(false);
        }

        homeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                Intent intent = new Intent(SnailRace.this, MainActivity.class);
                startActivity(intent);
            }
        });

        againBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                Intent intent = new Intent(SnailRace.this, SnailRaceStart.class);
                startActivity(intent);
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onPause();
                Intent intent = new Intent(SnailRace.this, ExercisesMain.class);
                startActivity(intent);
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                Intent intent = new Intent(SnailRace.this, TrainingsetExerciseFinished.class);
                intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                intent.putExtra("exerciseCounter", exerciseCounter);
                startActivity(intent);
            }
        });


    }

    protected void onPause(){
        snailRaceGame.destroyThread();
        super.onPause();
    }


    public void onBackPressed(){
        this.onPause();
        super.onBackPressed();
    }



}