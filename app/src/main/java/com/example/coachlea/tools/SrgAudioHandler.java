package com.example.coachlea.tools;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.coachlea.R;
import com.example.coachlea.data_access.SpeechRecorder;

public class SrgAudioHandler {
    private boolean isRecording = false;
    private SpeechRecorder recorder;
    private static String path;
    private Context context;
    private int currentVolume;
    private String vowel;



    SrgAudioHandler(Context context, String vowel){ //TODO call like: SrgAudioHandler srgAundioHandler = new SrgAudioHandler(this)

        //initialize
        this.context = context;
        this.vowel = vowel;
        isRecording = false;
        recorder = SpeechRecorder.getInstance(context, new SrgAudioHandler.VolumeHandler(), "SnailRace");
        startRecording();

    }

    private void startRecording(){
        if(!isRecording) {
            recorder.prepare(context.getResources().getString(R.string.Snail_race), vowel);
            recorder.record();
            isRecording = true;
        }
    }

    //returns true if succesful, false if not
    public boolean stopRecording(){
        if(isRecording){
            recorder.stopRecording();
            recorder.release();
            isRecording = false;
            return true;
        } else{
            return false;
        }
    }

    public int getCurrentVolume(){
        return this.currentVolume;
    }

    //for speech recording and evaluating
    private class VolumeHandler extends Handler {

        public VolumeHandler() {

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            currentVolume = (int) bundle.getDouble("Volume");

            /*
            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")) {
                if (path == null) {
                    Toast.makeText(context, context.getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                    return;
                }
                File f = new File(path);
                if (f.exists() && !f.isDirectory()) {
                    float[] int_f0 = RadarFeatures.intonation(path);
                    if (int_f0.length == 1) {
                        Toast.makeText(context, context.getResources().getString(R.string.messageAgain), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (Float.isNaN(int_f0[0])) {
                        Toast.makeText(context, context.getResources().getString(R.string.messageEmpty), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } */


        }
    }



}
