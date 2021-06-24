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
import com.example.coachlea.other_activities.SpeakingExerciseFinished;
import com.example.coachlea.tools.RadarFeatures;

import java.io.File;


public class ImageRecognition extends AppCompatActivity {

    private static final int EXERCISE_LENGTH = 4;

    private Button record;
    private SpeechRecorder recorder;
    private static String path;
    private ImageView imageView;
    private int[] images_all = new int[EXERCISE_LENGTH];
    private boolean isRecording = false;
    private TextView recordText;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_recognition);

        //initialize
        recorder = SpeechRecorder.getInstance(this, new ImageRecognition.VolumeHandler(), "ImageRecognition");
        record = findViewById(R.id.record2);
        imageView = findViewById(R.id.imageRecognitionView);
        recordText = findViewById(R.id.textView2);
        setImages();

        /*
        Random random = new Random();
        int choose = random.nextInt(4);
        imageView.setImageResource(choose);
        */

        imageView.setImageResource(images_all[counter]);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    recorder.stopRecording();
                    recordText.setText(R.string.record);

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
                    path = recorder.prepare("Image_Recognition",counter);
                    recorder.record();
                    isRecording = true;
                    recordText.setText(R.string.recording);
                }
                //TODO
            }
        });

    }

    private void setImages() { //TODO Bilder
        images_all = new int[]{R.drawable.baum, R.drawable.welt, R.drawable.huhn, R.drawable.rose};
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
                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Float.isNaN(int_f0[0])) {
                        Toast.makeText(ImageRecognition.this, getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
    }
}


