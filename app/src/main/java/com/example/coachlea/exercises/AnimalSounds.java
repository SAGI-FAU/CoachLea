package com.example.coachlea.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;
import com.example.coachlea.other_activities.MainActivity;
import com.example.coachlea.other_activities.SpeakingExerciseFinished;
import com.example.coachlea.tools.RadarFeatures;

import java.io.File;
import java.util.Random;

public class AnimalSounds extends AppCompatActivity {

    private Button record;
    private Button home;
    private SpeechRecorder recorder;
    private static String path;
    private ImageView imageView;
    private int[] animals = new int[4];
    private boolean isRecording = false;
    private TextView recordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_sounds);

        //initialize
        record = findViewById(R.id.record);
        home = findViewById(R.id.home3);
        imageView = findViewById(R.id.imageView);
        recordText = findViewById(R.id.RecordAnimalText);

        recorder = SpeechRecorder.getInstance(this, new AnimalSounds.VolumeHandler(), "AnimalSounds");

        //set animal
        animals[0] = R.drawable.affe;
        animals[1] = R.drawable.esel;
        animals[2] = R.drawable.hund_as;
        animals[3] = R.drawable.katze_as;

        Random rand = new Random();
        int choose = rand.nextInt(4);
        imageView.setImageResource(animals[choose]);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    recorder.stopRecording();
                    isRecording = false;
                    recordText.setText(R.string.record);

                } else {
                    path = recorder.prepare("Animal_Sounds","test");
                    recorder.record();
                    isRecording = true;
                    recordText.setText(R.string.recording);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(AnimalSounds.this, MainActivity.class);
                v.getContext().startActivity(intent);

            }
        });
    }


    private class VolumeHandler extends Handler {
        public VolumeHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")) {
                if (path == null) {
                    Toast.makeText(AnimalSounds.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(AnimalSounds.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Float.isNaN(int_f0[0])) {
                        Toast.makeText(AnimalSounds.this, getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent(getApplicationContext(), SpeakingExerciseFinished.class);
                        intent.putExtra("exercise", "Animal sounds");
                        record.setEnabled(false);
                        recorder.release();
                        getApplicationContext().startActivity(intent);
                    }
                }
            }
        }
    }

}

