package com.example.coachlea.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.coachlea.R;


public class SnailRaceGame extends View {

    private final int fieldColor;
    private final int lineColor;

    private final Paint paint = new Paint();

    private int lineWidth = getWidth()*(5/12);

    public SnailRaceGame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SnailRaceGame, 0, 0);

        try {
            fieldColor = a.getInteger(R.styleable.SnailRaceGame_FieldColor,0);
            lineColor = a.getInteger(R.styleable.SnailRaceGame_LineColor,0);

        }finally{
            a.recycle();
        }
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
}
