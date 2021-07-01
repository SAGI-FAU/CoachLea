package com.example.coachlea.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coachlea.R;


public class SnailRaceGame extends SurfaceView {

    private SurfaceHolder holder;
    private GameThread gameThread;

    private final int fieldColor;
    private final int lineColor;
    private int lineWidth = getWidth()*(5/12);

    private final Paint paint = new Paint();
    private boolean resetState;

    private  int leaX = lineWidth/2;
    private int leaY = 0;
    private int leaSpeed = 0;

    private int emilyX = (lineWidth/2)+lineWidth;
    private int emilyY = 0;
    private int emilySpeed = 10; //TODO value?

    private int screenHeight = 0;
    private int screenWidth = 0;

    //private Handler handler; //TODO ?
    private Runnable runnable; // ?

    long UPDATE_MILLIS = 30;

    private Bitmap lea, emily;
    private Context context;

    private Rect rect;


    public SnailRaceGame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        //for game loop
        gameThread = new GameThread(this);
        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("WrongCall")
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                gameThread.setRunning(true);
                gameThread.start();

                //TODO wieder einkommentieren??
                /*
                Canvas c = holder.lockCanvas();
                onDraw(c);
                holder.unlockCanvasAndPost(c);
                */
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            // if surface is destroyed, thread will stop
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


        //initialize
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SnailRaceGame, 0, 0);

        //Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); //?

        Point size = new Point();
        //display.getSize(size);

        try {
            fieldColor = a.getInteger(R.styleable.SnailRaceGame_FieldColor,0);
            lineColor = a.getInteger(R.styleable.SnailRaceGame_LineColor,0);
        }finally{
            a.recycle();
        }

        /*
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); //calls onDraw
            }
        };

        rect = new Rect(0,0,screenWidth,screenHeight);

        */

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

    }

    @Override
    protected void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawColor(fieldColor);

        canvas.drawBitmap(lea,leaX,leaY,null);

        drawRaceField(canvas);
        drawEmily(canvas);

    }

    private void drawRaceField(Canvas canvas){
        paint.setColor(lineColor);
        paint.setStrokeWidth(30);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,canvas.getHeight(),paint);
        canvas.drawLine((getWidth()/2) - lineWidth,0,getWidth()/2 - lineWidth,canvas.getHeight(),paint);
        canvas.drawLine(getWidth()/2 + lineWidth,0,getWidth()/2 + lineWidth,canvas.getHeight(),paint);

    }


    private void drawEmily(Canvas canvas){
        emilyY++;
        canvas.drawBitmap(emily,emilyX,emilyY,null);


       /* emilyY = emilyY + emilySpeed;
        resetState = true;

        if(emilyY >= screenHeight - emily.getHeight() ){
            resetState = false;
        }
        canvas.drawBitmap(emily, emilyX, emilyY,null);
        */

    }

    /*

    private void drawLea(Canvas canvas){

    } */
}
