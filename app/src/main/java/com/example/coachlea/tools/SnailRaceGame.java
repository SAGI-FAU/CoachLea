
package com.example.coachlea.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coachlea.R;
import com.example.coachlea.exercises.SnailRace;


public class SnailRaceGame extends SurfaceView {

    private SurfaceHolder holder;
    private GameThread gameThread;
    private SrgAudioHandler audioHandler;
    private boolean dailySession;

    private int lineWidth = getWidth()*(5/12);

    private final Paint paint = new Paint();

    private  int leaX = lineWidth/2;
    private int leaY = 0;
    private int leaSpeed = 0;
    private boolean leaStop;
    private int currentVolume = 0;
    private int leaThreshold = 0;
    private int leaWantedPitch = 20; //when the user hits this pitch (in dB), lea has the same speed as emily

    private int emilyX = (lineWidth/2)+lineWidth;
    private int emilyY = 0;
    private int emilySpeed = 1;
    private boolean emilyStop;//TODO value?
    private int emThresholdTop=0;
    private int emThresholdBot = 0;


    private int screenHeight = 0;
    private int screenWidth = 0;

    private boolean buttonsAdded;

    private ImageButton homeBTN;
    private ImageButton againBTN;
    private ImageButton backBTN;
    private ImageButton nextBTN;
    private TextView countdown;

    private boolean countdownRunning;

    private Bitmap lea, emily;
    private Context context;

    private static final String TAG = SnailRace.class.getSimpleName();

    private String vowel;








    public SnailRaceGame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;


        //initialize
        emilySpeed = 3;

        currentVolume = 0;
        emilyStop= false;
        leaStop = false;
        buttonsAdded = false;
        emThresholdBot = 0;
        leaWantedPitch = 18;


        //start countdown
        countdownRunning = true;
        Countdown countdown = new Countdown();
        countdown.countdown();



    }

    private void startGameLoop(){

        //start game
        audioHandler = new SrgAudioHandler(context,vowel);
        //for game loop
        gameThread = new GameThread(this);
        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("WrongCall")
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                gameThread.setRunning(true);
                gameThread.start();


            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

                boolean retry = true;
                gameThread.setRunning(false);
                while(retry){
                    try {
                        //join method waits for this thread to die
                        gameThread.join();
                        retry = false;
                    } catch (InterruptedException e){

                    }
                }

            }
        });


    }

    class Countdown{
         long millisInFuture = 3000;
         long countDownInterval = 1000;

        public void countdown(){
            final Handler handler = new Handler();
            final Runnable counter = new Runnable(){

            public void run(){
                    if(millisInFuture <= 0) {
                        countdown.setText("" + millisInFuture/1000);
                        countdownRunning = false;
                        countdown.setVisibility(View.GONE);
                    } else {
                        long sec = millisInFuture/1000;
                        countdown.setText("" + sec);
                        millisInFuture -= countDownInterval;
                        handler.postDelayed(this, countDownInterval);
                    }
                }
            };
            handler.postDelayed(counter, countDownInterval);

        }

    }

    public void setDailySession(boolean bool){
        this.dailySession = bool;
        startGameLoop(); //start gameloop after variables have been set
    }
    public void setVowel(String vowel){
        this.vowel = vowel;
    }

    public void setButtons(ImageButton homeBTN, ImageButton againBTN, ImageButton backBTN,ImageButton nextBTN, TextView countdown){
        this.homeBTN = homeBTN;
        this.againBTN = againBTN;
        this.backBTN = backBTN;
        this.nextBTN = nextBTN;
        this.countdown = countdown;

    }

    //set variables
    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        lineWidth = (int) ((getMeasuredWidth()/2) - (getMeasuredWidth()/2)*(1/(float)6));

        screenWidth = getMeasuredWidth();
        screenHeight = getMeasuredHeight();

        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());
        setSnails();

    }

    //set Snail Bitmaps
    protected void setSnails(){
        //define Lea and Emily images
        Bitmap tempLea = BitmapFactory.decodeResource(getResources(),R.drawable.lea);
        Bitmap tempEmily = BitmapFactory.decodeResource(getResources(),R.drawable.emily);

        lea = Bitmap.createScaledBitmap(tempLea,(int)(lineWidth*0.75), (int)(lineWidth*0.75),false);
        emily = Bitmap.createScaledBitmap(tempEmily,(int)(lineWidth*0.75),(int)(lineWidth*0.75),false);

        emilyX = lineWidth + (lineWidth/3);
        leaX = lineWidth/3;

        emilyY = screenHeight - emily.getHeight();
        leaY = screenHeight - lea.getHeight();
        emThresholdTop = emilyY;

        leaThreshold = leaY + (lea.getHeight()/2);

    }

    @Override
    protected void onDraw(Canvas canvas){

        //Add bottom navigation Buttons after finished game
        if(leaStop && emilyStop && !buttonsAdded){

            //Buttons can only be accessed from UI Thread
            ((SnailRace)context).runOnUiThread(new Runnable() {
                public void run(){

                    if(dailySession){
                       nextBTN.setVisibility(View.VISIBLE);
                    } else {
                        againBTN.setVisibility(View.VISIBLE);
                        homeBTN.setVisibility(View.VISIBLE);
                        backBTN.setVisibility(View.VISIBLE);
                    }
                    leaY = 0;
                    emilyY= 0;
                    destroyThread(); //stop game loop
                    buttonsAdded = true;
                }
            });
        }



        drawRaceField(canvas);
        drawEmily(canvas);

        currentVolume = audioHandler.getCurrentVolume();
        double volume_in_db = 0;
        if (currentVolume > 1){
            volume_in_db= 20* Math.log10(currentVolume);
        }

        if (volume_in_db < 20){
            leaSpeed = 0;
        } else if (volume_in_db >= 20 && volume_in_db < 25){
            leaSpeed = 1;
        } else if(volume_in_db >= 25 && volume_in_db < 30) {
            leaSpeed = 2;
        } else if(volume_in_db >= 30 && volume_in_db < 35){
            leaSpeed = 3;
        } else if(volume_in_db >= 35 && volume_in_db < 40){
            leaSpeed = 4;
        } else if( volume_in_db >= 40){
            leaSpeed = 5;
        }


        Log.d(TAG,"volume in db: " + volume_in_db);
        Log.d(TAG,"leaSpeed: " + leaSpeed);
        drawLea(canvas);


    }

    private void drawRaceField(Canvas canvas){
        //draw background
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawColor(getResources().getColor(R.color.dark_red));

        //draw field
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(30);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,canvas.getHeight(),paint);
        canvas.drawLine((getWidth()/2) - lineWidth,0,getWidth()/2 - lineWidth,canvas.getHeight(),paint);
        canvas.drawLine(getWidth()/2 + lineWidth,0,getWidth()/2 + lineWidth,canvas.getHeight(),paint);


        int stepsize = (int)(lineWidth/(float)10);
        int color1 = 0;
        int color2 = 0;

        paint.setStrokeWidth(stepsize);
        //draw finish line
        for(int i = 0; i < screenWidth; i = i + stepsize){

            if(i % (2 *stepsize) == 0){
                color1 = R.color.white;
                color2 = R.color.dark_red;
            } else {
                color1 = R.color.dark_red;
                color2 = R.color.white;
            }

            paint.setColor(getResources().getColor(color1));
            canvas.drawLine(i, (3*lea.getHeight())/4, i + stepsize, (3*lea.getHeight())/4, paint);

            paint.setColor(getResources().getColor(color2));
            canvas.drawLine(i, (3*lea.getHeight())/4 + paint.getStrokeWidth(), i + stepsize, (3*lea.getHeight())/4 + paint.getStrokeWidth(), paint);

        }


        float y = paint.getStrokeWidth();
        paint.setColor(getResources().getColor(R.color.dark_red));
        paint.setStrokeWidth(10);
        canvas.drawLine(0, (3*lea.getHeight())/4 + (3*y)/2,screenWidth, (3*lea.getHeight())/4 + (3*y)/2, paint);
        canvas.drawLine(0, (3*lea.getHeight())/4 - y/2,screenWidth, (3*lea.getHeight())/4 - y/2, paint);

        float y2 = paint.getStrokeWidth();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(12);

        canvas.drawLine(0, (3*lea.getHeight())/4 + (3*y)/2 + y2,screenWidth, (3*lea.getHeight())/4 + (3*y)/2+y2, paint);
        canvas.drawLine(0, (3*lea.getHeight())/4 - y/2 -y2,screenWidth, (3*lea.getHeight())/4 - y/2- y2, paint);


    }


    private void drawEmily(Canvas canvas){
        //finish line collision check
        if(emilyY <= 0){
            emilySpeed = 0;
            emilyStop = true;
        }

        //countdown check
        if(countdownRunning){
            emilySpeed = 0;
        } else if (!emilyStop){
            emilySpeed = 3;
        }



        emilyY = emilyY - emilySpeed; //move snail

        //move thresholds
        emThresholdTop = emilyY - 5;
        emThresholdBot = emilyY + emily.getHeight() + 5;

        //draw snail
        canvas.drawBitmap(emily,emilyX,emilyY,null);

        //stop drawing thresholds after finish line
        if(!emilyStop){
            drawEmThresholds(canvas);
        }


    }

    private void drawEmThresholds(Canvas canvas){
        //set color and stroke width
        paint.setColor(getResources().getColor(R.color.black));
        paint.setStrokeWidth(10);
        int stepsize = (int)(lineWidth/(float)10);

        for(int i = 0; i < screenWidth; i += stepsize){
            if(i % (2 *stepsize) == 0){
                canvas.drawLine(i, emThresholdTop, i + stepsize, emThresholdTop, paint);
                canvas.drawLine(i, emThresholdBot, i + stepsize, emThresholdBot, paint);

            } else {
                continue;
            }
        }

    }



    private void drawLea(Canvas canvas){

        //finish line collision check
        if(leaY <= 0){
            leaSpeed = 0;
            leaStop = true;
        }

        //countdown check
        if(countdownRunning){
            leaSpeed = 0;
        }

        leaY = leaY - leaSpeed; //set new speed

        leaThreshold = leaY + (lea.getHeight()/2); //set new threshold
        canvas.drawBitmap(lea,leaX,leaY,null);

        //draw grey foreground that indicates that the snail has to go faster/slower
        if(!leaStop){
            if(leaThreshold < emThresholdTop){ //smaller because the snails start from the bottom -> higher snail means lower y
                drawLeaThreshold(canvas,true);
            } else if(leaThreshold > emThresholdBot){
                drawLeaThreshold(canvas,false);
            }
        }
    }

    //draw grey filter over the game, if ThresholdTop = true over emily, if thresholdTop = false below emily
    private void drawLeaThreshold(Canvas canvas, boolean thresholdTop){

        paint.setColor(getResources().getColor(R.color.dark_grey));
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(140);

        if(thresholdTop){
            canvas.drawRect(0,0,screenWidth,emThresholdTop - 5,paint);
        } else {
            canvas.drawRect(0,emThresholdBot+5,screenWidth,screenHeight,paint);
        }

    }


    //stop thread
    public void destroyThread(){

        boolean retry = true;
        gameThread.setRunning(false);
        while(retry){
            try {
                //join method waits for this thread to die
                gameThread.join();
                retry = false;
            } catch (InterruptedException e){

            }
        }

        if(!audioHandler.stopRecording()){
            //TODO fehlerbehandlung
        }

        //free storage of bitmaps
        lea.recycle();
        emily.recycle();

    }

}
