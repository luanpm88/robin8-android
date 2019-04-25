package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.util.CacheUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshListView extends ListView {

    public static final int LOAD_MORE_FAIL = 0;//失败
    public static final int LOAD_MORE_SUCCESS = 1;// 成功
    public static final int LOAD_MORE_NO_DATA = 2;// 无更多数据
    public static final int LOAD_EMPTY = 3;// 数据为空

    private static final int STATE_IDLE = 0;
    private static final int PULL_DOWN_REFRESH = 1;// 下拉刷新状态
    private static final int RELEASE_REFRESH = 2;// 松开刷新状态
    private static final int REFRESHING = 3;// 正在刷新状态
    private static int CURRENT_STATE = STATE_IDLE;// 当前下拉刷新的状态

    private Context mContext;

    private int downX = 0;
    private int downY = -1;

    private int mLastX = 0;
    private int mLastY = 0;
    private int header_height;// 刷新头布局的高度
    private View refresh_header;// 刷新头布局
    private View secondHeader;// 记录第二个头布局
    private ImageView refresh_arr;
    private ImageView iv_progress;
    private TextView refresh_state;
    private TextView refresh_updatetime;
    private RotateAnimation up;
    private RotateAnimation down;
    private OnRefreshListener mListener;// 记录外接传递进来的 刷新监听器
    private View footer_view;// 上啦加载布局
    private boolean isLoadMore;// 是否正在上拉加载状态
    private int footer_height;
    private ImageView pb_load_more;
    private TextView tv_load_more;

    private View ll_nomore;
    private View ll_load_more;
    private AnimationDrawable mAnimationUp;
    private AnimationDrawable mAnimationDown;

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeader();
        initFooter();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initFooter();
    }

    public RefreshListView(Context context) {
        super(context);
        this.mContext = context;
        initHeader();
        initFooter();
    }

    // 初始化上拉加载界面
    private void initFooter() {
        footer_view = View.inflate(mContext, R.layout.refrech_footer, null);
        pb_load_more = (ImageView) footer_view.findViewById(R.id.iv_progress);
        tv_load_more = (TextView) footer_view.findViewById(R.id.tv_load_more);
        ll_load_more = footer_view.findViewById(R.id.ll_load_more);
        ll_nomore = footer_view.findViewById(R.id.ll_nomore);

        footer_view.measure(0, 0);
        footer_height = footer_view.getMeasuredHeight();
        addFooterView(footer_view);
        this.setOnScrollListener(new MyOnScrollListener());

        tv_load_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onLoadingMore();
                }
            }
        });
    }

    // 初始化下拉刷新头
    private void initHeader() {
        initAnimation();
        refresh_header = View.inflate(getContext(), R.layout.refresh_header, null);
        refresh_arr = (ImageView) refresh_header.findViewById(R.id.iv_refresh_arr);
        iv_progress = (ImageView) refresh_header.findViewById(R.id.iv_progress);
        refresh_state = (TextView) refresh_header.findViewById(R.id.tv_refresh_state);
        refresh_updatetime = (TextView) refresh_header.findViewById(R.id.tv_refresh_updatetime);
        addHeaderView(refresh_header);
        refresh_header.measure(0, 0);// 测量宽高
        header_height = refresh_header.getMeasuredHeight();
        refresh_header.setPadding(0, -header_height, 0, 0);
    }

    public void addSecondHeader(View view) {
        this.secondHeader = view;
        addHeaderView(view);
    }

    private void initAnimation() {
        up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        up.setDuration(500);
        up.setFillAfter(true);

        down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        down.setDuration(500);
        down.setFillAfter(true);

        mAnimationUp = new AnimationDrawable();
        mAnimationDown = new AnimationDrawable();
        setAnimation(mAnimationUp);
        setAnimation(mAnimationDown);
    }

    private void setAnimation(AnimationDrawable mAnimation) {
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_01), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_02), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_03), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_04), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_05), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_06), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_07), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_08), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_09), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_10), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_11), 50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_12), 50);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (downY == -1) {// 如果down事件被拦截，重新给downY赋值
                    downY = (int) ev.getY();
                }
                int moveY = (int) ev.getY();
                int diffY = moveY - downY;
                // 如果正在刷新就break
                if (CURRENT_STATE == REFRESHING) {
                    break;
                }
                if (!isSecondHeaderDisplay()) {
                    break;
                }

                if (diffY > 0) {
                    int diffPanding = diffY - header_height;// 根据手指移动的距离
                    // 计算出头布局的新的Padding值
                    // 下拉刷新的头布局没有完全显示 切换到下拉刷新的状态
                    if (diffPanding < 0 && CURRENT_STATE != PULL_DOWN_REFRESH) {
                        CURRENT_STATE = PULL_DOWN_REFRESH;
                        System.out.println("进入下拉刷新状态");
                        updateRefreshState(CURRENT_STATE);
                    }
                    // 下拉刷新头布局 完全显示了，切换到松开刷新的状态
                    else if (diffPanding > 0 && CURRENT_STATE != RELEASE_REFRESH) {
                        CURRENT_STATE = RELEASE_REFRESH;
                        System.out.println("进入松开刷新状态");
                        updateRefreshState(CURRENT_STATE);
                    }

                    refresh_header.setPadding(0, diffPanding, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downY = -1;
                // 用户松开手指时，如果当前是下拉刷新状态，就是下拉刷新布局没有完全拉出来，不去刷新数据
                if (CURRENT_STATE == PULL_DOWN_REFRESH) {
                    System.out.println("进入初始刷新状态");
                    refresh_header.setPadding(0, -header_height, 0, 0);
                    CURRENT_STATE = STATE_IDLE;
                }
                // 用户松开手指时，如果当前是松开刷新状态，切换为正在刷新状态，需要去服务器重新访问数据
                else if (CURRENT_STATE == RELEASE_REFRESH) {
                    System.out.println("进入正在刷新状态");
                    CURRENT_STATE = REFRESHING;
                    refresh_header.setPadding(0, 0, 0, 0);// 让下拉刷新头布局 完全显示
                    updateRefreshState(CURRENT_STATE);
                    if (mListener != null) {
                        mListener.onRefreshing();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private boolean isSecondHeaderDisplay() {
        int[] location = new int[2];
        if (location[0]==0 && location[1]==0){
            return true;
        }else {
            secondHeader.getLocationInWindow(location);
            int secondHeaderY = location[1];// 第二个头布局的高度位置
            this.getLocationInWindow(location);
            int listviewY = location[1];// 获取Listview的高度位置
            return secondHeaderY >= listviewY;
        }
    }

    // 外界通知我已经完成服务服务器的刷新动作
    public void setRefreshFinshed(boolean success) {
        if (success) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
            long time = CacheUtils.getLong(BaseApplication.getContext(), SPConstants.TAG_REFRESH_TIME, System.currentTimeMillis());
            refresh_updatetime.setText("最后更新：" + dateFormat.format(new Date(time)));
        }
        CURRENT_STATE = STATE_IDLE;
        moveLinePath(0, -header_height, new AccelerateInterpolator(0.6f), true, 350);
    }

    private void moveLinePath(int x, int y, android.view.animation.Interpolator interpolator,
                              final boolean needListener, int duration) {

        final ValueAnimator xValue = ValueAnimator.ofInt(x, y);
        xValue.setDuration(duration);
        xValue.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (Integer) animation.getAnimatedValue();
                refresh_header.setPadding(0, h, 0, 0);
                if (needListener && CURRENT_STATE != STATE_IDLE) {
                    xValue.cancel();
                }
            }
        });

        xValue.setInterpolator(interpolator);
        xValue.start();
    }

    // 外界通知我已经完成服务服务器的加载更多动作
    public void setLoadMoreFinished(int state) {
        isLoadMore = false;// 下次可以继续加载更多
        switch (state) {
            case LOAD_MORE_FAIL:
                footer_view.setPadding(0, 0, 0, 0);
                pb_load_more.setVisibility(View.GONE);
                ll_nomore.setVisibility(View.GONE);
                ll_load_more.setVisibility(View.VISIBLE);
                tv_load_more.setText(BaseApplication.getContext().getResources().getText(R.string.no_net_click_reload));
                break;
            case LOAD_MORE_SUCCESS:
                ll_nomore.setVisibility(View.GONE);
                ll_load_more.setVisibility(View.VISIBLE);
                footer_view.setPadding(0, -footer_height, 0, 0);// 隐藏上拉加载布局
                break;
            case LOAD_MORE_NO_DATA:
                footer_view.setPadding(0, 0, 0, 0);
                ll_nomore.setVisibility(View.GONE);
                ll_load_more.setVisibility(View.VISIBLE);
                pb_load_more.setVisibility(View.GONE);
                tv_load_more.setText(BaseApplication.getContext().getResources().getText(R.string.no_more_data));
                isLoadMore = true;
                break;
            case LOAD_EMPTY:
                footer_view.setPadding(0, 0, 0, 0);
                ll_nomore.setVisibility(View.VISIBLE);
                ll_load_more.setVisibility(View.GONE);
                break;
        }
    }

    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefreshing();

        void onLoadingMore();
    }

    private void updateRefreshState(int state) {
        switch (state) {
            case PULL_DOWN_REFRESH:
                refresh_arr.setVisibility(View.VISIBLE);
                iv_progress.setVisibility(View.INVISIBLE);
                refresh_state.setText("下拉可以刷新");
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
                long time = CacheUtils.getLong(BaseApplication.getContext(), SPConstants.TAG_REFRESH_TIME, System.currentTimeMillis());
                refresh_updatetime.setText("最后更新：" + dateFormat.format(new Date(time)));
                refresh_arr.startAnimation(down);
                break;
            case RELEASE_REFRESH:
                refresh_state.setText("松开立即刷新");
                refresh_arr.startAnimation(up);
                break;
            case REFRESHING:
                refresh_arr.clearAnimation();
                refresh_state.setText("Robin8引擎发动中");
                refresh_arr.setVisibility(View.INVISIBLE);
                iv_progress.setVisibility(View.VISIBLE);
                iv_progress.setBackgroundDrawable(mAnimationUp);
                if (mAnimationUp != null && !mAnimationUp.isRunning()) {
                    mAnimationUp.start();
                }
                break;

            default:
                break;
        }
    }

    class MyOnScrollListener implements OnScrollListener {

        // 监听Listview的滑动状态
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // 监听停止状态和 惯性滑动停止
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {

                // 滑动到底部
                if (getLastVisiblePosition() > 3 && getLastVisiblePosition() >= getCount() - 3) {

                    if (!isLoadMore) {
                        isLoadMore = true;
                        pb_load_more.setVisibility(View.VISIBLE);
                        tv_load_more.setText(BaseApplication.getContext().getResources().getText(R.string.more_loading));
                        pb_load_more.setBackgroundDrawable(mAnimationDown);
                        if (mAnimationDown != null && !mAnimationDown.isRunning()) {
                            mAnimationDown.start();
                        }
                        footer_view.setPadding(0, 0, 0, 0);// 显示上拉加载布局
//                        setSelection(getCount());// 显示最后一条数据，把上拉加载布局显示出来
                        System.out.println("上拉加载");
                        if (mListener != null) {
                            mListener.onLoadingMore();
                        }
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }
}
