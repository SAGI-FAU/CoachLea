package com.example.coachlea.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;
import com.example.coachlea.other_activities.MainActivity;

public class AnimalSounds extends AppCompatActivity {

    private Button record;
    private Button home;
    private SpeechRecorder recorder;
    private ImageView imageView;
    private int[] animals = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_sounds);

        record = findViewById(R.id.record);
        home = findViewById(R.id.home3);

        //recorder = SpeechRecorder.getInstance(this, new AnimalSounds().VolumeHandler(), "AnimalSounds"); // TODO Volume Handler

        animals[0] = R.drawable.affe;
        animals[1] = R.drawable.esel;
        animals[2] = R.drawable.hund;
        animals[3] = R.drawable.katze;

        imageView.setImageResource(animals[0]);

        home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                Intent intent = new Intent(AnimalSounds.this, MainActivity.class);
                v.getContext().startActivity(intent);

            }
        });
    }


}