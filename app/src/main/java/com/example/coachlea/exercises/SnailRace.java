package com.example.coachlea.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.other_activities.MainActivity;
import com.example.coachlea.tools.SnailRaceGame;

public class SnailRace extends AppCompatActivity {

    private SnailRaceGame snailRaceGame;

    //all the stuff happens in SnailRaceGame.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snail_race);

        snailRaceGame = findViewById(R.id.snailRaceGame);
    }

    protected void onPause(){
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        snailRaceGame.destroyThread();
        super.onPause();
    }

    public void onBackPressed(){
        this.onPause();
        super.onBackPressed();
    }

    public void homeBTNClick(View view){

        onPause();
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }


}