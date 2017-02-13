package com.robin8.rb.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.robin8.rb.R;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Figo
 */
public class ClipImageActivity extends Activity implements View.OnClickListener {

    public static final int NONE = 0;
    public static final int DRAG = 1;
    public static final int ZOOM = 2;

    ImageView img;
    PointF startF;
    PointF midF;
    float oldDist;
    int mode;
    private Matrix matrixCurrent;
    private Matrix matrixOld;
    float zoomMin;
    float zoomMax;
    float scaleCurrent;
    float scaleOld;
    int screenWidth;
    int screenHeight;
    int imgWidth;
    int imgHeight;
    int startImgWidth;
    int startImgHeight;
    Activity mActivity;
    private LinearLayout mLLContent;
    private File tempFile;
    private String pathName;
    private Bitmap mBitmap;
    private int mStatusBarHeights;
    private View mView;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        mActivity = this;
        initView();
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        getScreenSize();
        imgWidth = screenWidth;
        imgHeight = screenHeight;
        startImgWidth = screenWidth;
        startImgHeight = screenHeight;
        img = (ImageView) findViewById(R.id.img);
        mLLContent = (LinearLayout) findViewById(R.id.ll_content);

        mView = new View(this.getApplicationContext());
        mView.setBackgroundResource(R.drawable.shape_bg_clip_img);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, screenWidth * 9 / 16, Gravity.CENTER);
        mLLContent.addView(mView, params);

        View tvCancel = findViewById(R.id.tv_cancel);
        View tvConfirm = findViewById(R.id.tv_confirm);

        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            pathName = intent.getStringExtra("uri");
            mBitmap = BitmapUtil.decodeSampledBitmapFromResource(pathName, screenWidth, screenHeight * 9 / 16);
            img.setImageBitmap(mBitmap);
        }
        initData();
    }

    private void initData() {
        startF = new PointF();
        midF = new PointF();
        oldDist = 0;
        mode = NONE;
        matrixCurrent = new Matrix();
        matrixOld = new Matrix();
        zoomMin = 1f;
        zoomMax = 3f;
        scaleCurrent = 1;
        scaleOld = 1;
    }


    public void clip(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        mStatusBarHeights = rect.top;

        mView.setVisibility(View.INVISIBLE);

        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);

        // 去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, screenWidth, screenHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, (screenHeight - screenWidth * 9 / 16 ) / 2 , screenWidth, screenWidth * 9 / 16);
        // 销毁缓存信息
        view.destroyDrawingCache();
        img.setImageBitmap(bitmap);
        mView.setVisibility(View.VISIBLE);
        try {
             mPath = FileUtils.saveBitmapToSD(bitmap,"temp"+ SystemClock.currentThreadTimeMillis());
        } catch (IOException e) {
        }
        Intent intent = getIntent();
        intent.putExtra("path",mPath);
        setResult(SPConstants.RESULT_CLIP,intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.e("", "----point----1-----down-----");
                startF.set(event.getX(), event.getY());
                matrixOld.set(matrixCurrent);
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mode == DRAG) {
                    matrixOld.set(matrixCurrent);

                    oldDist = spacing(event);
                    Log.e("", "----point----2-----down----->" + oldDist);
                    if (oldDist > 20f) {
                        midPoint(midF, event);
                        scaleOld = scaleCurrent;
                        mode = ZOOM;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mode) {
                    case DRAG:
                        //img
                        float x = event.getX() - startF.x;
                        float y = event.getY() - startF.y;
                        matrixCurrent.set(matrixOld);
                        matrixCurrent.postTranslate(x, y);
                        break;
                    case ZOOM:
                        float crrentDist = spacing(event);
                        if (crrentDist < 20f) {
                            break;
                        }

                        scaleCurrent = scaleOld;

                        float needScaleMin = zoomMin / scaleCurrent;
                        float needScaleMax = zoomMax / scaleCurrent;

                        float scale = crrentDist / oldDist;

                        scaleCurrent *= scale;

                        if (scaleCurrent < zoomMin) {
                            scale = needScaleMin;
                            scaleCurrent = zoomMin;
                        }
                        if (scaleCurrent > zoomMax) {
                            scale = needScaleMax;
                            scaleCurrent = zoomMax;
                        }

                        imgWidth = (int) (scaleCurrent * startImgWidth);

                        //img
                        matrixCurrent.set(matrixOld);
                        matrixCurrent.postScale(scale, scale, midF.x, midF.y);
                        break;
                }

                break;

        }
        img.setImageMatrix(matrixCurrent);
        return super.onTouchEvent(event);
    }

    private float spacing(MotionEvent event) {
        try {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float)Math.sqrt(x * x + y * y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_confirm:
                clip(this);
                break;
        }
    }

}
