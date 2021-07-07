package com.example.coachlea.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coachlea.R;
import com.example.coachlea.exercises.SnailRace;


public class SnailRaceGame extends SurfaceView {

    private SurfaceHolder holder;
    private GameThread gameThread;
    private SrgAudioHandler audioHandler;

    private int lineWidth = getWidth()*(5/12);

    private final Paint paint = new Paint();

    private  int leaX = lineWidth/2;
    private int leaY = 0;
    private int leaSpeed = 0;
    private int currentVolume = 0;

    private int emilyX = (lineWidth/2)+lineWidth;
    private int emilyY = 0;
    private int emilySpeed = 1; //TODO value?

    private int screenHeight = 0;
    private int screenWidth = 0;


    long UPDATE_MILLIS = 30;

    private Bitmap lea, emily;
    private Context context;
    //private boolean isRecording = false;

    private static final String TAG = SnailRace.class.getSimpleName();






    public SnailRaceGame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        //initialize
        emilySpeed = 1;
        audioHandler = new SrgAudioHandler(context);
        currentVolume = 0;


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

    }

    @Override
    protected void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawColor(getResources().getColor(R.color.dark_red));

        canvas.drawBitmap(lea,leaX,leaY,null);

        drawRaceField(canvas);
        drawEmily(canvas);

        //TODO connect currentvolume with lea speed
        currentVolume = audioHandler.getCurrentVolume();
        Log.d(TAG,"currentVolume: " + currentVolume);
        leaSpeed = (int)(currentVolume / (float)30);
        drawLea(canvas);


    }

    private void drawRaceField(Canvas canvas){
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStrokeWidth(30);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,canvas.getHeight(),paint);
        canvas.drawLine((getWidth()/2) - lineWidth,0,getWidth()/2 - lineWidth,canvas.getHeight(),paint);
        canvas.drawLine(getWidth()/2 + lineWidth,0,getWidth()/2 + lineWidth,canvas.getHeight(),paint);

    }




    private void drawEmily(Canvas canvas){
        //finish line collision check

        if(emilyY == 0){
            emilySpeed = 0;
        }


        emilyY = emilyY - emilySpeed;
        canvas.drawBitmap(emily,emilyX,emilyY,null);

    }



    private void drawLea(Canvas canvas){
        if(leaY <= 0){
            leaSpeed = 0;
        }

        leaY = leaY - leaSpeed;
        canvas.drawBitmap(lea,leaX,leaY,null);
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
