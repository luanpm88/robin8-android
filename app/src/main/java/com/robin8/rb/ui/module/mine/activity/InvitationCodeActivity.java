package com.robin8.rb.ui.module.mine.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import static com.robin8.rb.base.constants.CommonConfig.MY_INVITATION_CODE_URL;

/**
 * 输入邀请码
 */
public class InvitationCodeActivity extends BaseActivity {

    private EditText editCode;
    private WProgressDialog mWProgressDialog;


    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.edit_invitation_code));
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(getString(R.string.submit));
        mBottomTv.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_invitation_code, mLLContent);
        editCode = ((EditText) view.findViewById(R.id.ed_invitation_code));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                loadData();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void loadData() {
        if (TextUtils.isEmpty(editCode.getText().toString().trim())) {
            CustomToast.showShort(this, R.string.robin392);
            return;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(InvitationCodeActivity.this);
        }
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        //测试码768888
        params.put("invite_code", editCode.getText().toString().trim());
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(MY_INVITATION_CODE_URL), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                CustomToast.showShort(getApplicationContext(), R.string.robin391);

            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("邀请码", "======>" + response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        CustomToast.showShort(InvitationCodeActivity.this, R.string.robin390);
                        finish();
                    } else if (baseBean.getError() == 1) {
                        //无效的验证码
                        editCode.setText("");
                        if (baseBean.getDetail() != null) {
                            CustomToast.showShort(InvitationCodeActivity.this, baseBean.getDetail());
                        } else {
                            CustomToast.showShort(InvitationCodeActivity.this, R.string.robin389);
                        }
                    }
                }

            }
        });
    }


    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
