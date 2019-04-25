package com.robin8.rb.ui.activity.uesr_msg;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.model.UserCircleBean;
import com.robin8.rb.ui.model.UserShowBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 普通用户
 */
public class PublicUserMsgActivity extends BaseActivity {

    public static final String PUBLIC = "public_user";
    @Bind(R.id.tv_item_range)
    TextView tvItemRange;
    @Bind(R.id.layout_item_circle)
    LinearLayout layoutCircle;
    @Bind(R.id.edit_wechat_num)
    EditText editWechatNum;

    private WProgressDialog mWProgressDialog;
    private List<String> circleNameList;
    private ArrayList<Integer> circleIdList;
    private List<UserCircleBean.CirclesListBean> circlesList;//接口的圈子


    @Override
    public void setTitleView() {
        mTVCenter.setText(getResources().getString(R.string.tv_general_user).toString());
        mTvSave.setVisibility(View.VISIBLE);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_public_user_msg, mLLContent, true);
        //普通用户
        ButterKnife.bind(this);
        circleIdList = new ArrayList<>();
        circleNameList = new ArrayList<>();
        initData();
        Intent intent = getIntent();
        String extra = intent.getStringExtra(PUBLIC);
        if (! TextUtils.isEmpty(extra)) {
            initLastData(extra);
        }
        layoutCircle.setOnClickListener(this);
        if (! TextUtils.isEmpty(editWechatNum.getText().toString().trim())) {
            editWechatNum.setSelection(editWechatNum.getText().toString().trim().length());
        }
        editWechatNum.setOnFocusChangeListener(new MyFocusListener(R.id.edit_wechat_num));
        setSave();
        editWechatNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSave();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setSave() {
        if (checkParams(false) == true) {
            mTvSave.setTextColor(getResources().getColor(R.color.black_000000));
            mTvSave.setClickable(true);
        } else {
            mTvSave.setTextColor(getResources().getColor(R.color.gray_second));
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
    public void onClick(View v) {
        super.onClick(v);
        setSave();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.layout_item_circle:
                Intent intent = new Intent(PublicUserMsgActivity.this, ChooseFavoriteActivity.class);
                //传送当前已经选择的圈子
                if (circleIdList != null && circleIdList.size() != 0) {
                    intent.putIntegerArrayListExtra(ChooseFavoriteActivity.CHOOSE_LIST, circleIdList);
                }
                intent.putExtra(ChooseFavoriteActivity.CHOOSE_CIRCLE, PUBLIC);
                startActivityForResult(intent, SPConstants.CIRCLE_CHOOSE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(tvItemRange.getText().toString().trim()) || tvItemRange.getText().toString().trim().equals("请选择你感兴趣的圈子")) {
                    CustomToast.showShort(this, "请选择你感兴趣的圈子");
                } else if (TextUtils.isEmpty(editWechatNum.getText().toString().trim())) {
                    CustomToast.showShort(this, "请填写微信好友数");
                } else {
                    saveData();
                }
                break;
        }
    }

    private void saveData() {
        if (checkParams(true)==false) {
            return;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        RequestParams mRequestParams = new RequestParams();

        String circleResult = Joiner.on(",").join(circleIdList);
        LogUtil.LogShitou("选择的圈子", circleResult);

        mRequestParams.put("circle_ids", circleResult);

        mRequestParams.put("wechat_firends_count", Integer.valueOf(editShow(true,editWechatNum.getText().toString().trim(),"数量：")));
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFO_URL), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                LogUtil.LogShitou("更新基本信息", "==>" + response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean != null) {
                    if (baseBean.getError() == 0) {
                        Intent intent = new Intent(PublicUserMsgActivity.this, UserInformationActivity.class);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
            }
        });
    }

    private Boolean checkParams(boolean show) {
        if (TextUtils.isEmpty(tvItemRange.getText().toString()) || tvItemRange.getText().toString().equals("请选择你感兴趣的圈子") || circleIdList.size() == 0) {
            if (show) {
                CustomToast.showShort(this, "请选择你感兴趣的圈子");
            }
            return false;
        }

        if (TextUtils.isEmpty(editWechatNum.getText().toString().trim())) {
            if (show) {
                CustomToast.showShort(this, "请填写微信好友数");
            }
            return false;
        }

        return true;
    }

    private void initLastData(String extra) {
        UserShowBean userShowBean = GsonTools.jsonToBean(extra, UserShowBean.class);
        if (userShowBean != null) {
            if (userShowBean.getError() == 0) {
                UserShowBean.KolBean kol = userShowBean.getKol();
                if (kol != null) {
                    List<UserShowBean.CirclesBean> initCirclesList = kol.getCircles();
                    //取出圈子id／名称
                    if (initCirclesList.size() != 0) {
                        for (int i = 0; i < initCirclesList.size(); i++) {
                            circleIdList.add(initCirclesList.get(i).getId());
                            circleNameList.add(initCirclesList.get(i).getLabel());
                        }
                        tvItemRange.setText(Joiner.on(" , ").join(circleNameList));
                    }
                    editWechatNum.setText("数量：" + kol.getWechat_friends_count());
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPConstants.CIRCLE_CHOOSE) {
            if (resultCode == RESULT_OK) {
                // tvItemRange.setText();
                ArrayList<Integer> listExtra = data.getIntegerArrayListExtra(ChooseFavoriteActivity.CHOOSE_CIRCLE);
                if (listExtra.size() != 0) {
                    if (circleIdList.size() != 0) {
                        circleIdList.clear();
                    }
                    if (circleNameList.size() != 0) {
                        circleNameList.clear();
                    }
                    for (int i = 0; i < listExtra.size(); i++) {
                        circleIdList.add(listExtra.get(i));
                        LogUtil.LogShitou("返回的圈子id", "===>" + circleIdList.get(i));
                    }
                    for (int i = 0; i < circlesList.size(); i++) {
                        for (int j = 0; j < listExtra.size(); j++) {
                            if (circlesList.get(i).getId() == listExtra.get(j)) {
                                circleNameList.add(circlesList.get(i).getLabel());
                            }
                        }
                    }
                    tvItemRange.setText(Joiner.on(" ,").join(circleNameList));
                }else {
                    tvItemRange.setText("请选择你感兴趣的圈子");
                }
            }
            setSave();
        }
    }

    private void initData() {
        String data = CacheUtils.getString(PublicUserMsgActivity.this, HelpTools.BASEINFO, null);
        if (TextUtils.isEmpty(data)) {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(this);
            }
            mWProgressDialog.show();
            final BasePresenter mBasePresenter = new BasePresenter();
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.UPDATE_BASE_INFOS_URL), null, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    LogUtil.LogShitou("数据", "==>" + response);
                    UserCircleBean bean = GsonTools.jsonToBean(response, UserCircleBean.class);
                    if (bean != null) {
                        if (bean.getError() == 0) {
                            CacheUtils.putString(PublicUserMsgActivity.this, HelpTools.BASEINFO, response);
                            circlesList = bean.getCircles_list();
                        }
                    }
                }
            });
        } else {
            UserCircleBean bean = GsonTools.jsonToBean(data, UserCircleBean.class);
            if (bean != null) {
                if (bean.getError() == 0) {
                    circlesList = bean.getCircles_list();
                }
            }
        }

    }

    class MyFocusListener implements View.OnFocusChangeListener {
        private int viewId;

        public MyFocusListener(int id) {
            this.viewId = id;
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            switch (viewId) {
                case R.id.edit_wechat_num:
                    editShowText(b, editWechatNum, editWechatNum.getText().toString().trim(), "数量：");
                    break;
            }
        }
    }

    private void editShowText(boolean is, EditText editText, String tx, String repaceStr) {
        if (is == false) {
            if (! TextUtils.isEmpty(tx)) {
                if (tx.startsWith(repaceStr)) {
                    editText.setText(tx);
                } else {
                    editText.setText(repaceStr + editText.getText().toString().trim());
                }
            }
        } else {
            if (! TextUtils.isEmpty(tx)) {
                if (tx.startsWith(repaceStr)) {
                    String replace = tx.replace(repaceStr, "");
                    editText.setText(replace);
                }
            }
        }
    }

    /**
     @param is true=提交；false = 赋值
     @param tx
     @param repaceStr editview要添加的开头文字
     @return
     */
    private String editShow(boolean is, String tx, String repaceStr) {
        if (is == true) {
            if (tx.startsWith(repaceStr)) {
                String replace = tx.replace(repaceStr, "");
                tx = replace;
            }
            return tx;
        } else {
            if (! tx.startsWith(repaceStr)) {
                tx = repaceStr + tx;
            }
            return tx;
        }
    }

}
