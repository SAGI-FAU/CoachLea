package com.example.coachlea.other_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.coachlea.R;
import com.example.coachlea.exercises.AnimalSounds;
import com.example.coachlea.exercises.ImageRecognition;
import com.example.coachlea.exercises.MinimalPairs;

import java.util.Objects;
import java.util.Random;

public class TrainingsetExerciseFinished extends AppCompatActivity {

    private TextView textMotivation;
    private TextView nextExercise;
    private TextView nextTaskIntroduction;
    private ImageButton done, again;
    private CardView cardView;
    private int exerciseCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingset_exercise_finished);
        getSupportActionBar().setTitle(getResources().getString(R.string.trainingsetTitle)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //initialize
        Intent intent;
        Random random = new Random();
        textMotivation = findViewById(R.id.textMotivation);
        nextExercise = findViewById(R.id.nextExercise);
        nextTaskIntroduction = findViewById(R.id.nextTaskIntroduction);
        done = findViewById(R.id.next_trainingsetExercise);
        again = findViewById(R.id.again_trainingsetExercise);
        cardView = findViewById(R.id.set_cardview);

        String[] motivation = getResources().getStringArray(R.array.motivations);
        textMotivation.setText(motivation[random.nextInt(motivation.length)]);

        if (getIntent().getExtras() != null){
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0) + 1;
        }

        if(exerciseCounter >= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getStringArray("exerciseList")).length) {
            nextTaskIntroduction.setText(R.string.TrainingsetLastExerciseFinished);
            nextExercise.setText("");
            nextExercise.setVisibility(View.GONE);
            cardView.setVisibility(View.GONE);
            done.setForeground(getResources().getDrawable(R.drawable.ic_home));
        } else {
            nextExercise.setText(getIntent().getExtras().getStringArray("exerciseList")[exerciseCounter]);
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;

                if(exerciseCounter >= Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getStringArray("exerciseList")).length) {
                    intent = new Intent(v.getContext(),MainActivity.class);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("SessionFinished", true);
                    editor.apply();
                    v.getContext().startActivity(intent);
                } else {
                    switch (Objects.requireNonNull(getIntent().getExtras().getStringArray("exerciseList"))[exerciseCounter]){
                        case "Minimalpaare":
                            intent = new Intent(v.getContext(), MinimalPairs.class);
                            intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Tierlaute":
                            intent = new Intent(v.getContext(), AnimalSounds.class);
                            intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Bilder erkennen":
                            intent = new Intent(v.getContext(), ImageRecognition.class);
                            intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        case "Schneckenrennen":
                            intent = new Intent(v.getContext(), SnailRaceStart.class);
                            intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset", true);
                            break;
                        default:
                            intent = new Intent(v.getContext(),MainActivity.class);
                            break;
                    }
                    v.getContext().startActivity(intent);
                }
            }


        });

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                exerciseCounter -= 1;
                switch (Objects.requireNonNull(getIntent().getExtras().getStringArray("exerciseList"))[exerciseCounter]){
                    case "Minimalpaare":
                        intent = new Intent(v.getContext(), MinimalPairs.class);
                        intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                        intent.putExtra("exerciseCounter", exerciseCounter);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Tierlaute":
                        intent = new Intent(v.getContext(), AnimalSounds.class);
                        intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                        intent.putExtra("exerciseCounter", exerciseCounter);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Bilder erkennen":
                        intent = new Intent(v.getContext(), ImageRecognition.class);
                        intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                        intent.putExtra("exerciseCounter", exerciseCounter);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Schneckenrennen":
                        intent = new Intent(v.getContext(), SnailRaceStart.class);
                        intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                        intent.putExtra("exerciseCounter", exerciseCounter);
                        intent.putExtra("trainingset", true);
                        break;
                    default:
                        intent = new Intent(v.getContext(),MainActivity.class);
                        break;
                }
                v.getContext().startActivity(intent);

            }
        });
    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}