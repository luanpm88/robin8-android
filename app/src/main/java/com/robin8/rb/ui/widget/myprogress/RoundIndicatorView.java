package com.robin8.rb.ui.widget.myprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.robin8.rb.R;


/**
 Created by zc on 2017/07/13.
 圆形仪表盘 */

public class RoundIndicatorView extends View {

    private int mWidth;
    private int mHeight;
    private int diameter = 20;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 180;
    private float sweepAngle = 270;
    private int need = 9;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(26);//当前进度
    private float hintSize = dipToPx(11);
    private float curSpeedSize = dipToPx(16);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);

    private String hintColor = "#969696";
    private String longDegreeColor = "#f3eddf";
    private String shortDegreeColor = "#f3eddf";
    private String bgArcColor = "#f3eddf";
    private String titleString;
    private String hintString;
    private String hintStringTwo;

    private boolean isShowCurrentSpeed = true;
    //   private boolean isNeedPhoto;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedContent;
    private boolean isNeedContentTwo;

    // sweepAngle / maxValues 的值
    private float k;

    public RoundIndicatorView(Context context) {
        super(context, null);
        initView();
    }

    public RoundIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initCofig(context, attrs);
        initView();
    }

    public RoundIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCofig(context, attrs);
        initView();
    }

    /**
     初始化布局配置
     @param context
     @param attrs
     */
    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundIndicatorView);
        int color1 = a.getColor(R.styleable.RoundIndicatorView_front_color1, Color.GREEN);
        int color2 = a.getColor(R.styleable.RoundIndicatorView_front_color2, color1);
        int color3 = a.getColor(R.styleable.RoundIndicatorView_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};

        sweepAngle = a.getInteger(R.styleable.RoundIndicatorView_total_engle, 270);
        startAngle = a.getInteger(R.styleable.RoundIndicatorView_start_engle, 135);
       need = a.getInteger(R.styleable.RoundIndicatorView_need, 9);
        bgArcWidth = a.getDimension(R.styleable.RoundIndicatorView_back_width, dipToPx(2));
        progressWidth = a.getDimension(R.styleable.RoundIndicatorView_front_width, dipToPx(10));
        // isNeedPhoto = a.getBoolean(R.styleable.RoundIndicatorView_is_need_photo, false);
        isNeedTitle = a.getBoolean(R.styleable.RoundIndicatorView_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.RoundIndicatorView_is_need_content, false);
        isNeedContentTwo = a.getBoolean(R.styleable.RoundIndicatorView_is_need_content_two, false);
        isNeedUnit = a.getBoolean(R.styleable.RoundIndicatorView_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.RoundIndicatorView_is_need_dial, false);
        hintString = a.getString(R.styleable.RoundIndicatorView_string_unit);
        hintStringTwo = a.getString(R.styleable.RoundIndicatorView_string_unit);
        titleString = a.getString(R.styleable.RoundIndicatorView_string_title);
        curValues = a.getFloat(R.styleable.RoundIndicatorView_current_value, 0);
        maxValues = a.getFloat(R.styleable.RoundIndicatorView_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {

        diameter = 4 * getScreenWidth() / need;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);
        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.parseColor(hintColor));
        hintPaint.setTextAlign(Paint.Align.CENTER);
        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
     //   vTextPaint.setFakeBoldText(true);
      //  vTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        curSpeedPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        curSpeedPaint.setColor(Color.parseColor("#212121"));
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);
        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.parseColor("#212121"));
        vTextPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
     //   vTextPaint.setFakeBoldText(true);//设置粗体
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);
        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2, centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 - shortdegree, degreePaint);
                }

                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        //设置渐变色
        rotateMatrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);
        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY - textSize*0.7f, vTextPaint);
        }
        if (isNeedContentTwo){
            canvas.drawText(String.format("%.0f", curValues),centerX,centerY,vTextPaint);
            canvas.drawText(titleString, centerX, centerY+curSpeedSize*1.5f, curSpeedPaint);
            canvas.drawText(hintString, centerX, centerY+1.9f*textSize, hintPaint);
            canvas.drawText(hintStringTwo, centerX, centerY+textSize*2.5f, hintPaint);
        }
        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY+curSpeedSize*0.4f, curSpeedPaint);
        }
        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3, hintPaint);
        }
        //当前进度
        canvas.drawArc(bgRect, startAngle, currentAngle, false, progressPaint);
        //        if (isNeedContent) {
        //            canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
        //        }
        //        if (isNeedUnit) {
        //            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3, hintPaint);
        //        }
        //        if (isNeedTitle) {
        //            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3, curSpeedPaint);
        //        }

        invalidate();

    }

    /**
     设置最大值
     @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;
    }

    /**
     设置当前值
     @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    public void setAllText(String title, String detail, String data) {
        if (!TextUtils.isEmpty(title)){
            setTitle(title);
        }
         if (!TextUtils.isEmpty(detail)){
             setUnit(detail);
        }
         if (!TextUtils.isEmpty(data)){
             setUnitTwo(data);
        }
        //  setTextSize(18);

    }

    /**
     设置整个圆弧宽度
     @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     设置进度宽度
     @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     设置速度文字大小
     @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     获取当前的进度
     @return
     */
    public float getCurrent() {
        return curValues;
    }

    /**
     设置单位文字大小
     @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     设置单位文字
     @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    } /**
     设置单位文字2
     @param hintStringTwo
     */
    public void setUnitTwo(String hintStringTwo) {
        this.hintStringTwo = hintStringTwo;
        invalidate();
    }

    /**
     设置直径大小
     @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     设置标题
     @param title
     */
    public void setTitle(String title) {
        this.titleString = title;
    }

    /**
     设置是否显示标题
     @param isNeedTitle
     */
    private void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     设置是否显示单位文字
     @param isNeedUnit
     */
    private void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     设置是否显示外部刻度盘
     @param isNeedDial
     */
    private void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**
     为进度设置动画
     @param last
     @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                curValues = currentAngle / k;
            }
        });
        progressAnimator.start();
    }

    /**
     dip 转换成px
     @param dip
     @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : - 1));
    }

    /**
     得到屏幕宽度
     @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);

            Canvas canvas = new Canvas(bitmap);

            // canvas.setBitmap(bitmap);

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2, sbmp.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        sbmp.recycle();
        return output;
    }
}
