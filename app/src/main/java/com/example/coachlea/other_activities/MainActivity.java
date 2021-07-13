
package com.example.coachlea.other_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;

public class MainActivity extends AppCompatActivity {
    private int All_Code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);

        //check if CoachLea is used for the first time
        int login = prefs.getInt("UserCreated", 0);

        if (login == 0) {
            Intent intent = new Intent(this, LoginInfoScreen.class);
            this.startActivity(intent);
        }

        //initialize
        Button profile = findViewById(R.id.profile);
        Button exercises = findViewById(R.id.exercises);

        exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExercisesMain.class);
                v.getContext().startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileMain.class);
                v.getContext().startActivity(intent);
            }
        });
    }

}