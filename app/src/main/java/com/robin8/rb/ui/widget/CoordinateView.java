package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.ui.model.Point;

import java.util.ArrayList;

/**
 * 坐标轴
 */
public class CoordinateView extends View {

    private Paint mPaint;

    float mPx = 3;//像素密度
    int mTextSize = 11;//x、y轴文字大小
    private int mTextDix = 3;
    private ArrayList<Point> mPointList = new ArrayList<>();
    private int heightSize;

    public void setArrX(String[] arrX) {
        this.arrX = arrX;
    }

    public void setClickNum(String[] numArr) {
        clickNum = numArr;
    }

    public void setClickNum2(String[] clickNum2) {
        this.clickNum2 = clickNum2;
    }

    private String arrX[] = {"2016-09-16", "2016-09-17", "2016-09-18"};
    private int arrY[] = {0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private String[] clickNum = {"0", "0", "0"};//总点击数
    private String[] clickNum2 = {"0", "0", "0"};//计费点击数
    private int mTotalX;
    private int mTotalY;
    private float mOriginalX;
    private float mOriginalY;

    private float mTopY = 30;//y轴文字顶部间隙
    private float mRightX = 100;//x轴文字右边间隙

    private float mGapY = 10;//文字与y轴间隙
    private float mGapX = 10;//文字与x轴间隙想
    private int mLengthX;
    private int mLengthY;
    private int mMaxY;

    private int bg_color = R.color.white_custom;//背景色
    private int line1_color = R.color.blue_custom;//线条1颜色
    private int line2_color = R.color.yellow_custom;//线条2颜色
    private int line1_fill_color = R.color.blue_transparent_custom;//线条1填充颜色
    private int line2_fill_color = R.color.yellow_transparent_custom;//线条2填充颜色
    private int axis_color = R.color.sub_gray_custom;//x、y轴的颜色

    public CoordinateView(Context context) {
        super(context);
        init();
    }

    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoordinateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (arrX.length <= 4) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int width;
            width = (int) (80 * mPx * arrX.length);
            setMeasuredDimension(width, heightSize);
        }
    }

    public void init() {
        initData();
        setBackgroundResource(bg_color);
        mPaint = new Paint();
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void notifyDateSetChanged() {
        init();
        int width = (int) (80 * mPx * arrX.length);
        setMeasuredDimension(width, heightSize);
        invalidate();
    }

    private void initData() {
        setPadding((int) (mPx * 10), (int) (mPx * 20), (int) (mPx * 0), (int) (mPx * 10));
        mOriginalX = getPaddingLeft() + 2 * mTextSize * mPx;//Y轴距边缘距离
        mOriginalY = getPaddingBottom() + mTextSize * mPx + mTextDix * mPx;//X轴距边缘距离

        mPx = getResources().getDisplayMetrics().density;
        mMaxY = getMaxY(clickNum);
        for (int i = 0; i <= 10; i++) {
            arrY[i] = mMaxY / 10 * i;
        }

        mLengthX = arrX.length;
        mLengthY = arrY.length;
    }

    private int getMaxY(String[] arr) {
        int max = Integer.parseInt(arr[0]);
        for (int i = 0; i < arr.length; i++) {
            if (i + 1 < arr.length - 1 && max < Integer.parseInt(arr[i + 1])) {
                max = Integer.parseInt(arr[i + 1]);
            }
            continue;
        }

        int a = max / 10;
        int b = max % 10;
        if (b > 0) {
            max = (a + 1) * 10;
        } else {
            max = a * 10;
        }
        if(max<10){
            max = 10;
        }
        return max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStrokeWidth(mTextSize * mPx);
        mPaint.setColor(getResources().getColor(line1_color));
        canvas.drawPoint(mOriginalX, 20 * mPx, mPaint);
        mPaint.setColor(getResources().getColor(line2_color));
        canvas.drawPoint(mOriginalX + 20 * mPx + 5 * mTextSize * mPx, 20 * mPx, mPaint);

        mPaint.setTextSize(mTextSize * mPx);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(getResources().getColor(line1_color));
        canvas.drawText("总点击数", mOriginalX + 10 * mPx, 25 * mPx, mPaint);
        mPaint.setColor(getResources().getColor(line2_color));
        canvas.drawText("计费点击", mOriginalX + 30 * mPx + 5 * mTextSize * mPx, 25 * mPx, mPaint);

        mPaint.setColor(getResources().getColor(axis_color));
        mPaint.setStrokeWidth(4);
        //画Y轴
//        drawLine(canvas, new Point(mOriginalX, getHeight() - mOriginalY), new Point(mOriginalX, getPaddingTop()), mPaint);
        //画X轴
        drawLine(canvas, new Point(mOriginalX, getHeight() - mOriginalY), new Point(getWidth() - getPaddingRight(), getHeight() - mOriginalY), mPaint);
        mTotalX = (int) (getWidth() - getPaddingRight() - getPaddingLeft() - 2 * mTextSize * mPx);
        mTotalY = (int) (getHeight() - getPaddingBottom() - getPaddingTop() - mTextSize * mPx);

        drawText(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(line1_fill_color));
        drawClickNumLine(canvas, clickNum, mPaint, line1_color);
        drawPoint(canvas);

        mPaint.setColor(getResources().getColor(line2_fill_color));
        drawClickNumLine(canvas, clickNum2, mPaint, line2_color);
        drawPoint(canvas);
    }

    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < mPointList.size(); i++) {
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(mPointList.get(i).getX(), mPointList.get(i).getY(), 10, mPaint);
        }
    }

    private void drawClickNumLine(Canvas canvas, String[] clickNum, Paint paint, int color) {
        int length = clickNum.length;
        mPointList.clear();
        Point startP = null;
        Point endP = null;
        Path path = new Path();

        for (int i = 0; i < length; i++) {
            if (i == 0) {
                startP = new Point(mOriginalX, getHeight() - mOriginalY);
                endP = new Point(mOriginalX + (mTotalX - mRightX) * (i + 1) / mLengthX,
                        getHeight() - mOriginalY - (mTotalY - mTopY) * Integer.parseInt(clickNum[i]) / (mLengthY * mMaxY / 10));
                path.moveTo(startP.getX(), startP.getY());
            } else if (i < length - 1) {
                Point startP2 = new Point(mOriginalX + (mTotalX - mRightX) * i / mLengthX,
                        getHeight() - mOriginalY - (mTotalY - mTopY)  * Integer.parseInt(clickNum[i - 1]) / (mLengthY * mMaxY / 10));
                endP = new Point(mOriginalX + (mTotalX - mRightX) * (i + 1) / mLengthX,
                        getHeight() - mOriginalY - (mTotalY - mTopY)  * Integer.parseInt(clickNum[i]) / (mLengthY * mMaxY / 10));
                path.lineTo(startP2.getX(), startP2.getY());
            } else if (i == length - 1) {
                Point startP2 = new Point(mOriginalX + (mTotalX - mRightX) * i / mLengthX,
                        getHeight() - mOriginalY - (mTotalY - mTopY) * Integer.parseInt(clickNum[i - 1])/ (mLengthY * mMaxY / 10));
                endP = new Point(mOriginalX + (mTotalX - mRightX) * (i + 1) / mLengthX,
                        getHeight() - mOriginalY - (mTotalY - mTopY)  * Integer.parseInt(clickNum[i]) / (mLengthY * mMaxY / 10));
                path.lineTo(startP2.getX(), startP2.getY());
                path.lineTo(endP.getX(), endP.getY());
                path.lineTo(endP.getX() + 1000, endP.getY());
                path.lineTo(endP.getX() + 1000, startP.getY());
            }
            mPointList.add(endP);
        }
        canvas.drawPath(path, paint);

        mPaint.setStrokeWidth(8);
        mPaint.setColor(getResources().getColor(color));
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, mPaint);
    }

    /**
     * xy轴文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        mPaint.setTextSize(mTextSize * mPx);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mLengthX; i++) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(arrX[i], mOriginalX + (mTotalX - mRightX) * (i + 1) / mLengthX,
                    getHeight() - mOriginalY + mTextSize * mPx + mTextDix * mPx, mPaint);

//            drawLine(canvas, new Point(mOriginalX + (mTotalX - mRightX) * (i + 1) / mLengthX, getHeight() - mOriginalY),
//                    new Point(mOriginalX + (mTotalX - mRightX) * (i + 1) / mLengthX, getPaddingTop()), mPaint);
        }

        for (int i = 0; i < mLengthY; i++) {
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(arrY[i]), mOriginalX - mGapY, getHeight() - mOriginalY - (mTotalY - mTopY) * i / mLengthY + mTextDix * mPx / 2, mPaint);

            if(i!= mLengthY-1){
                drawLine(canvas, new Point(mOriginalX, getHeight() - mOriginalY - (mTotalY - mTopY) * (i + 1) / mLengthY),
                        new Point(getWidth() - getPaddingRight(), getHeight() - mOriginalY - (mTotalY - mTopY) * (i + 1) / mLengthY), mPaint);
            }
        }

    }

    private void drawLine(Canvas canvas, Point startP, Point endP, Paint paint) {
        canvas.drawLine(startP.getX(), startP.getY(), endP.getX(), endP.getY(), paint);
    }

}
