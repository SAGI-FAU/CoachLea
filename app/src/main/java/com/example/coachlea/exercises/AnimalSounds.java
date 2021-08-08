package com.example.coachlea.exercises;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

    Dialog explanationDialog;
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
        getSupportActionBar().setTitle(R.string.animal_sounds);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        explanationDialog = new Dialog(this);
        record = findViewById(R.id.record);
        imageView = findViewById(R.id.imageView);
        recorder = SpeechRecorder.getInstance(this, new AnimalSounds.VolumeHandler(), "AnimalSounds");
        progress=findViewById(R.id.timerProgress);

        //check if AnimalSounds is used for the first time
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        int login = prefs.getInt("AnimalSoundsUsed", 0);

        if(login == 0){
            openPopup();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("AnimalSoundsUsed",13);
            editor.apply();
        }

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

        title_e.setText(R.string.animal_sounds);
        explanation.setText(R.string.animalSounds_explanation);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(explanationDialog.getWindow().getAttributes());
        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        explanationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        explanationDialog.getWindow().setAttributes(layoutParams);
        explanationDialog.show();

    }

}

