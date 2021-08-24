package com.example.coachlea.exercises;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;
import com.example.coachlea.other_activities.SpeakingExerciseFinished;
import com.example.coachlea.other_activities.TrainingsetExerciseFinished;
import com.example.coachlea.tools.RadarFeatures;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class ImageRecognition extends AppCompatActivity {

    private static final int EXERCISE_LENGTH = 5;

    private ArrayList<String> used;
    private Random random;
    private Dialog explanationDialog;

    private int[] fricatives;
    private int[] plosives;
    private int[] nasal;

    private String[] fricatives_str;
    private String[] plosives_str;
    private String[] nasal_str;

    private int[] images_all = new int[EXERCISE_LENGTH];
    private String[] images_all_str = new String[EXERCISE_LENGTH];

    private ImageButton record;
    private SpeechRecorder recorder;
    private static String path;
    private ImageView imageView;
    private boolean isRecording = false;
    private int counter = 0;

    private ProgressBar pb;
    private int currentProgress = 0;
    private TextView progressText;
    private int exerciseCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_recognition);
        getSupportActionBar().setTitle(R.string.image_recognition);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        used = new ArrayList<>(); //some images are in multiple sets
        random = new Random();
        explanationDialog = new Dialog(this);

        //check if ImageRecognition is used for the first time
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        int login = prefs.getInt("ImageRecognitionUsed", 0);

        if(login == 0){
            openPopup();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("ImageRecognitionUsed",13);
            editor.apply();
        }

        //set String arrays
        fricatives_str = getResources().getStringArray(R.array.image_recognition_fricative);
        plosives_str = getResources().getStringArray(R.array.image_recognition_plosive);
        nasal_str = getResources().getStringArray(R.array.image_recognition_nasal);

        //set drawable arrays
        setFricatives();
        setPlosives();
        setNasal();

        //initialize
        if(getIntent().getExtras() != null)
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter", 0);

        recorder = SpeechRecorder.getInstance(this, new ImageRecognition.VolumeHandler(), "ImageRecognition");
        record = findViewById(R.id.record2);
        imageView = findViewById(R.id.imageRecognitionView);
        pb = findViewById(R.id.imageRecognitionProgress);
        currentProgress = 100 / EXERCISE_LENGTH;
        pb.setProgress(currentProgress);
        pb.setMax(100);
        progressText = findViewById(R.id.imageRecognitionText);
        progressText.setText((counter+1)+"/"+EXERCISE_LENGTH);

        //set images for the game
        setImages_all(fricatives, fricatives_str, 2, 0);
        setImages_all(plosives, plosives_str, 2, 2);
        setImages_all(nasal, nasal_str, 1, 4);


        imageView.setImageResource(images_all[counter]);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    recorder.stopRecording();
                    record.setForeground(getDrawable(R.drawable.ic_mic));


                    if (counter < (EXERCISE_LENGTH - 1)) {
                        counter++;
                        imageView.setImageResource(images_all[counter]);
                        currentProgress += (100 / EXERCISE_LENGTH);
                        progressText.setText((counter+1)+"/"+EXERCISE_LENGTH);
                        pb.setProgress(currentProgress);
                        pb.setMax(100);


                    } else {
                        Intent intent = new Intent(getApplicationContext(), SpeakingExerciseFinished.class);
                        intent.putExtra("exercise", "ImageRecognition");
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset",true);
                        }
                        record.setEnabled(false);
                        recorder.release();
                        getApplicationContext().startActivity(intent);
                    }
                    isRecording = false;


                } else {
                    path = recorder.prepare("Image_Recognition", images_all_str[counter]);
                    recorder.record();
                    isRecording = true;
                    record.setForeground(getDrawable(R.drawable.ic_stop));
                }
            }
        });

    }

    private void setImages_all(int[] images, String[] images_str, int amount, int pos) {

        for (int i = pos; i < (pos + amount); i++) {

            int position = random.nextInt(images.length);

            //check if word was already used
            while (used.contains(images_str[position])) {
                position = random.nextInt(images.length);
            }


            //set String array
            images_all_str[i] = images_str[position];

            //set drawable array
            this.images_all[i] = images[position];

            // add used image to arraylist
            used.add(images_str[position]);

        }

    }

    private void setFricatives() {
        fricatives = new int[]{R.drawable.fisch, R.drawable.flasche, R.drawable.frosch, R.drawable.hexe, R.drawable.haus, R.drawable.heizung, R.drawable.hase,
                R.drawable.hund, R.drawable.vogel, R.drawable.eichhoernchen, R.drawable.stuhl, R.drawable.marienkaefer, R.drawable.schiff, R.drawable.schokolade,
                R.drawable.schere, R.drawable.schlange, R.drawable.schluessel, R.drawable.schuh, R.drawable.schwein, R.drawable.pilz, R.drawable.zange,
                R.drawable.pflaster, R.drawable.wurst};
    }

    private void setPlosives() {
        plosives = new int[]{R.drawable.anker, R.drawable.apfel, R.drawable.auto, R.drawable.ball, R.drawable.bank, R.drawable.baum, R.drawable.berg, R.drawable.bett,
                R.drawable.blume, R.drawable.brille, R.drawable.buch, R.drawable.drachen, R.drawable.dusche, R.drawable.elefant, R.drawable.erdbeere, R.drawable.feder,
                R.drawable.fenster, R.drawable.gabel, R.drawable.gespenst, R.drawable.giesskanne, R.drawable.gitarre, R.drawable.glas, R.drawable.jacke, R.drawable.kanne,
                R.drawable.katze, R.drawable.kleid, R.drawable.knoepfe, R.drawable.korb, R.drawable.krokodil, R.drawable.kuh, R.drawable.lampe, R.drawable.mond,
                R.drawable.nagel, R.drawable.nest, R.drawable.pferd, R.drawable.rad, R.drawable.rutsche, R.drawable.schmetterling, R.drawable.schnecke, R.drawable.tasche,
                R.drawable.tasse, R.drawable.taucher, R.drawable.telefon, R.drawable.teller, R.drawable.tiger, R.drawable.topf, R.drawable.wippe, R.drawable.zebra,
                R.drawable.zitrone, R.drawable.zwerg};
    }

    private void setNasal() {
        nasal = new int[]{R.drawable.nuss, R.drawable.schornstein, R.drawable.schwein, R.drawable.sonne, R.drawable.spinne, R.drawable.zange, R.drawable.eimer,
                R.drawable.milch, R.drawable.ananas};
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

        explanation.setText(R.string.imageRecognition_explanation);
        title_e.setText(R.string.image_recognition);
        resId = getResources().getIdentifier("image_recognition_explanation", "raw", getPackageName());
        path = "a" + resId;
        mp3_file  = path.substring(1);

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
        explanationDialog.show();

    }


    private class VolumeHandler extends Handler {
        public VolumeHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            //saidSomething = true;
            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")) {
                if (path == null) {
                    Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();

                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();

                        return;
                    } else if (Float.isNaN(int_f0[0])) {
                        Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();

                        return;
                    }

                }

            }

        }
    }
}


