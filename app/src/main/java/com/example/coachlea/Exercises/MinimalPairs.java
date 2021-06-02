package com.example.coachlea.Exercises;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;

public class MinimalPairs extends AppCompatActivity {

    private int[] minimal_pairs_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimal_pairs);

        setMinimal_pairs_all();
    }

    private void setMinimal_pairs_all(){
        minimal_pairs_all = new int[]{R.drawable.gold, R.drawable.geld, R.drawable.sand, R.drawable.wand, R.drawable.see, R.drawable.tee, R.drawable.hose,
                R.drawable.rose, R.drawable.huhn, R.drawable.hut, R.drawable.baum, R.drawable.schaum, R.drawable.geld, R.drawable.welt, R.drawable.tasche, R.drawable.tasse};
    }
}