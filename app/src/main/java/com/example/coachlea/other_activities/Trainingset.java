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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.exercises.AnimalSounds;
import com.example.coachlea.exercises.ImageRecognition;
import com.example.coachlea.exercises.MinimalPairs;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class Trainingset extends AppCompatActivity {

    private Dialog explanationDialog;
    private Random rand = new Random();
    private String exercise_list[];
    private String state = "";
    private int choose = 0;
    private boolean sessionFinished;
    private boolean minimal_pairs, animal_sounds, image_recognition, snail_race;
    private TextView setExercise1;
    private TextView setExercise2;
    private TextView setExercise3;
    private ImageButton startTrainingset;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Set<String> session = new HashSet<String>();


        //get date
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.GERMANY);
        Calendar calendar = Calendar.getInstance();
        String weekDay = dayFormat.format(calendar.getTime());

        //shared preferences
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        state = pref.getString("MEM1", "");
        choose = pref.getInt("MEM2", -1);
        sessionFinished = pref.getBoolean("SessionFinished",false);
        SharedPreferences exercise = getApplicationContext().getSharedPreferences("ExerciseFinished", 0);
        SharedPreferences.Editor editor1 = exercise.edit();


        if(!weekDay.equals(state)){

            setContentView(R.layout.trainingset);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_blue)));
            getSupportActionBar().setTitle(getResources().getString(R.string.trainingsetTitle)); // for set actionbar title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //initialize
            explanationDialog = new Dialog(this); //for explanation popup
            setExercise1 = findViewById(R.id.SetExercise1);
            setExercise2 = findViewById(R.id.SetExercise2);
            setExercise3 = findViewById(R.id.SetExercise3);
            startTrainingset = findViewById(R.id.start_trainingset);

            String hearing_exercise=getResources().getString(R.string.minimal_pairs);
            String[] speaking_exercise ={getResources().getString(R.string.animal_sounds),getResources().getString(R.string.image_recognition), getResources().getString(R.string.Snail_race)};

            minimal_pairs = exercise.getBoolean("MinimalPairs", false);
            animal_sounds = exercise.getBoolean("AnimalSounds",false);
            image_recognition = exercise.getBoolean("ImageRecognition",false);
            snail_race = exercise.getBoolean("SnailRace",false);

            choose = rand.nextInt(2);
            editor.putInt("MEM2", choose);
            editor.apply();
            //state = weekDay;
            editor.putString("MEM1", weekDay);
            editor.commit();

            exercise_list = new String[]{hearing_exercise, speaking_exercise[choose], speaking_exercise[(choose + 1) % 3]};
            exercise_list = shuffleArray(exercise_list);
            editor1.putBoolean("Finished", false);
            editor1.apply();

            //set texts
            setExercise1.setText(exercise_list[0]);
            setExercise2.setText(exercise_list[1]);
            setExercise3.setText(exercise_list[2]);
            session.addAll(Arrays.asList(exercise_list));
            editor.putStringSet("ExerciseList", session);
            editor.apply();

        } else if(weekDay.equals(state) && sessionFinished){ //check if daily session already has been done

            setContentView(R.layout.trainingset__finished);
            getSupportActionBar().setTitle(getResources().getString(R.string.trainingsetTitle)); // for set actionbar title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ImageButton home = findViewById(R.id.home_trainingset);

            //home Button leads to main activity
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),MainActivity.class);
                    startActivity(intent);
                }
            });

        } else {
            setContentView(R.layout.trainingset);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_blue)));
            getSupportActionBar().setTitle(getResources().getString(R.string.trainingsetTitle)); // for set actionbar title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //initialize
            explanationDialog = new Dialog(this); //for explanation popup
            setExercise1 = findViewById(R.id.SetExercise1);
            setExercise2 = findViewById(R.id.SetExercise2);
            setExercise3 = findViewById(R.id.SetExercise3);
            startTrainingset = findViewById(R.id.start_trainingset);
            exercise_list = new String[3];

          
            session = pref.getStringSet("ExerciseList",null);

            if(session == null){
                Toast.makeText(this, getResources().getString(R.string.errorMessage),Toast.LENGTH_SHORT).show();
            } else {
                session.toArray(exercise_list);

                //set texts
                setExercise1.setText(exercise_list[0]);
                setExercise2.setText(exercise_list[1]);
                setExercise3.setText(exercise_list[2]);

            }
        }



        startTrainingset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (exercise_list[0]){
                    case "Minimalpaare":
                        intent = new Intent(v.getContext(), MinimalPairs.class);
                        intent.putExtra("exerciseList",exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Tierlaute":
                        intent = new Intent(v.getContext(), AnimalSounds.class);
                        intent.putExtra("exerciseList",exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Bilder erkennen":
                        intent = new Intent(v.getContext(), ImageRecognition.class);
                        intent.putExtra("exerciseList",exercise_list);
                        intent.putExtra("exerciseCounter", 0);
                        intent.putExtra("trainingset", true);
                        break;
                    case "Schneckenrennen":
                        intent = new Intent(v.getContext(), SnailRaceStart.class);
                        intent.putExtra("exerciseList",exercise_list);
                        intent.putExtra("exerciseCounter", 0);
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



    //Fisher-Yates shuffle
    public String[] shuffleArray(String[] array){

        for(int i = array.length - 1; i > 0; i--){
            int index = rand.nextInt(i+1);

            //swap
            String temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
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

        title_e.setText(R.string.trainingsetTitle);
        explanation.setText(R.string.animalSounds_explanation); //TODO explanation trainingset schreiben


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(explanationDialog.getWindow().getAttributes());
        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        explanationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        explanationDialog.getWindow().setAttributes(layoutParams);
        explanationDialog.show();

    }
}