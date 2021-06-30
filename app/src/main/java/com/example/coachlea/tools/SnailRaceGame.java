package com.example.coachlea.tools;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.coachlea.R;


public class SnailRaceGame extends View {

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

        //define Lea and Emily images
        Bitmap tempLea = BitmapFactory.decodeResource(getResources(),R.drawable.lea);
        Bitmap tempEmily = BitmapFactory.decodeResource(getResources(),R.drawable.emily);

        lea = Bitmap.createScaledBitmap(tempLea,lineWidth/2, lineWidth/2,false);
        emily = Bitmap.createScaledBitmap(tempEmily,lineWidth/2,lineWidth/2,false);

        //initialize
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SnailRaceGame, 0, 0);

        Display display = ((Activity)context).getWindowManager().getDefaultDisplay(); //?

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        emilyX= lineWidth + lineWidth/2;
        emilyY= emily.getHeight();

        leaX = lineWidth/2;
        leaY = lea.getHeight();

        try {
            fieldColor = a.getInteger(R.styleable.SnailRaceGame_FieldColor,0);
            lineColor = a.getInteger(R.styleable.SnailRaceGame_LineColor,0);
        }finally{
            a.recycle();
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); //calls onDraw
            }
        };

        rect = new Rect(0,0,screenWidth,screenHeight);


    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);

        lineWidth = (int) ((getMeasuredWidth()/2) - (getMeasuredWidth()/2)*(1/(float)6));

        setMeasuredDimension(getMeasuredWidth(),getMeasuredHeight());

    }

    @Override
    protected void onDraw(Canvas canvas){
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        drawRaceField(canvas);

    }

    private void drawRaceField(Canvas canvas){
        paint.setColor(lineColor);
        paint.setStrokeWidth(30);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,canvas.getHeight(),paint);
        canvas.drawLine((getWidth()/2) - lineWidth,0,getWidth()/2 - lineWidth,canvas.getHeight(),paint);
        canvas.drawLine(getWidth()/2 + lineWidth,0,getWidth()/2 + lineWidth,canvas.getHeight(),paint);

    }

    private void drawEmily(Canvas canvas){
        emilyY = emilyY + emilySpeed;
        resetState = true;

        if(emilyY >= screenHeight - emily.getHeight() ){
            resetState = false;
        }
        canvas.drawBitmap(emily, emilyX, emilyY,null);

    }

    private void drawLea(Canvas canvas){

    }
}
