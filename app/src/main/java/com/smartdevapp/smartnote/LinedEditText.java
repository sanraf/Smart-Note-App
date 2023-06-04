package com.smartdevapp.smartnote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;


public class LinedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private  Paint mPaint;
    private Rect mRect;


    public LinedEditText(Context context) {
        super(context);
        initPaint();
    }

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public LinedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


   private void initPaint(){
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));

   }

    @Override
    protected void onDraw(Canvas canvas) {

        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = height/lineHeight;

        if (getLineCount()>count)
            count = getLineCount();
        Rect r = mRect;
        Paint paint = mPaint;

        int baseline = getLineBounds(0,r);


        for (int i = 0;i<count;i++){

            canvas.drawLine(r.left ,baseline+2,r.right,baseline+2,paint);
            baseline+=getLineHeight();
        }
        super.onDraw(canvas);
    }
}
