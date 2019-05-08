package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.robin8.rb.util.DensityUtils;

/**
 * Created by seven on 25/03/2017.
 */

public class CustomRedDotRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    private Paint mPaint;

    public CustomRedDotRadioButton(Context context) {
        super(context);
        initPaint();
    }

    public CustomRedDotRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomRedDotRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2 + DensityUtils.dp2px(5), DensityUtils.dp2px(5), DensityUtils.dp2px(5), mPaint);
    }

    public void hidRedDot() {
        mPaint.setColor(Color.TRANSPARENT);
        postInvalidate();
    }

    public void showRedDot() {
        mPaint.setColor(Color.RED);
        postInvalidate();
    }
}
