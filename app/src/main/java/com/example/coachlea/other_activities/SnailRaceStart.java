package com.example.coachlea.other_activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.exercises.SnailRace;

import java.util.Random;

public class SnailRaceStart extends AppCompatActivity {

    private Dialog explanationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snail_race_start);
        getSupportActionBar().setTitle(R.string.Snail_race);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        explanationDialog = new Dialog(this);
        ImageButton start = findViewById(R.id.playBTNsnailRace);
        TextView v = findViewById(R.id.textVowel);
        String vowel;
        String[] vowels= new String[]{"A","E","I","O","U"};

        //check if SnailRace is used for the first time
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        int login = prefs.getInt("SnailRaceUsed", 0);

        if(login == 0){
            openPopup();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("SnailRaceUsed",13);
            editor.apply();
        }

        //set vowel
        Random rand = new Random();
        vowel=vowels[rand.nextInt(vowels.length)];

        v.setText(vowel);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SnailRaceStart.this, SnailRace.class);
                intent.putExtra("Vowel",vowel);
                v.getContext().startActivity(intent);
            }
        });
    }

    //explanation menu
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.explanation_menu), menu);
        return true;

    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.explanation_menu){
           openPopup();
        } else {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openPopup(){
        TextView close;
        TextView title_e;
        TextView explanation;
        ImageButton explanation_mp3;

        explanationDialog.setContentView(R.layout.popup_explanation);


        title_e = (TextView) explanationDialog.findViewById(R.id.explanation_title);
        explanation = (TextView)  explanationDialog.findViewById(R.id.text_explanation);
        explanation_mp3 = (ImageButton)  explanationDialog.findViewById(R.id.play_explanation);
        close = (TextView)  explanationDialog.findViewById(R.id.txtclose);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explanationDialog.dismiss();
            }
        });

        title_e.setText(R.string.Snail_race);
        explanation.setText(R.string.snailrace_explanation);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(explanationDialog.getWindow().getAttributes());
        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        explanationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        explanationDialog.getWindow().setAttributes(layoutParams);
        explanationDialog.show();
    }
}