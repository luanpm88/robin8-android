package com.robin8.rb.ui.module.social;

import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.BindSocialPresenter;
import com.robin8.rb.view.IBindSocialView;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 测试影响力 绑定社交账号页面
 */
public class BindSocialActivity extends BaseActivity implements IBindSocialView {

    private Button mBtnStartTest;
    private ImageButton mIBWeiXin;
    private ImageButton mIBWeiBo;
    private ImageButton mIBQQ;
    private BindSocialPresenter mBindSocialPresenter;
    private LinearLayout mLLMove;
    private TextView mTVBindSocial;
    private ListView mLVBind;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.influence_measure));
    }

    @Override
    public void initView() {
        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mLLRoot.setBackgroundResource(R.mipmap.mine_bg);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_bind_social, mLLContent, true);
        Button mBtnStartTest = (Button) view.findViewById(R.id.bt_start_test);
        ImageButton mIBWeiXin = (ImageButton) view.findViewById(R.id.ib_weixin);
        ImageButton mIBWeiBo = (ImageButton) view.findViewById(R.id.ib_weibo);
        ImageButton mIBQQ = (ImageButton) view.findViewById(R.id.ib_qq);
        TextView mTVInfo = (TextView) view.findViewById(R.id.tv_info);
        mTVBindSocial = (TextView) view.findViewById(R.id.tv_bind_social);
        mLLMove = (LinearLayout) view.findViewById(R.id.ll_third_login_move);
        mLVBind = (ListView) view.findViewById(R.id.lv_bind_list);

        mTVInfo.setText(Html.fromHtml(getString(R.string.click_approve) + "<font color=#2dcad0>" + getString(R.string.serviece_protocol) + "</font>"));
        mBtnStartTest.setOnClickListener(this);
        mIBWeiXin.setOnClickListener(this);
        mIBWeiBo.setOnClickListener(this);
        mIBQQ.setOnClickListener(this);

        mBindSocialPresenter = new BindSocialPresenter(this,this);
        mBindSocialPresenter.init();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_start_test:
                Intent intent = new Intent(this, SocialInfluenceActivity.class);
                // intent.putExtra("kol_uuid", mKolUuid);
                //  intent.putExtra("haveResult", true);
                startActivity(intent);
               // mBindSocialPresenter.startTest();
                break;
            case R.id.ib_weixin:
                mBindSocialPresenter.authorize(new Wechat());
                break;
            case R.id.ib_weibo:
                mBindSocialPresenter.authorize(new SinaWeibo());
                break;
            case R.id.ib_qq:
                mBindSocialPresenter.authorize(new QQ());
                break;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public LinearLayout getLinearLayout() {
        return mLLMove;
    }

    @Override
    public ListView getBindListView() {
        return mLVBind;
    }

    @Override
    public TextView getBindSocialTV() {
        return mTVBindSocial;
    }

    @Override
    public void setBindSocialTV(int visible){
        mTVBindSocial.setVisibility(visible);
    }
}
