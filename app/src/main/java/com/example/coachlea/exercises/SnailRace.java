package com.example.coachlea.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
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

        //make game fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.snail_race);


        ImageButton againBTN = findViewById(R.id.againBTN2);
        ImageButton homeBTN = findViewById(R.id.homeBTN);
        ImageButton backBTN = findViewById(R.id.backBTN);

        //make Buttons invisible, they will appear when the game is over
        againBTN.setVisibility(View.GONE);
        homeBTN.setVisibility(View.GONE);
        backBTN.setVisibility(View.GONE);

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