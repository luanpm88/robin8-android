package com.robin8.rb.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.dialog.CustomDialogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class BaseActivity extends BaseDataActivity implements View.OnClickListener, Observer {

    protected LinearLayout mLLRoot;
    protected RelativeLayout mLLTitleBar;
    protected LinearLayout mLLContent;
    protected TextView mTVCenter;
    protected TextView mTVRight;
    protected TextView mTvSave;
    protected ImageView mIVBack;
    protected View mViewLine;
    protected TextView mBottomTv;
    protected TextView mTvEdit;
    protected TextView tvJump;
    protected LinearLayout mShare;
    protected CardView mCardTitle;
    protected ImageView mImgBack;
    protected TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.layout_titlebar_activity);
       // ActivityManagerUtils.getInstance().addActivity(this);
        initBaseView();
        setTitleView();
        initView();
      //  showMyDialog();
    }

    /**
     * 初始化界面
     */
    private void initBaseView() {
        mLLRoot = (LinearLayout) findViewById(R.id.ll_root);
        mCardTitle = ((CardView) findViewById(R.id.card_view_title));
        mImgBack = ((ImageView) findViewById(R.id.img_back));
        mTitle = ((TextView) findViewById(R.id.tv_title));
        mLLTitleBar = (RelativeLayout) findViewById(R.id.ll_titlebar);
        mTVCenter = (TextView) findViewById(R.id.tv_center);
        mTVRight = (TextView) findViewById(R.id.tv_right);
        mTvSave = (TextView) findViewById(R.id.tv_save);
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
        mTvSave.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                executeOnclickLeftView();
                break;
            case R.id.iv_back:
            case R.id.img_back:
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


    private void showMyDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_red_layout, null);
        ImageView imgRed = (ImageView) view.findViewById(R.id.img_red);
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(imgRed, "rotation", 2, 10, 2, - 10, 2);
        translationXAnim.setDuration(200);
        translationXAnim.setRepeatCount(10);//无限循环
        translationXAnim.setRepeatMode(ValueAnimator.RESTART);
        translationXAnim.start();
        animators.add(translationXAnim);

        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(imgRed, "rotation", - 2, - 10, - 2, 10, - 2);
        translationYAnim.setDuration(200);
        translationYAnim.setRepeatCount(10);
        translationYAnim.setRepeatMode(ValueAnimator.RESTART);
        translationYAnim.start();
        animators.add(translationYAnim);
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();//
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(1);
        btnSexAnimatorSet.start();
        CustomDialogManager cdm = new CustomDialogManager(this, view);
        cdm.dg.setCanceledOnTouchOutside(false);
        Window window = cdm.dg.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1.0f;
        lp.dimAmount = 0.0f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        window.setWindowAnimations(R.style.AnimLeft);
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        // cdm.dg.setCancelable(false);
        cdm.dg.show();

    }
}
