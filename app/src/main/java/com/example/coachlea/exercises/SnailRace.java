package com.example.coachlea.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.other_activities.MainActivity;

public class SnailRace extends AppCompatActivity {

    //all the stuff happens in SnailRaceGame.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snail_race);
    }

    protected void onPause(){
        super.onPause();
    }

    public void homeBTNClick(View view){

        this.onPause();
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }
}