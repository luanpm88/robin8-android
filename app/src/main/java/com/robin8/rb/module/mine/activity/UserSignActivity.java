package com.robin8.rb.module.mine.activity;

import android.text.Html;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.mine.presenter.UserSignPresenter;
import com.robin8.rb.module.mine.view.IUserSignView;
import com.robin8.rb.ui.widget.OtherGridView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.view.widget.CircleImageView;
import com.robin8.rb.view.widget.CustomDialogManager;


/**
 签到页面 */
public class UserSignActivity extends BaseActivity implements IUserSignView {

    private UserSignPresenter mUserSignPresenter;
    private TextView mBottomTv;
    private OtherGridView mMonthGv;
    private TextView mHasSignedDaysTv;
    private CircleImageView mCircleImageView;
    private TextView mMonthTv;
    private TextView mUserNameTv;
    private TextView mEarnAccumulatedTv;
    private TextView mEarnTodayTv;
    private ImageView imgBack;
    private ImageView imgNext;
    private static final int MaxWidth=80;
    private static final int MaxSpeed=200;

    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_sign, mLLContent);

        mCircleImageView = (CircleImageView) view.findViewById(R.id.civ_image);
        mUserNameTv = (TextView) view.findViewById(R.id.tv_user_name);
        mHasSignedDaysTv = (TextView) view.findViewById(R.id.tv_has_signed_days);
        mMonthTv = (TextView) view.findViewById(R.id.tv_month);
        mMonthGv = (OtherGridView) view.findViewById(R.id.gv_month);
        mBottomTv = (TextView) view.findViewById(R.id.tv_bottom);
        //累计收益
        mEarnAccumulatedTv = (TextView) view.findViewById(R.id.tv_earn_accumulated);
        //今日收益
        mEarnTodayTv = (TextView) view.findViewById(R.id.tv_earn_today);

        imgBack = (ImageView) view.findViewById(R.id.img_back_last_month);
        imgNext = (ImageView) view.findViewById(R.id.img_back_next_month);
        LinearLayout ll_show = (LinearLayout) view.findViewById(R.id.ll_sign_show);
        ll_show.setOnClickListener(this);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        mBottomTv.setOnClickListener(this);
        mUserSignPresenter = new UserSignPresenter(this, this);
        mUserSignPresenter.init();
        mMonthGv.setLongClickable(true);

        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 同时，在这里增加相应的手势处理函数来完成相应的界面切换效果
                if (e1.getX() - e2.getX() >MaxWidth && Math.abs(velocityX)>MaxSpeed) {
                  //  CustomToast.showShort(UserSignActivity.this,"您现在是左滑！");
                    mUserSignPresenter.lastMouth(1);
                    return true;
                } else if (e2.getX()- e1.getX() > MaxWidth && Math.abs(velocityX)>MaxSpeed) {
                  //  CustomToast.showShort(UserSignActivity.this,"您现在是右滑！");
                    mUserSignPresenter.lastMouth(0);
                    return true;
                }
                return true;
            }
        });
        mMonthGv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_SIGN;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                mUserSignPresenter.sign();
                break;
            case R.id.img_back_last_month:
                mUserSignPresenter.lastMouth(0);
                break;
            case R.id.img_back_next_month:
                mUserSignPresenter.lastMouth(1);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_sign_show:
                shareCtaAndCti();
                break;
        }
    }

    private void shareCtaAndCti() {
        View view = LayoutInflater.from(UserSignActivity.this).inflate(R.layout.dialog_promit_cpa, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView infoSecondTv = (TextView) view.findViewById(R.id.tv_info_second);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        infoSecondTv.setVisibility(View.VISIBLE);
        topTv.setText(R.string.sign_rule);
        confirmTV.setText(R.string.known);
       // infoTv.setText(Html.fromHtml("1.连续签到,获得奖励累加,\n第一天签到获得<font color=#2bdbe2>" + "0.10" + "</font>元,\n连续签到两天获得<font color=#2bdbe2>" + "0.20" + "</font>元,\n连续签到三天获得<font color=#2bdbe2>" + "0.25" + "</font>元,\n连续签到四天获得<font color=#2bdbe2>" + "0.30" + "</font>元,\n连续签到五天获得<font color=#2bdbe2>" + "0.35" + "</font>元,\n连续签到六天获得<font color=#2bdbe2>" + "0.40" + "</font>元,\n连续签到七天获得<font color=#2bdbe2>" + "0.50" + "</font>元。")+"\n2.七天一个周期,第八天重新开始计算。(若中途某一天停止签到,仍会重新计算)");
        infoTv.setText(Html.fromHtml("1.连续签到,获得奖励累加,\n第一天签到获得<font color=#2bdbe2>" + "0.10" + "</font>元,\n连续签到两天获得<font color=#2bdbe2>" + "0.20" + "</font>元,\n连续签到三天获得<font color=#2bdbe2>" + "0.25" + "</font>元,\n连续签到四天获得<font color=#2bdbe2>" + "0.30" + "</font>元,\n连续签到五天获得<font color=#2bdbe2>" + "0.35" + "</font>元,\n连续签到六天获得<font color=#2bdbe2>" + "0.40" + "</font>元,\n连续签到七天获得<font color=#2bdbe2>" + "0.50" + "</font>元。"));
       infoSecondTv.setText("2.七天一个周期,第八天重新开始计算。(若中途某一天停止签到,仍会重新计算)");
        infoTv.setGravity(Gravity.LEFT);
        infoSecondTv.setGravity(Gravity.LEFT);
        final CustomDialogManager cdm = new CustomDialogManager(UserSignActivity.this, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    @Override
    protected void executeOnclickLeftView() {
        //  finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public void setCircleImageView(String url) {
        BitmapUtil.loadImage(this, url, mCircleImageView);
    }

    @Override
    public void setHasSignedDaysTv(String text) {
        mHasSignedDaysTv.setText(getString(R.string.you_have_sign) + text + getString(R.string.day));
    }

    @Override
    public void setUserNameTv(String text) {
        if (TextUtils.isEmpty(text)){
            mUserNameTv.setVisibility(View.GONE);
        }else {
            mUserNameTv.setText(text);
        }
    }

    @Override
    public void setMonthTv(String text) {
        mMonthTv.setText(text + getString(R.string.month_sign_chart));
    }

    @Override
    public OtherGridView getMonthGv() {
        return mMonthGv;
    }

    @Override
    public TextView getBottomTv() {
        return mBottomTv;
    }

    @Override
    public TextView mEarnAccumulatedTv() {
        return mEarnAccumulatedTv;
    }

    @Override
    public TextView getEarnTodayTv() {
        return mEarnTodayTv;
    }

    @Override
    public ImageView goNext() {
        return imgNext;
    }

    @Override
    public ImageView backLast() {
        return imgBack;
    }
}
