package com.example.coachlea.Exercises;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;

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

       // animals[0] = R.drawable.a;

        imageView.setImageResource(animals[0]);
    }


}