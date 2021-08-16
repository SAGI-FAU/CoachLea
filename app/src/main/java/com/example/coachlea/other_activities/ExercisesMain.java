package com.example.coachlea.other_activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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


public class ExercisesMain extends AppCompatActivity {
    Dialog explanationDialog;
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
                showPopup( getResources().getString(R.string.Snail_race));
            }
        });

        explanationMinimalPairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(getResources().getString(R.string.minimal_pairs));
            }
        });

        explanationAnimalSounds.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showPopup(getResources().getString(R.string.animal_sounds));
            }
        });

        explanationImageRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(getResources().getString(R.string.image_recognition));
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
    }


    public void animalSoundsBTNClick(View view){
        Intent intent = new Intent(this, AnimalSounds.class);
        startActivity(intent);
    }

    public void showPopup( String title){
        TextView close;
        TextView title_e;
        TextView explanation;
        ImageButton explanation_mp3;
        String mp3_file = null;
        final MediaPlayer[] player = {null};
        int resId;
        String path;

        explanationDialog.setContentView(R.layout.popup_explanation);


        title_e = (TextView) explanationDialog.findViewById(R.id.explanation_title);
        explanation = (TextView)  explanationDialog.findViewById(R.id.text_explanation);
        explanation_mp3 = (ImageButton)  explanationDialog.findViewById(R.id.play_explanation);
        close = (TextView)  explanationDialog.findViewById(R.id.txtclose);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player[0]!= null){
                    player[0].stop();
                    player[0].release();
                    player[0] = null;
                }
                explanationDialog.dismiss();
            }
        });

        switch(title){
            case "Minimalpaare":

                title_e.setText(R.string.minimal_pairs);
                explanation.setText(R.string.minimalPairs_explanation);
                resId = getResources().getIdentifier("minimal_pairs_explanation", "raw", getPackageName());
                path = "a" + resId;
                mp3_file  = path.substring(1);
                break;
            case "Bilder erkennen":

                title_e.setText(R.string.image_recognition);
                explanation.setText(R.string.imageRecognition_explanation);
                resId = getResources().getIdentifier("image_recognition_explanation", "raw", getPackageName());
                path = "a" + resId;
                mp3_file  = path.substring(1);
                break;
            case "Schneckenrennen":
                title_e.setText(R.string.Snail_race);
                explanation.setText(R.string.snailrace_explanation);
                resId = getResources().getIdentifier("snail_race_explanation", "raw", getPackageName());
                path = "a" + resId;
                mp3_file  = path.substring(1);
                break;
            case "Tierlaute":
                explanation.setText(R.string.animalSounds_explanation);
                title_e.setText(R.string.animal_sounds);
                resId = getResources().getIdentifier("animal_sounds_explanation", "raw", getPackageName());
                path = "a" + resId;
                mp3_file  = path.substring(1);
                break;
            default:
                break;

        }

        String finalMp3_file = mp3_file;
        explanation_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player[0] != null) {
                    player[0].seekTo(0);
                    player[0].start();
                } else {
                    player[0] = MediaPlayer.create(v.getContext(), Integer.parseInt(finalMp3_file));
                    player[0].start();
                }
            }
        });

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(explanationDialog.getWindow().getAttributes());
        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        explanationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        explanationDialog.getWindow().setAttributes(layoutParams);
        explanationDialog.setCancelable(false);
        explanationDialog.show();


    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
