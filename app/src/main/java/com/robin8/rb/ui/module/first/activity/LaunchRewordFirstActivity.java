package com.robin8.rb.ui.module.first.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.ui.module.first.model.ExpectEffectModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发起悬赏活动首页
 */
public class LaunchRewordFirstActivity extends BaseActivity {

    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.gv_expect_effect)
    GridView gvExpectEffect;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    private BasePresenter mBasePresenter;
    private MyBaseAdapter mMyBaseAdapter;
    private WProgressDialog mWProgressDialog;
    private LayoutInflater mLayoutInflater;
    private ExpectEffectModel mExpectEffectModel;
    private List<ExpectEffectModel.ExpectEffectListBean> mExpectEffectList;
    private String mExpectEffectStr;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.launch_reword_activity);
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(R.string.intelligent_release);
        mLayoutInflater = LayoutInflater.from(this);
        View view = mLayoutInflater.inflate(R.layout.activity_launch_reword_activity_first, mLLContent, true);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager cm = (ClipboardManager) BaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm.getPrimaryClipDescription()!=null && cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData cd = cm.getPrimaryClip();
                ClipData.Item item = cd.getItemAt(0);
                String itemStr = item.getText().toString();
                if (!TextUtils.isEmpty(itemStr)) {
                    etAddress.setText(itemStr);
                    tvDelete.setVisibility(View.VISIBLE);
                }
            }
        }

        IconFontHelper.setTextIconFont(tvDelete, R.string.icons_delete2);
        mBottomTv.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null || s.length() > 0) {
                    tvDelete.setVisibility(View.VISIBLE);
                } else {
                    tvDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loadData();
        initGridView();
    }

    private void initGridView() {
        mMyBaseAdapter = new MyBaseAdapter();
        gvExpectEffect.setAdapter(mMyBaseAdapter);
    }

    private void loadData() {

        String expectEffectData = CacheUtils.getString(this, SPConstants.EXPECT_EFFECT_DATA, null);
        if (expectEffectData != null) {
            parseJson(expectEffectData);
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.EXPECT_EFFECT_LIST_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    CacheUtils.putString(LaunchRewordFirstActivity.this, SPConstants.EXPECT_EFFECT_DATA, response);
                    parseJson(response);
                }
            }
        });
    }

    private void parseJson(String expectEffectData) {
        mExpectEffectModel = GsonTools.jsonToBean(expectEffectData, ExpectEffectModel.class);//解析
        mExpectEffectList = mExpectEffectModel.getExpect_effect_list();
        if (mExpectEffectList != null && mExpectEffectList.size() > 0) {
            mExpectEffectList.get(0).setSelected(true);
            mExpectEffectStr = mExpectEffectList.get(0).getName();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                //智能发布
                submit();
                break;
            case R.id.tv_delete:
                etAddress.setText(null);
                tvDelete.setVisibility(View.GONE);
                break;
        }
    }

    private void submit() {
        String etAddressStr = etAddress.getText().toString();
        if (TextUtils.isEmpty(etAddressStr)) {
            CustomToast.showShort(this, getString(R.string.please_write_link));
            return;
        }

        Intent intent = new Intent(LaunchRewordFirstActivity.this, LaunchRewordActivity.class);
        intent.putExtra("url", etAddressStr);
        intent.putExtra("expect_effect", mExpectEffectStr);
        startActivity(intent);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }


    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mBasePresenter == null) {
            mBasePresenter = null;
        }
    }


    private class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mExpectEffectList == null ? 0 : mExpectEffectList.size();
        }

        @Override
        public ExpectEffectModel.ExpectEffectListBean getItem(int position) {
            return mExpectEffectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ExpectEffectModel.ExpectEffectListBean bean = getItem(position);
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.activity_launch_reword_activity_first_item, null);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (bean == null) {
                return convertView;
            }
            holder.text.setText(bean.getLabel());
            holder.text.setSelected(bean.isSelected());
            holder.text.setOnClickListener(new MyOnClickListener(position));
            return convertView;
        }
    }

    private class ViewHolder {
        public TextView text;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private int position;

        public MyOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (mExpectEffectList == null || mExpectEffectList.size() == 0) {
                return;
            }
            for (int i = 0; i < mExpectEffectList.size(); i++) {
                ExpectEffectModel.ExpectEffectListBean expectEffectListBean = mExpectEffectList.get(i);
                if (i == position) {
                    expectEffectListBean.setSelected(true);
                    mExpectEffectStr = expectEffectListBean.getName();
                } else {
                    expectEffectListBean.setSelected(false);
                }
            }
            mMyBaseAdapter.notifyDataSetChanged();
        }
    }
}
