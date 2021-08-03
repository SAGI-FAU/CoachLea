package com.example.coachlea.other_activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.exercises.AnimalSounds;
import com.example.coachlea.exercises.ImageRecognition;
import com.example.coachlea.exercises.MinimalPairs;

//TODO change dialog into popup inflater?
public class ExercisesMain extends AppCompatActivity {
    Dialog explanationDialog;
    //LayoutInflater inflater;
    private Button explanationSnailRace;
    private Button explanationMinimalPairs;
    private Button explanationAnimalSounds;
    private Button explanationImageRecognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_main);
        getSupportActionBar().setTitle(R.string.exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        explanationDialog = new Dialog(this);
        //inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        explanationSnailRace = findViewById(R.id.explanationSnailRace);
        explanationMinimalPairs = findViewById(R.id.explanationMinimalPairs);
        explanationAnimalSounds = findViewById(R.id.explanationAnimalSounds);
        explanationImageRecognition = findViewById(R.id.explanationImageRecognition);

        //call explanation popup
        explanationSnailRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, getResources().getString(R.string.Snail_race));
            }
        });

        explanationMinimalPairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,getResources().getString(R.string.minimal_pairs));
            }
        });

        explanationAnimalSounds.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showPopup(v,getResources().getString(R.string.animal_sounds));
            }
        });

        explanationImageRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,getResources().getString(R.string.image_recognition));
            }
        });
    }

    public void minimalPairsBTNClick(View view){
        Intent intent = new Intent(this, MinimalPairs.class);
        startActivity(intent);
    }

    public void imageRecognitionBTNClick(View view){
        Intent intent = new Intent(this, ImageRecognition.class);
        startActivity(intent);

    }

    public void snailRaceBTNClick(View view){
        Intent intent = new Intent(this, SnailRaceStart.class);
        startActivity(intent);
        //TODO
    }


    public void animalSoundsBTNClick(View view){
        Intent intent = new Intent(this, AnimalSounds.class);
        startActivity(intent);
    }

    public void showPopup(View view, String title){
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
                //popupWindow.dismiss();
            }
        });

        switch(title){
            case "Minimalpaare":
                //TODO
                title_e.setText(R.string.minimal_pairs);
                explanation.setText(R.string.minimalPairs_explanation);
                break;
            case "Bilder erkennen":
                //TODO
                title_e.setText(R.string.image_recognition);
                explanation.setText(R.string.imageRecognition_explanation);
                break;
            case "Schneckenrennen":
                title_e.setText(R.string.Snail_race);
                explanation.setText(R.string.snailrace_explanation);
                break;
            case "Tierlaute":
                //TODO
                explanation.setText(R.string.animalSounds_explanation);
                title_e.setText(R.string.animal_sounds);
                break;
            default:
                break;

        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(explanationDialog.getWindow().getAttributes());
        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        explanationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        explanationDialog.getWindow().setAttributes(layoutParams);
        explanationDialog.show();


    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
