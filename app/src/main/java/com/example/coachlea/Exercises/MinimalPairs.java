package com.example.coachlea.Exercises;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.MinimalPairsExerciseFinished;
import com.example.coachlea.R;

import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs extends AppCompatActivity {


    private int[] minimal_pairs_all;
    private String[] minimal_pairs_all_str;
    private int[] minimal_pairs = new int[8];
    private int[] minimal_pairs_result = new int[8];
    private String[] minimal_pairs_correct_str = new String[8];
    private int[] minimal_pairs_correct = new int[8];
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
        //topIMG.setBackgroundResource(R.color.limegreen);
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
                    if (oldPos >= 8) {

                        Intent intent = new Intent(MinimalPairs.this, MinimalPairsExerciseFinished.class);

                        if(topIMG.getTag() == "correct"){
                            topIMG.setBackgroundResource(R.color.limegreen);
                            minimal_pairs_result[choose] = 1;
                        } else {
                            minimal_pairs_result[choose] = 0;
                        }

                        intent.putExtra("correct_words",minimal_pairs_correct_str);
                        intent.putExtra("results",minimal_pairs_result);

                        //TODO store data
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
                if (oldPos >= 8) {
                    Intent intent = new Intent(MinimalPairs.this, MinimalPairsExerciseFinished.class);

                    if(botIMG.getTag() == "correct"){
                        botIMG.setBackgroundResource(R.color.limegreen);
                        minimal_pairs_result[choose] = 1;
                    } else {
                        minimal_pairs_result[choose] = 0;
                    }

                    intent.putExtra("correct_words",minimal_pairs_correct_str);
                    intent.putExtra("results",minimal_pairs_result);

                    //TODO store data
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

    private void nextButtons(ImageButton imageButton){

        if(imageButton.getTag() == "correct"){
            imageButton.setBackgroundResource(R.color.limegreen);
            minimal_pairs_result[choose] = 1;
        } else {
            minimal_pairs_result[choose] = 0;
        }
        choose++;

        Random random = new Random();
        int position = random.nextInt(2);

        int a= oldPos + (position % 2);
        int b= oldPos + ((position + 1) % 2);


        //Time delay for green frame as feedback
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run(){
                imageButton.setBackgroundResource(0);
                topIMG.setImageResource(minimal_pairs[a]);
                botIMG.setImageResource(minimal_pairs[b]);

            }

        },400);





        if(minimal_pairs[a] == minimal_pairs_correct[choose]){
            topIMG.setTag("correct");
            botIMG.setTag("false");
        } else {
            topIMG.setTag("false");
            botIMG.setTag("correct");
        }

        oldPos += 2;
    }


    private void setMinimal_pairs(){
        Random random = new Random();
        int position = random.nextInt(8)*2;


        ArrayList<Integer> usedNumber = new ArrayList<>();
        for(int i = 0; i < 4; i++){

            while(usedNumber.contains(position)){
                position = random.nextInt(8)*2;
            }
            int corr = random.nextInt(2);
            minimal_pairs_correct_str[i] = minimal_pairs_all_str[position + corr];
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

    public int[] getMinimal_pairs_result(){
        return minimal_pairs_result;
    }

    public String[] getMinimal_pairs_all_str() {
        return minimal_pairs_all_str;
    }

    public String[] getMinimal_pairs_correct_str(){
        return minimal_pairs_correct_str;
    }
}