/**
 * Created by Paula Schaefer
 */

package com.example.coachlea.other_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.tools.NotificationReceiver;
import com.example.coachlea.tools.Notifier;

public class MainActivity extends AppCompatActivity {
    private int All_Code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);

        //check if CoachLea is used for the first time
        int login = prefs.getInt("UserCreated", 0);
        

        //open login screen if used for the first time
        if (login == 0) {
            Intent intent = new Intent(this, LoginInfoScreen.class);
            this.startActivity(intent);
        }

        //initialize
        ImageButton dailySession = findViewById(R.id.dailySession);
        ImageButton exercises = findViewById(R.id.exercises);

        exercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExercisesMain.class);
                v.getContext().startActivity(intent);
            }
        });

        dailySession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Trainingset.class);
                v.getContext().startActivity(intent);
            }
        });

        setAlarm();
    }

    //create overflow menu for profile and settings
    @Override
    public  boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, menu);
        return true;

    }

    //overflow menu in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profil:
                Intent intent = new Intent(this, ProfileMain.class);
                startActivity(intent);
                return true;

            case R.id.settings:
                Intent intent2 = new Intent(this, Settings.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //start notifier
    public void setAlarm() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        int hour = p.getInt("Notification Time", 9);
        Notifier notifier = new Notifier(this);
        notifier.setReminder(this, NotificationReceiver.class, hour, 0);
    }

}