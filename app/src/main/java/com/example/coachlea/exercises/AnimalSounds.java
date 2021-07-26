package com.example.coachlea.exercises;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;
import com.example.coachlea.other_activities.SpeakingExerciseFinished;
import com.example.coachlea.tools.RadarFeatures;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AnimalSounds extends AppCompatActivity {


    private ProgressBar progress;
    private ImageButton record;
    private Button home;
    private SpeechRecorder recorder;
    private static String path;
    private ImageView imageView;
    private int[] animals;
    private String[] animals_str;
    private boolean isRecording, started;
    private Context con = this;

    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_sounds);

        //initialize
        record = findViewById(R.id.record);
        imageView = findViewById(R.id.imageView);
        recorder = SpeechRecorder.getInstance(this, new AnimalSounds.VolumeHandler(), "AnimalSounds");
        progress=findViewById(R.id.timerProgress);

        //set animal
        animals = new int[]{ R.drawable.affe,  R.drawable.esel, R.drawable.hund_as, R.drawable.katze_as};
        animals_str = getResources().getStringArray(R.array.animal_sounds);

        Random rand = new Random();
        int choose = rand.nextInt(4);
        imageView.setImageResource(animals[choose]);
        String filename = animals_str[choose];

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!started){
                    started = true;
                    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(100, 1000);
                    myCountDownTimer.Start();
                    if (isRecording) {

                    } else {
                        path = recorder.prepare("Animal_Sounds",filename);
                        record.setForeground(getDrawable(R.drawable.ic_mic_red));
                        recorder.record();
                        isRecording = true;

                    }
                }

            }
        });

    }



    class MyCountDownTimer {
        //TODO countdown in wrong direktion and big chunks
        private long maxMillis;
        private int c;

        public MyCountDownTimer(long pmaxMillis, long pCountDownInterval) {
            this.maxMillis = pmaxMillis;
            this.c = 0;
        }
        public void Start()
        {

            final Timer t = new Timer();
            TimerTask tt = new TimerTask(){

                public void run(){
                    if(c ==maxMillis) {
                        progress.setProgress(c);
                        t.cancel();
                        isRecording = false;
                        started = false;
                        //record.setForeground(getDrawable(R.drawable.ic_mic));
                        Intent intent = new Intent(con, SpeakingExerciseFinished.class);
                        intent.putExtra("exercise", "SyllableRepetition");
                        recorder.stopRecording();
                        recorder.release();
                        con.startActivity(intent);
                    } else {
                        //long sec = maxMillis/1000;
                        progress.setProgress(c);
                        c++;


                    }
                }
            };
            t.schedule(tt,0,100);
        }
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

