package com.robin8.rb.module.find.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 可收起展开的TextView
 Created by zc on 2018/3/29. */

public class ExpandableTextView extends RelativeLayout {
    private TextView mText;

    private TextView mExpandText;


    private int mTextColor = Color.GRAY;

    private int mTextLine = 3;

    private int mStart;

    private int mEnd;

    private boolean isFirst = true;

    private boolean isExpand = false;
    private boolean isHave = false;
    private TextView show;
    private int myLine;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMyLine(mExpandText.getLineCount());
        final ViewGroup.LayoutParams params = getLayoutParams();
        if (isFirst) {
            isFirst = false;
            mText.post(new Runnable() {

                @Override
                public void run() {
                    mStart = mText.getLineHeight() * mText.getLineCount();
                    params.height = mStart;
                    setLayoutParams(params);
                }
            });
            mExpandText.post(new Runnable() {

                @Override
                public void run() {
                    mEnd = mExpandText.getLineHeight() * mExpandText.getLineCount();
                }
            });
        }

    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mText = new TextView(context, attrs);
        mText.setTextColor(mTextColor);
        mText.setEllipsize(TextUtils.TruncateAt.END);
        mText.setMaxLines(mTextLine);
        addView(mText, params);
        mExpandText = new TextView(context);
        mExpandText.setTextColor(Color.TRANSPARENT);
        addView(mExpandText, params);
    }

    public void setText(String text) {
        isFirst = true;
        mText.setText(text);
        mExpandText.setText(text);
        setMyLine(mExpandText.getLineCount());
        requestLayout();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        mText.setTextColor(color);
    }

    public void setTextSize(int size) {
        isFirst = true;
        mText.setTextSize(size);
        mExpandText.setTextSize(size);
        requestLayout();
    }

    public void setTextMaxLine(int num) {
        mTextLine = num;
        mText.setMaxLines(num);
    }

    public void setGravity(int gravity) {
        mText.setGravity(gravity);
        mExpandText.setGravity(gravity);
    }

    public void setEllipsize(TextUtils.TruncateAt ell) {
        mText.setEllipsize(ell);
    }

    public void setTextLineSpacingExtra(float spac) {
        mText.setLineSpacing(spac, 1.0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mText.setLetterSpacing(0.02f);
            mExpandText.setLetterSpacing(0.02f);
        }
        mExpandText.setLineSpacing(spac, 1.0f);

    }

    public TextView text() {
        return mText;
    }

    public TextView expandText() {
        return mExpandText;
    }

    public int line() {
        return mTextLine;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void expand() {
        if (! isExpand) {
            isExpand = true;
            mText.setTextColor(Color.TRANSPARENT);
            mExpandText.setTextColor(mTextColor);
            Animation animation = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = ExpandableTextView.this.getLayoutParams();
                    params.height = mStart + (int) ((mEnd - mStart) * interpolatedTime);
                    setLayoutParams(params);
                }
            };
            animation.setDuration(500);
            startAnimation(animation);
        }

    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
    }

    public void shrink() {
        if (isExpand) {
            isExpand = false;
            Animation animation = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.LayoutParams params = ExpandableTextView.this.getLayoutParams();
                    params.height = mStart + (int) ((mEnd - mStart) * (1 - interpolatedTime));
                    setLayoutParams(params);
                }
            };
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mText.setTextColor(mTextColor);
                    mExpandText.setTextColor(Color.TRANSPARENT);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(500);
            startAnimation(animation);
        }
    }

    public void switchs() {

        if (isExpand) {
            // LogUtil.LogShitou("这是", "isExpand===>" + isExpand);
            shrink();
        } else {
            // LogUtil.LogShitou("这是走的是", "isExpand===>" + isExpand);
            expand();
        }
    }

    public boolean isShow() {
        return isExpand;
    }

    public TextView getmExpandText() {
        return mExpandText;
    }

    public void setmExpandText(TextView mExpandText) {
        this.mExpandText = mExpandText;
    }

    public int getMyLine() {
        return myLine;
    }

    public void setMyLine(int myLine) {
        this.myLine = myLine;
    }
}
