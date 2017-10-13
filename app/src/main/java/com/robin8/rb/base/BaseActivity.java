package com.robin8.rb.base;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.util.ActivityManagerUtils;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseActivity extends BaseDataActivity implements View.OnClickListener, Observer {

    protected LinearLayout mLLRoot;
    protected RelativeLayout mLLTitleBar;
    protected LinearLayout mLLContent;
    protected TextView mTVCenter;
    protected TextView mTVRight;
    protected ImageView mIVBack;
    protected View mViewLine;
    protected TextView mBottomTv;
    protected TextView mTvEdit;
    protected TextView tvJump;
    protected LinearLayout mShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.layout_titlebar_activity);
        ActivityManagerUtils.getInstance().addActivity(this);
        initBaseView();
        setTitleView();
        initView();
    }

    /**
     * 初始化界面
     */
    private void initBaseView() {
        mLLRoot = (LinearLayout) findViewById(R.id.ll_root);
        mLLTitleBar = (RelativeLayout) findViewById(R.id.ll_titlebar);
        mTVCenter = (TextView) findViewById(R.id.tv_center);
        mTVRight = (TextView) findViewById(R.id.tv_right);
        mIVBack = (ImageView) findViewById(R.id.iv_back);
        mLLContent = (LinearLayout) findViewById(R.id.ll_content);
        mViewLine = findViewById(R.id.view_line);
        mBottomTv = (TextView) findViewById(R.id.tv_bottom);
        mTvEdit = ((TextView) findViewById(R.id.tv_edit));
        tvJump = ((TextView) findViewById(R.id.tv_jump));
        mShare = ((LinearLayout) findViewById(R.id.ll_share));
        mShare.setOnClickListener(this);
        mTVRight.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        tvJump.setOnClickListener(this);
        mTvEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                executeOnclickLeftView();
                break;
            case R.id.iv_back:
                executeOnclickLeftView();
                break;
            case R.id.tv_right:
                executeOnclickRightView();
                break;
        }
    }

    /**
     * 设置标题
     */
    public abstract void setTitleView();

    /**
     * 初始化子View界面
     */
    public abstract void initView();

    /**
     * 点击左侧View执行事件
     */
    protected abstract void executeOnclickLeftView();

    /**
     * 点击右侧View执行事件
     */
    protected abstract void executeOnclickRightView();


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
