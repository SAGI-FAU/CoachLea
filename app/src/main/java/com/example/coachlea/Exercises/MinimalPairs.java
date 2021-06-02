package com.example.coachlea.Exercises;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;

import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs extends AppCompatActivity {

    private Random random = new Random();

    private int[] minimal_pairs_all;
    private String[] minimal_pairs_all_str;
    private int[] minimal_pairs = new int[8];
    private String[] minimal_pairs_result;
    private String[] minimal_pairs_correct = new String[8];


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
        botIMG.setImageResource(minimal_pairs[1]);

        topIMG.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

            }

        });




    }

    private void setMinimal_pairs(){
        int position = random.nextInt(8)*2;


        ArrayList<Integer> usedNumber = new ArrayList<>();
        for(int i = 0; i < 4; i++){

            while(usedNumber.contains(position)){
                position = random.nextInt(8)*2;
            }
            int corr = random.nextInt(2);
            minimal_pairs_correct[i] = minimal_pairs_all_str[position + corr];
            minimal_pairs[i*2] = minimal_pairs_all[position];
            minimal_pairs[i*2+1] = minimal_pairs_all[position + 1];

            usedNumber.add(position);

        }

    }

    private void setMinimal_pairs_all(){
        minimal_pairs_all = new int[]{R.drawable.gold, R.drawable.geld, R.drawable.sand, R.drawable.wand, R.drawable.see, R.drawable.tee, R.drawable.hose,
                R.drawable.rose, R.drawable.huhn, R.drawable.hut, R.drawable.baum, R.drawable.schaum, R.drawable.geld, R.drawable.welt, R.drawable.tasche, R.drawable.tasse};
    }
}