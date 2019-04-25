package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.first.model.ColumnarDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 柱状图
 */
public class ColumnarChartView extends SurfaceView {

    private Paint mPaint;

    float mPx = 3;//像素密度
    private int bg_color = R.color.white_custom;//背景色
    private int mWidth;
    private int mHeight;
    private int mPadding;
    private List<ColumnarDataBean> mDataList = new ArrayList<>();
    private int[] colorId = {R.color.red_custom, R.color.gray_custom, R.color.blue_custom, R.color.yellow_custom, R.color.green_custom};
    private int mMax = 10;
    private int arr[] = {0, 2, 4, 6, 8, 10};
    private int heightSize;
    private int widthSize;

    public ColumnarChartView(Context context) {
        super(context);
        init();
    }

    public ColumnarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColumnarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDataList(List<ColumnarDataBean> dataList) {

        if (dataList == null || dataList.size() == 0) {
            return;
        }

        mDataList.clear();
        mDataList.addAll(dataList);
        mMax = mDataList.get(0).getFreq();
        for (int i = 1; i < mDataList.size(); i++) {
            if (mMax < mDataList.get(i).getFreq()) {
                mMax = mDataList.get(i).getFreq();
            }
        }

        mMax = (mMax / 10 + 1) * 10;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 2 * i * mMax / 10;
        }

        widthSize = (int) (40 * mPx * mDataList.size() + 40 * mPx + mPadding * 2);
        setMeasuredDimension(widthSize, heightSize);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (mDataList.size() <= 5) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            widthSize = (int) (40 * mPx * mDataList.size() + mPadding * 2);
            setMeasuredDimension(widthSize, heightSize);
        }
    }

    public void init() {
        setBackgroundResource(bg_color);
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initData() {

        mDataList.add(new ColumnarDataBean("", 0));
        mDataList.add(new ColumnarDataBean("", 0));
        mDataList.add(new ColumnarDataBean("", 0));
        mDataList.add(new ColumnarDataBean("", 0));
        mDataList.add(new ColumnarDataBean("", 0));

        mPadding = (int) (mPx * 10);
        setPadding(mPadding, mPadding, mPadding, mPadding);
        mPx = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.sub_black_custom));

        canvas.drawLine(mPadding + 30 * mPx, mHeight - mPadding - 30 * mPx, mPadding + 30 * mPx, mPadding, mPaint);//Y轴
        canvas.drawLine(mPadding + 30 * mPx, mHeight - mPadding - 30 * mPx, mWidth - mPadding, mHeight - mPadding - 30 * mPx, mPaint);//X轴

        mPaint.setTextSize(10 * mPx);
        mPaint.setColor(getResources().getColor(R.color.sub_black_custom));
        for (int i = 0; i < arr.length; i++) {
            float textX = mPadding + 28 * mPx;
            float textY = mHeight - mPadding - 30 * mPx - (mHeight - 2 * mPadding - 30 * mPx) * arr[i] * 1.0f / mMax;
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(arr[i]), textX, textY, mPaint);
        }

        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
        for (int i = 0; i < mDataList.size(); i++) {
            float textX = mPadding + 20 * mPx + (i + 1) * 40 * mPx;
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(getResources().getColor(R.color.sub_black_custom));
            canvas.drawText(mDataList.get(i).getLabel(), textX, mHeight - mPadding - 10 * mPx, mPaint);

            float rectXL = textX - 10 * mPx;
            float rectXR = textX + 10 * mPx;
            float rectXT = mHeight - mPadding - 30 * mPx - (mHeight - 2 * mPadding - 30 * mPx) * mDataList.get(i).getFreq() * 1.0f / mMax;
            float rectXB = mHeight - mPadding - 30 * mPx;

            mPaint.setColor(getResources().getColor(colorId[i % colorId.length]));
            canvas.drawRect(rectXL, rectXT, rectXR, rectXB, mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initData();
    }
}
