package com.robin8.rb.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.create.model.ProductPopItem;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.UIUtils;

import java.util.List;


/**
 * 输入法特殊的弹出框：点击弹出框外部的区域关闭弹出框�?
 *
 * @author figo
 */
public class ProductPopupWindow extends PopupWindow {

    private List<ProductPopItem> mDataList;
    protected Activity mContext;

    /**
     * 父视图
     */
    protected View mParent;

    /**
     * 视图尺寸的宽度测量模式�?
     */
    protected int mWidthMeasureSpecMode;

    /**
     * 视图尺寸的高度测量模式�?
     */
    protected int mHeightMeasureSpecMode;

    /**
     * 弹出框显示和�?��的定时器
     */
    protected PopupWindowTimer mPopupWindowTimer;
    protected View mContentView;
    Animation animation;
    Animation alaphAnimation;
    Animation inanimation;
    Animation inalaphAnimation;

    DismissListener listener;
    private View mContentll;
    private View mShadell;
    private GridView mGridView;
    private LayoutInflater mLayoutInflater;
    private ListView mListView;
    private OnItemSelectListener onItemSelectListener;


    public ProductPopupWindow(Activity context, View parent, int widthMeasureSpecMode, int heightMeasureSpecMode, List<ProductPopItem> list) {
        super(context);
        this.mDataList = list;
        mContext = context;
        mParent = parent;
        mWidthMeasureSpecMode = widthMeasureSpecMode;
        mHeightMeasureSpecMode = heightMeasureSpecMode;
        mPopupWindowTimer = new PopupWindowTimer();

        setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        setBackgroundDrawable(new ColorDrawable(0));
        setOutsideTouchable(false);
        setFocusable(false);

        mContentView = onCreateContentView();
        setContentView(mContentView);
        setWidth(DensityUtils.getScreenWidth(mContext));
        LogUtil.logXXfigo(DensityUtils.getScreenHeight(mContext) +"  getStatusBarHeight =   " +   DensityUtils.getStatusHeight(mContext));
        setHeight(DensityUtils.getScreenHeight(mContext) - UIUtils.dip2px(80) - getStatusHeight(mContext));
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface DismissListener {
        public void onDialogDismiss();
    }

    public void setOnDismissListener(DismissListener listener) {
        this.listener = listener;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void fullScreen() {
        // 获取屏幕大小
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        int screenWidth = d.getWidth();
        int screenHeight = d.getHeight();

        setWidth(screenWidth);

//        mRootViewRect.set(0, UIUtils.dip2px(80) + getStatusBarHeight(mContext), screenWidth, screenHeight);
    }

    /**
     * 根据测量模式获取宽度和高度的测量方式
     *
     * @param measureSpecMode
     * @return
     */
    private int getParamsWidthHeight(int measureSpecMode) {
        if (measureSpecMode == MeasureSpec.EXACTLY) {
            return AbsoluteLayout.LayoutParams.MATCH_PARENT;
        } else {
            return AbsoluteLayout.LayoutParams.WRAP_CONTENT;
        }
    }

    public void dismiss(boolean flag) {
        if (listener == null) {
            return;
        }

        if (!flag) {
            listener.onDialogDismiss();
            dismiss();
            return;
        }
        listener.onDialogDismiss();
        Animation animation = getOutAnimation();
        Animation alaphAnimation = getOutAlphaAnimation();
        mContentll.startAnimation(animation);
        mShadell.startAnimation(alaphAnimation);
        animation.setAnimationListener(new dismisAnimationListener());
    }

    private Animation getOutAnimation() {
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_top_out);
            animation.setFillAfter(true);
        }
        return animation;
    }

    private Animation getOutAlphaAnimation() {
        if (alaphAnimation == null) {
            alaphAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_out);
            alaphAnimation.setFillAfter(true);
        }
        return alaphAnimation;
    }

    public interface OnItemSelectListener {
        void onItemSelect(int position);
    }

    class dismisAnimationListener implements AnimationListener {

        @Override
        public void onAnimationEnd(Animation arg0) {
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    dismiss();
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {

        }

        @Override
        public void onAnimationStart(Animation arg0) {
        }

    }


    /**
     * 显示
     *
     * @param locationInParent
     */
    private void show(int locationInParent[]) {
        if (mParent == null) {
            return;
        }
        int parentLocationInWindow[] = {0, 0};
        int parentLocationInScreen[] = {0, 0};

        int statusBarHeight = getStatusHeight(mContext);
        parentLocationInScreen[1] -= statusBarHeight;

        int windowOnScreenX = parentLocationInScreen[0] - parentLocationInWindow[0];
        int windowOnScreenY = parentLocationInWindow[1];
        LogUtil.logXXfigo("statusBarHeight="+statusBarHeight);
        // 把popWindow显示在屏幕的原点
        showAtLocation(mParent, Gravity.LEFT | Gravity.TOP, -windowOnScreenX,   DensityUtils.dp2px(80) + statusBarHeight);
        startInAnimation();
    }

    private void startInAnimation() {
        final Animation inanimation = getInAnimation();
        Animation inalpha = getInAlphaAnimation();
        mContentll.startAnimation(inanimation);
        mShadell.startAnimation(inalpha);
    }

    private Animation getInAlphaAnimation() {
        if (alaphAnimation == null) {
            alaphAnimation = AnimationUtils.loadAnimation(mContext, R.anim.anim_alpha_in);
            alaphAnimation.setFillAfter(true);
        }
        return alaphAnimation;
    }

    private Animation getInAnimation() {
        if (inanimation == null) {
            inanimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_top_in);
            inanimation.setFillAfter(true);
        }
        return inanimation;
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }


    /**
     * 延时显示弹出�? *
     *
     * @param delay 延时的时�? * @param locationInParent 弹出框显示的位置，相对于父视�?
     */
    public void delayedShow(long delay, int locationInParent[]) {
        if (mPopupWindowTimer.isPending()) {
            mPopupWindowTimer.removeTimer();
        }

        if (delay <= 0) {
            show(locationInParent);
        } else {
            mPopupWindowTimer.startTimerShow(delay, locationInParent);
        }
    }

    protected void log(String msg) {
        LogUtil.logXXfigo(msg);
    }

    protected void log(String msg, Throwable e) {
        LogUtil.logXXfigo(msg + e.toString());
    }

    /**
     * 创建内容视图
     */
    protected View onCreateContentView() {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = mLayoutInflater.inflate(R.layout.layout_product_pop, null);
        mShadell = layout.findViewById(R.id.ll_shape);
        mContentll = layout.findViewById(R.id.ll_content);
        mGridView = (GridView) layout.findViewById(R.id.gv_list);
        mListView = (ListView) layout.findViewById(R.id.lv_list);
        mGridView.setNumColumns(3);

        if (mDataList.size() < 12) {
            mListView.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
        }

        mShadell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(true);
            }
        });

        initListView();
        return layout;
    }

    private void initListView() {
        final MyAdapter myAdapter = new MyAdapter();
        mGridView.setAdapter(myAdapter);
        mListView.setAdapter(myAdapter);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(position);
                }

                for (int i = 0; i < mDataList.size(); i++) {
                    if (position == i) {
                        mDataList.get(i).isSelected = true;
                    } else {
                        mDataList.get(i).isSelected = false;
                    }
                }

                myAdapter.notifyDataSetChanged();
            }
        };
        mListView.setOnItemClickListener(onItemClickListener);
        mGridView.setOnItemClickListener(onItemClickListener);
    }

    /**
     * 显示、更新�?隐藏定时�? *
     *
     * @author keanbin
     */
    private class PopupWindowTimer extends Handler implements Runnable {
        public static final int ACTION_SHOW = 1;
        public static final int ACTION_HIDE = 2;

        /**
         * The pending action.
         */
        private int mAction;

        private int mPositionInParent[] = new int[2];

        private boolean mTimerPending = false;

        public void startTimerShow(long time, int positionInParent[]) {
            mAction = ACTION_SHOW;

            mPositionInParent[0] = positionInParent[0];
            mPositionInParent[1] = positionInParent[1];

            postDelayed(this, time);
            mTimerPending = true;
        }


        public void startTimerHide(long time) {
            mAction = ACTION_HIDE;

            postDelayed(this, time);
            mTimerPending = true;
        }

        public boolean isPending() {
            return mTimerPending;
        }

        public boolean removeTimer() {
            if (mTimerPending) {
                mTimerPending = false;
                removeCallbacks(this);
                return true;
            }

            return false;
        }

        public int getAction() {
            return mAction;
        }

        public void run() {
            switch (mAction) {
                case ACTION_SHOW:
                    show(mPositionInParent);
                    break;

                case ACTION_HIDE:
                    dismiss(true);
                    break;
            }
            mTimerPending = false;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public ProductPopItem getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mLayoutInflater.inflate(R.layout.item_product_pop, null);
            TextView nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            nameTv.setText(mDataList.get(position).name);
            nameTv.setSelected(mDataList.get(position).isSelected);
            return convertView;
        }
    }
}
