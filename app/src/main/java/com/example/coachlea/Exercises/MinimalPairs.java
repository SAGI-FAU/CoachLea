package com.example.coachlea.Exercises;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;

public class MinimalPairs extends AppCompatActivity {

    private int[] minimal_pairs =new int[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimal_pairs);
    }
}