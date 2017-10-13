package com.robin8.rb.module.social.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.util.LogUtil;

import java.util.List;


/**
 Created by zc on 2017/8/8. */

public class ShowDataView extends View {
    private int center;//中心点
    private float one_radius; //外层菱形圆半径
    private float distance;//多边形之间的间距
    private int defalutSize = 180;//默认大小
    private String[] str = {"时尚", "摄影", "投资", "宠物", "互联网", "旅游", "美妆", "健身"};
    private Rect str_rect;//字体矩形
    private Paint rank_Paint;//各等级进度画笔
    private Paint str_paint;//字体画笔
    private Paint center_paint;//中心线画笔
    private Paint two_paint;//第二层多边形画笔
    private Paint three_paint;//第三层多边形画笔
    private float f1, f2, f3, f4, f5, f6, f7, f8;
    private Float[] f;

    public ShowDataView(Context context) {
        // super(context);
        this(context, null);
        // initview();
    }

    public ShowDataView(Context context, @Nullable AttributeSet attrs) {
        // super(context,attrs);
        this(context, attrs, 0);
        // initCofig(context, attrs);
        // initview();
    }

    public ShowDataView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // initCofig(context, attrs);
        initview();
    }

    //    private void initCofig(Context context, AttributeSet attrs) {
    //     TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShowDataView);
    //        f1 = a.getFloat(R.styleable.ShowDataView_value1, 10f);
    //        f2 = a.getFloat(R.styleable.ShowDataView_value2, 10f);
    //        f3 = a.getFloat(R.styleable.ShowDataView_value3, 10f);
    //        f4 = a.getFloat(R.styleable.ShowDataView_value4, 10f);
    //        f5 = a.getFloat(R.styleable.ShowDataView_value5, 10f);
    //        f6 = a.getFloat(R.styleable.ShowDataView_value6, 10f);
    //        f7 = a.getFloat(R.styleable.ShowDataView_value7, 10f);
    //        f8 = a.getFloat(R.styleable.ShowDataView_value8, 10f);
    //        setValue1(f1);
    //        setValue2(f2);
    //        setValue3(f3);
    //        setValue4(f4);
    //        setValue5(f5);
    //        setValue6(f6);
    //        setValue7(f7);
    //        setValue8(f8);
    //        a.recycle();
    //
    //    }

    private void initview() {
        defalutSize = dp_px(defalutSize);
        //初始化字体画笔
        str_paint = new Paint();
        str_paint.setAntiAlias(true);
        str_paint.setColor(getResources().getColor(R.color.gray_565656));
        str_paint.setTextSize(dp_px(12));
        str_rect = new Rect();
        str_paint.getTextBounds(str[0], 0, str[0].length(), str_rect);

        //初始化各等级进度画笔
        rank_Paint = new Paint();
        rank_Paint.setAntiAlias(true);
        rank_Paint.setColor(getResources().getColor(R.color.yellow_ff8004));
        rank_Paint.setStyle(Paint.Style.FILL);


        //初始化第二层多边形画笔
        two_paint = new Paint();
        two_paint.setAntiAlias(true);
        two_paint.setColor(getResources().getColor(R.color.yellow_ff9b0d));
        two_paint.setStyle(Paint.Style.FILL);//设置空心

        //初始化第三层多边形画笔
        three_paint = new Paint();
        three_paint.setAntiAlias(true);
        three_paint.setColor(getResources().getColor(R.color.gray_565656));
        three_paint.setStyle(Paint.Style.STROKE);//设置空心


        //初始化中心线画笔
        center_paint = new Paint();
        center_paint.setAntiAlias(true);
        center_paint.setColor(getResources().getColor(R.color.gray_565656));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        PaintRank(canvas);
        center(canvas);
        PaintDot(canvas);
        PaintFout(canvas);
        otherPolygons(canvas, 0);
        otherPolygons(canvas, 1);
        otherPolygons(canvas, 2);
        otherPolygons(canvas, 3);
        otherPolygons(canvas, 4);

        // onePolygons(canvas);
        //  twoPolygons(canvas);
    }

    private void otherPolygons(Canvas canvas, int i) {
        distance = one_radius / 5;
        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * str_rect.width() + i * distance);                           //起始点
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 8)) * (one_radius - i * distance)), (float) (getPaddingTop() + 2 * str_rect.width() + (one_radius) - Math.abs(Math.cos(Math.toRadians(360 / 8)) * (one_radius - i * distance))));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 4)) * (one_radius - i * distance)), (float) (Math.cos(Math.toRadians(360 / 4)) * (one_radius - i * distance)) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 8)) * (one_radius - i * distance)), (float) (Math.cos(Math.toRadians(360 / 8)) * (one_radius - i * distance)) + center);
        path.lineTo((float) (center), (float) (one_radius + center - i * distance));
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 8)) * (one_radius - i * distance)), (float) (Math.cos(Math.toRadians(360 / 8)) * (one_radius - i * distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 4)) * (one_radius - i * distance)), (float) (Math.cos(Math.toRadians(360 / 4)) * (one_radius - i * distance)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 8)) * (one_radius - i * distance)), (float) (getPaddingTop() + 2 * str_rect.width() + (one_radius) - Math.abs(Math.cos(Math.toRadians(360 / 8)) * (one_radius - i * distance))));
        path.close();
        canvas.drawPath(path, three_paint);
    }

    /**
     绘制等级进度
     */
    private void PaintRank(Canvas canvas) {

        Path path = new Path();
        path.moveTo(center, getPaddingTop() + 2 * str_rect.width() + f1);//起始点
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 8)) * (one_radius - f2)), (float) (getPaddingTop() + 2 * str_rect.width() + (one_radius) - Math.abs(Math.cos(Math.toRadians(360 / 8)) * (one_radius - f2))));
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 4)) * (one_radius - f3)), (float) (Math.cos(Math.toRadians(360 / 4)) * (one_radius - f3)) + center);
        path.lineTo((float) (center + Math.sin(Math.toRadians(360 / 8)) * (one_radius - f4)), (float) (Math.cos(Math.toRadians(360 / 8)) * (one_radius - f4)) + center);
        path.lineTo((float) (center), (float) (one_radius + center - f5));
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 8)) * (one_radius - f6)), (float) (Math.cos(Math.toRadians(360 / 8)) * (one_radius - f6)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 4)) * (one_radius - f7)), (float) (Math.cos(Math.toRadians(360 / 4)) * (one_radius - f7)) + center);
        path.lineTo((float) (center - Math.sin(Math.toRadians(360 / 8)) * (one_radius - f8)), (float) (getPaddingTop() + 2 * str_rect.width() + (one_radius) - Math.abs(Math.cos(Math.toRadians(360 / 8)) * (one_radius - f8))));
        path.close();
        canvas.drawPath(path, rank_Paint);

    }

    /**
     绘制圆点
     */
    private void PaintDot(Canvas canvas) {
        Path path = new Path();
        path.addCircle(center, getPaddingTop() + 2 * str_rect.width() + f1, dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center + Math.sin(Math.toRadians(360 / 8)) * (one_radius - f2)), (float) (getPaddingTop() + 2 * str_rect.width() + (one_radius) - Math.abs(Math.cos(Math.toRadians(360 / 8)) * (one_radius - f2))), dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center + Math.sin(Math.toRadians(360 / 4)) * (one_radius - f3)), (float) (Math.cos(Math.toRadians(360 / 4)) * (one_radius - f3)) + center, dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center + Math.sin(Math.toRadians(360 / 8)) * (one_radius - f4)), (float) (Math.cos(Math.toRadians(360 / 8)) * (one_radius - f4)) + center, dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center), (float) (one_radius + center - f5), dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center - Math.sin(Math.toRadians(360 / 8)) * (one_radius - f6)), (float) (Math.cos(Math.toRadians(360 / 8)) * (one_radius - f6)) + center, dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center - Math.sin(Math.toRadians(360 / 4)) * (one_radius - f7)), (float) (Math.cos(Math.toRadians(360 / 4)) * (one_radius - f7)) + center, dp_px(2), Path.Direction.CCW);
        path.addCircle((float) (center - Math.sin(Math.toRadians(360 / 8)) * (one_radius - f8)), (float) (getPaddingTop() + 2 * str_rect.width() + (one_radius) - Math.abs(Math.cos(Math.toRadians(360 / 8)) * (one_radius - f8))), dp_px(2), Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, two_paint);

    }


    /**
     绘制字体
     @param canvas
     */
    private void PaintFout(Canvas canvas) {
        canvas.drawText(str[0], center - str_rect.width() / 2, (float) (getPaddingTop() + 1.5 * str_rect.width()), str_paint);

        canvas.drawText(str[1], (float) (center + Math.sin(Math.toRadians(360 / 8)) * one_radius + str_rect.width() / 2), (float) ((getPaddingTop() + 2 * str_rect.width() + one_radius - Math.abs(Math.cos(Math.toRadians(360 / 8)) * one_radius))), str_paint);

        canvas.drawText(str[2], (float) (center + Math.sin(Math.toRadians(360 / 4)) * one_radius + str_rect.width() / 2), (float) (Math.cos(Math.toRadians(360 / 4)) * one_radius) + center + str_rect.height() / 4, str_paint);

        canvas.drawText(str[3], (float) (center + Math.sin(Math.toRadians(360 / 6)) * one_radius), (float) ((Math.cos(Math.toRadians(360 / 6)) * one_radius) + center + str_rect.width()), str_paint);

        canvas.drawText(str[5], (float) (center - Math.sin(Math.toRadians(360 / 6)) * one_radius - str_rect.width()), (float) ((Math.cos(Math.toRadians(360 / 6)) * one_radius) + center + str_rect.width()), str_paint);

        canvas.drawText(str[6], (float) (center - Math.sin(Math.toRadians(360 / 4)) * one_radius - 1.5 * str_rect.width()), (float) (Math.cos(Math.toRadians(360 / 4)) * one_radius) + center + str_rect.height() / 4, str_paint);

        canvas.drawText(str[7], (float) (center - Math.sin(Math.toRadians(360 / 8)) * one_radius - 1.5 * str_rect.width()), (float) ((getPaddingTop() + 2 * str_rect.width() + one_radius - Math.abs(Math.cos(Math.toRadians(360 / 8)) * one_radius))), str_paint);

        canvas.drawText(str[4], center - str_rect.width() / 2, (float) (one_radius + center + str_rect.width()), str_paint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (wMode == MeasureSpec.EXACTLY) {
            width = wSize;
        } else {
            width = Math.min(wSize, defalutSize);
        }

        if (hMode == MeasureSpec.EXACTLY) {
            height = hSize;
        } else {
            height = Math.min(hSize, defalutSize);
        }
        center = width / 2;//中心点
        one_radius = center - getPaddingTop() - 2 * str_rect.width();
        f1 = one_radius - one_radius / 5 * 1;
        f2 = one_radius - one_radius / 5 * 1;
        f3 = one_radius - one_radius / 5 * 1;
        f4 = one_radius - one_radius / 5 * 1;
        f5 = one_radius - one_radius / 5 * 1;
        f6 = one_radius - one_radius / 5 * 1;
        f7 = one_radius - one_radius / 5 * 1;
        f8 = one_radius - one_radius / 5 * 1;
        setMeasuredDimension(width, height);
    }

    /**
     绘制中心线
     @param canvas
     */
    private void center(Canvas canvas) {
        //绘制七边形中心线
        canvas.save();//保存当前状态
        canvas.rotate(0, center, center);
        //  float startY = getPaddingTop() + 2*str_rect.width();
        float startY = 2 * str_rect.width();
        float endY = center;
        float du = (float) (360 / 8);
        for (int i = 0; i < 8; i++) {
            canvas.drawLine(center, startY, center, endY, center_paint);
            canvas.rotate(du, center, center);

        }
        canvas.restore();//恢复之前状态
    }

    /**
     dp转px
     @param values
     @return
     */
    public int dp_px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }


    public void setValue(List<Float> list) {
        f = new Float[8];
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 0) {
                f[i] = one_radius-one_radius/5*0.1f;
            } else if (list.get(i) >= 100) {
                f[i] = one_radius - one_radius / 5 * 4.99f;
            } else {
                f[i] = (one_radius - one_radius / 5 * (list.get(i)/20));
            }
            //            if ((one_radius - one_radius / 5 * (list.get(i))) <= 0) {
            //                f[i] = one_radius - one_radius*(list.get(i)/100);
            //            } else if (list.get(i) == 0) {
            //                //  f[i] = one_radius - one_radius / 5 * 1;
            //                f[i] = one_radius;
            //            } else {
            //                f[i] = one_radius - one_radius / 5 * list.get(i);
            //            }
        }
        f1 = f[0];
        f2 = f[1];
        f3 = f[2];
        f4 = f[3];
        f5 = f[4];
        f6 = f[5];
        f7 = f[6];
        f8 = f[7];
        invalidate();
        //   postInvalidate();
    }

    public void setValue1(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f1 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f1 = one_radius;
        } else {
            f1 = one_radius - one_radius / 5 * value;
        }
        LogUtil.LogShitou("我真的想知道走了没有1", "===>" + value);
        LogUtil.LogShitou("我真的想知道走了没有", "===>" + f1);

        invalidate();
    }

    public void setValue2(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f2 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            f2 = one_radius;
        } else {
            f2 = one_radius - one_radius / 5 * value;
        }
        LogUtil.LogShitou("我真的想知道走了没有1", "===>" + value);
        LogUtil.LogShitou("我真的想知道走了没有", "===>" + f2);
        invalidate();
        // postInvalidate();
    }

    public void setValue3(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f3 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f3 = one_radius;
        } else {
            f3 = one_radius - one_radius / 5 * value;
        }
        LogUtil.LogShitou("我真的想知道走了没有", "===>" + f3);
        // postInvalidate();
        invalidate();
    }

    public void setValue4(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f4 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f4 = one_radius;
        } else {
            f4 = one_radius - one_radius / 5 * value;
        }
        LogUtil.LogShitou("我真的想知道走了没有", "===>" + f4);
        invalidate();
        //postInvalidate();
    }

    public void setValue5(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f5 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f5 = one_radius;
        } else {
            f5 = one_radius - one_radius / 5 * value;
        }
        LogUtil.LogShitou("我真的想知道走了没有", "===>" + f5);

        invalidate();
    }

    public void setValue6(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f6 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f6 = one_radius;
        } else {
            f6 = one_radius - one_radius / 5 * value;
        }
        invalidate();
    }

    public void setValue7(float value) {

        if ((one_radius - one_radius / 5 * value) <= 0) {
            f7 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f7 = one_radius;
        } else {
            f7 = one_radius - one_radius / 5 * value;
        }
        invalidate();
    }

    public void setValue8(float value) {
        if ((one_radius - one_radius / 5 * value) <= 0) {
            f8 = one_radius - one_radius / 5 * 4.99f;
        } else if (value == 0) {
            //  f[i] = one_radius - one_radius / 5 * 1;
            f8 = one_radius;
        } else {
            f8 = one_radius - one_radius / 5 * value;
        }
        invalidate();
    }

    public void setStr(String[] str) {
        this.str = str;
        invalidate();
    }


}
