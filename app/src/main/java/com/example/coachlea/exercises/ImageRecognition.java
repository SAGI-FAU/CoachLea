package com.example.coachlea.exercises;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;
import com.example.coachlea.other_activities.SpeakingExerciseFinished;
import com.example.coachlea.tools.RadarFeatures;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;


public class ImageRecognition extends AppCompatActivity {

    private static final int EXERCISE_LENGTH = 5;

    ArrayList<String> used;
    Random random;

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
    private boolean saidSomething = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_recognition);

        used = new ArrayList<>(); //some images are in multiple sets
        random = new Random();

        //set String arrays
        fricatives_str = getResources().getStringArray(R.array.image_recognition_fricative);
        plosives_str = getResources().getStringArray(R.array.image_recognition_plosive);
        nasal_str = getResources().getStringArray(R.array.image_recognition_nasal);

        //set drawable arrays
        setFricatives();
        setPlosives();
        setNasal();

        //initialize
        recorder = SpeechRecorder.getInstance(this, new ImageRecognition.VolumeHandler(), "ImageRecognition");
        record = findViewById(R.id.record2);
        imageView = findViewById(R.id.imageRecognitionView);
        //recordText = findViewById(R.id.textView2);
        saidSomething = true;

        //set images for the game
        setImages_all(fricatives,fricatives_str,2,0);
        setImages_all(plosives, plosives_str, 2, 2);
        setImages_all(nasal, nasal_str, 1,4);


        imageView.setImageResource(images_all[counter]);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    recorder.stopRecording();
                    //recordText.setText(R.string.record);
                    record.setForeground(getDrawable(R.drawable.ic_mic));

                    if (counter < (EXERCISE_LENGTH-1)) {
                        counter++;
                        imageView.setImageResource(images_all[counter]);


                    } else {
                        Intent intent = new Intent(getApplicationContext(), SpeakingExerciseFinished.class);
                        intent.putExtra("exercise", "Image Recognition");
                        record.setEnabled(false);
                        recorder.release();
                        getApplicationContext().startActivity(intent);
                    }
                    isRecording = false;

                } else {
                    path = recorder.prepare("Image_Recognition",images_all_str[counter]);
                    recorder.record();
                    isRecording = true;
                    //recordText.setText(R.string.recording);
                    record.setForeground(getDrawable(R.drawable.ic_stop));
                }
                //TODO
            }
        });

    }

    private void setImages_all(int[] images, String[] images_str, int amount, int pos){

        for( int i = pos; i < (pos + amount); i++ ){

            int position = random.nextInt(images.length);

            //check if both words were already used
            while(used.contains(images_str[position]) && used.contains(images_str[position + 1])){
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

    private void setFricatives(){
        fricatives = new int[]{R.drawable.fisch, R.drawable.flasche, R.drawable.frosch, R.drawable.hexe, R.drawable.haus, R.drawable.heizung, R.drawable.hase,
                R.drawable.hund, R.drawable.vogel, R.drawable.eichhoernchen, R.drawable.stuhl, R.drawable.marienkaefer, R.drawable.schiff, R.drawable.schokolade,
                R.drawable.schere, R.drawable.schlange, R.drawable.schluessel, R.drawable.schuh, R.drawable.schwein, R.drawable.pilz, R.drawable.zange,
                R.drawable.pflaster, R.drawable.wurst};
    }

    private void setPlosives(){
        plosives = new int[]{R.drawable.anker, R.drawable.apfel, R.drawable.auto, R.drawable.ball, R.drawable.bank, R.drawable.baum, R.drawable.berg, R.drawable.bett,
                R.drawable.blume, R.drawable.brille, R.drawable.buch, R.drawable.drachen, R.drawable.dusche, R.drawable.elefant, R.drawable.erdbeere, R.drawable.feder,
                R.drawable.fenster, R.drawable.gabel, R.drawable.gespenst, R.drawable.giesskanne, R.drawable.gitarre, R.drawable.glas, R.drawable.jacke, R.drawable.kanne,
                R.drawable.katze, R.drawable.kleid, R.drawable.knoepfe, R.drawable.korb, R.drawable.krokodil, R.drawable.kuh, R.drawable.lampe, R.drawable.mond,
                R.drawable.nagel, R.drawable.nest, R.drawable.pferd, R.drawable.rad, R.drawable.rutsche, R.drawable.schmetterling, R.drawable.schnecke, R.drawable.tasche,
                R.drawable.tasse, R.drawable.taucher, R.drawable.telefon, R.drawable.teller, R.drawable.tiger, R.drawable.topf, R.drawable.wippe, R.drawable.zebra,
                R.drawable.zitrone, R.drawable.zwerg};
    }

    private void setNasal(){
        nasal = new int[]{R.drawable.nuss, R.drawable.schornstein, R.drawable.schwein, R.drawable.sonne, R.drawable.spinne, R.drawable.zange, R.drawable.eimer,
                R.drawable.milch, R.drawable.ananas};
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
                    Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                    saidSomething = false;
                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                        saidSomething = false;
                        return;
                    }
                    if (Float.isNaN(int_f0[0])) {
                        Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();
                        saidSomething = false;
                        return;
                    }
                    saidSomething = true;
                }
                saidSomething = true;
            }
        }
    }
}


