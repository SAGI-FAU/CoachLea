package com.example.coachlea.other_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;

public class Trainingset_Finished extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainingset__finished);
        getSupportActionBar().setTitle(getResources().getString(R.string.trainingsetTitle)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton home = findViewById(R.id.home_trainingset);

        //home Button leads to main activity
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}