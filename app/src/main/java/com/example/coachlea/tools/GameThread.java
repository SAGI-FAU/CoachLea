package com.example.coachlea.tools;

import android.graphics.Canvas;

public class GameThread extends Thread {

    private SnailRaceGame snailRaceGame;
    private boolean running = false;

    public  GameThread(SnailRaceGame snailRaceGame){
        this.snailRaceGame = snailRaceGame;
    }

    public void setRunning(boolean run){
        running = run;
    }

    @Override
    public void run(){
        while (running){
            Canvas c = null;
            try{
                c = snailRaceGame.getHolder().lockCanvas();
                synchronized (snailRaceGame.getHolder()){
                    snailRaceGame.onDraw(c);
                }
            } finally {
                if (c != null){
                    snailRaceGame.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }


}
