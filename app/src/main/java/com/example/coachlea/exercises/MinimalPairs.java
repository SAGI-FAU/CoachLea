package com.example.coachlea.exercises;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.other_activities.MinimalPairsExerciseFinished;
import com.example.coachlea.R;
import com.example.coachlea.data_access.CSVFileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs extends AppCompatActivity {

    private static final int EXERCISE_LENGTH = 4;


    private int[] minimal_pairs_all;
    private String[] minimal_pairs_all_str;
    private int[] minimal_pairs = new int[2*EXERCISE_LENGTH];
    private int[] minimal_pairs_result = new int[2*EXERCISE_LENGTH];
    private String[] minimal_pairs_result_str = new String[2*EXERCISE_LENGTH];
    private String[] minimal_pairs_correct_str = new String[2*EXERCISE_LENGTH];
    private String[] minimal_pairs_false_str = new String[2*EXERCISE_LENGTH];
    private int[] minimal_pairs_correct = new int[2*EXERCISE_LENGTH];
    private boolean playPressed = false;
    private int choose = 0;
    private int oldPos = 2;



    private ImageButton topIMG;
    private ImageButton botIMG;
    private MediaPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimal_pairs);

        setMinimal_pairs_all();
        minimal_pairs_all_str = getResources().getStringArray(R.array.Minimal_Pairs);

        //Initialize
        topIMG = findViewById(R.id.topIMG);
        botIMG = findViewById(R.id.botIMG);
        Button play = findViewById(R.id.playBTN);

        setMinimal_pairs();

        topIMG.setImageResource(minimal_pairs[0]);
        topIMG.setBackgroundResource(0);

        botIMG.setImageResource(minimal_pairs[1]);
        botIMG.setBackgroundResource(0);

        //setting tags for correct or false button
        if(minimal_pairs[0] == minimal_pairs_correct[0]){
            topIMG.setTag("correct");
            botIMG.setTag("false");
        } else {
            topIMG.setTag("false");
            botIMG.setTag("correct");
        }

        // top ImageButton
        topIMG.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (playPressed) {
                    if (oldPos >= 2*EXERCISE_LENGTH) {

                        Intent intent = new Intent(MinimalPairs.this, MinimalPairsExerciseFinished.class);

                        if(topIMG.getTag() == "correct"){
                            topIMG.setBackgroundResource(R.color.limegreen);
                            minimal_pairs_result[choose] = 1;
                            minimal_pairs_result_str[choose] = minimal_pairs_correct_str[choose];
                        } else {
                            minimal_pairs_result[choose] = 0;
                            minimal_pairs_result_str[choose] = minimal_pairs_false_str[choose];
                        }

                        intent.putExtra("correct_words",minimal_pairs_correct_str);
                        intent.putExtra("results",minimal_pairs_result);

                        //TODO store data
                        try {
                            export_data();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        // time delay for green frame
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            public void run(){
                                v.getContext().startActivity(intent);
                            }

                        },400);


                    } else {
                        playPressed = false;

                        nextButtons(topIMG);

                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }

                }
            }

        });

        // bottom ImageButton
        botIMG.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                if (oldPos >= 2*EXERCISE_LENGTH) {
                    Intent intent = new Intent(MinimalPairs.this, MinimalPairsExerciseFinished.class);

                    if(botIMG.getTag() == "correct"){
                        botIMG.setBackgroundResource(R.color.limegreen);
                        minimal_pairs_result[choose] = 1;
                        minimal_pairs_result_str[choose] = minimal_pairs_correct_str[choose];
                    } else {
                        minimal_pairs_result[choose] = 0;
                        minimal_pairs_result_str[choose] = minimal_pairs_false_str[choose];
                    }

                    intent.putExtra("correct_words",minimal_pairs_correct_str);
                    intent.putExtra("results",minimal_pairs_result);

                    //TODO store data
                    try {
                        export_data();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // time delay for green frame
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        public void run(){
                            v.getContext().startActivity(intent);
                        }

                    },400);


                } else {
                    playPressed = false;

                    nextButtons(botIMG);

                    if (player != null) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                }

            }
        });


        //play Button
        play.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                playPressed = true;

                if(player != null){
                    player.seekTo(0);
                    player.start();
                } else {
                    //TODO replace ä ö ü ß?
                    String file = minimal_pairs_correct_str[choose];
                    int resId = getResources().getIdentifier(file,"raw",getPackageName());
                    String path = "a" + resId;
                    String path2 = path.substring(1);
                    player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                    player.start();
                }

            }
        });




    }

    //set next Buttons
    private void nextButtons(ImageButton imageButton){

        //check whether the right or wrong word was chosen
        if(imageButton.getTag() == "correct"){
            imageButton.setBackgroundResource(R.color.limegreen);
            minimal_pairs_result[choose] = 1;
            minimal_pairs_result_str[choose] = minimal_pairs_correct_str[choose];

        } else {
            minimal_pairs_result[choose] = 0;
            minimal_pairs_result_str[choose] = minimal_pairs_false_str[choose];

        }
        choose++;

        Random random = new Random();
        int position = random.nextInt(2);

        int a= oldPos + (position % 2);
        int b= oldPos + ((position + 1) % 2);


        //Time delay for green frame as feedback
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            //set next Buttons
            public void run(){
                imageButton.setBackgroundResource(0);
                topIMG.setImageResource(minimal_pairs[a]);
                botIMG.setImageResource(minimal_pairs[b]);

            }

        },400);

        //set next tags
        if(minimal_pairs[a] == minimal_pairs_correct[choose]){
            topIMG.setTag("correct");
            botIMG.setTag("false");
        } else {
            topIMG.setTag("false");
            botIMG.setTag("correct");
        }

        oldPos += 2;
    }

    //randomly choose minimal pairs & correct words
    private void setMinimal_pairs(){
        Random random = new Random();
        int position = random.nextInt(EXERCISE_LENGTH*2)*2;


        ArrayList<Integer> usedNumber = new ArrayList<>();
        for(int i = 0; i < EXERCISE_LENGTH; i++){

            while(usedNumber.contains(position)){
                position = random.nextInt(EXERCISE_LENGTH*2)*2;
            }
            int corr = random.nextInt(2);
            int flse = (corr + 1) % 2;
            minimal_pairs_correct_str[i] = minimal_pairs_all_str[position + corr];
            minimal_pairs_false_str[i] = minimal_pairs_all_str[position + flse];
            minimal_pairs_correct[i] = minimal_pairs_all[position + corr];
            minimal_pairs[i*2] = minimal_pairs_all[position];
            minimal_pairs[i*2+1] = minimal_pairs_all[position + 1];

            usedNumber.add(position);

        }

    }

    private void setMinimal_pairs_all(){
        minimal_pairs_all = new int[]{R.drawable.gold, R.drawable.geld, R.drawable.sand, R.drawable.wand, R.drawable.see, R.drawable.tee, R.drawable.hose,
                R.drawable.rose, R.drawable.huhn, R.drawable.hut, R.drawable.baum, R.drawable.schaum, R.drawable.geld, R.drawable.welt, R.drawable.tasche, R.drawable.tasse};
    }

    private void export_data() throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CoachLea/METADATA/RESULTS/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("MinimalPairs", PATH); // TODO exerciseName ändern falls name geändert wird
        String[] start = {"correct_word", "chosen_word"};
        mCSVFileWriter.write(start);


        for(int i=0; i < EXERCISE_LENGTH; i++){
            String[] correct_result = {minimal_pairs_correct_str[i], minimal_pairs_false_str[i]};
            mCSVFileWriter.write(correct_result);
        }
        mCSVFileWriter.close();

    }


}