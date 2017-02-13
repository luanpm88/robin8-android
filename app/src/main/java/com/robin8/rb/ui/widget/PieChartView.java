package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.robin8.rb.R;
import com.robin8.rb.model.Point;
import com.robin8.rb.module.first.model.PieDataBean;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 饼状图
 */
public class PieChartView extends SurfaceView {

    private Paint mPaint;

    float mPx = 3;//像素密度
    int mTextSize = 11;//x、y轴文字大小
    private ArrayList<Point> mPointList = new ArrayList<>();
    private int bg_color = R.color.white_custom;//背景色
    private int line1_color = R.color.blue_custom;//线条1颜色
    private int line2_color = R.color.yellow_custom;//线条2颜色
    private int mWidth;
    private int mHeight;
    private int mPadding;
    private int mPieHeight;
    private int mPieWidth;
    private List<PieDataBean> mDataList = new ArrayList<>();
    private int[] colorId = {R.color.red_custom, R.color.gray_custom, R.color.blue_custom, R.color.yellow_custom, R.color.green_custom};

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDataList(List<PieDataBean> dataList) {
        mDataList.clear();
        mDataList.addAll(dataList);

        if (mDataList.size() > 5) {
            float sum = 0;
            for (int i = 0; i < 4; i++) {
                sum += mDataList.get(i).getProbability();
            }
            PieDataBean other = new PieDataBean("其他", 1 - sum);
            mDataList.add(4, other);
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = width / 2;
        setMeasuredDimension(width, measuredHeight);
    }

    public void init() {
        setBackgroundResource(bg_color);
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void initData() {

        mPadding = (int) (mPx * 10);
        mPieWidth = mPieHeight = mWidth / 2 - mPadding * 2;
        setPadding(mPadding, mPadding, mPadding, mPadding);
        mPx = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(line1_color));
        RectF RectF = new RectF(mPadding, mPadding, mPadding + mPieWidth, mPadding + mPieHeight);
        float startAngle = 0;
        float sweepAngle = 0;

        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        int size = mDataList.size();
        if (size > 5) {
            size = 5;
        }

        for (int i = 0; i < size; i++) {
            sweepAngle = mDataList.get(i).getProbability() * 360;
            mPaint.setStrokeWidth(1);
            mPaint.setColor(getResources().getColor(colorId[i]));
            canvas.drawArc(RectF, startAngle, sweepAngle, true, mPaint);//画扇形
            startAngle = startAngle + sweepAngle;

            mPaint.setStrokeWidth(15 * mPx);
            canvas.drawPoint(mWidth / 2 + mPadding + 8 * mPx, i * 30 * mPx + 20 * mPx, mPaint);//画点

            mPaint.setStrokeWidth(2);
            mPaint.setTextSize(mPx * 15);
            String name = mDataList.get(i).getLabel();

            NumberFormat num = NumberFormat.getPercentInstance();
            num.setMaximumIntegerDigits(3);
            num.setMaximumFractionDigits(2);
            num.setMinimumFractionDigits(2);
            String percent = String.valueOf(num.format((double) (mDataList.get(i).getProbability())));
            canvas.drawText(name + " " + percent, mWidth / 2 + mPadding + mPx * 21,  i * 30 * mPx + 25 * mPx, mPaint);//画文字
        }

        mPaint.setColor(getResources().getColor(bg_color));
        canvas.drawCircle(mPadding + mPieWidth / 2, mPadding + mPieHeight / 2, mPieWidth / 8, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initData();
    }
}
