package com.robin8.rb.module.mine.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.ui.widget.OtherGridView;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 成为KOL
 */
public class BeKolDetailActivity extends BaseActivity {


    private static final String SLASH = "/";
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.tv_et_num)
    TextView tvEtNum;
    @Bind(R.id.ll_et_content)
    LinearLayout llEtContent;
    @Bind(R.id.et_desc)
    EditText etDesc;
    @Bind(R.id.tv_desc_num)
    TextView tvDescNum;
    @Bind(R.id.rl_et_content)
    RelativeLayout rlEtContent;
    @Bind(R.id.gv_tags)
    OtherGridView gvTags;
    private String content;
    private String name;
    private int mLimitNumber = 10;
    List<TagsItem> mGridData = new ArrayList<>();
    private int mTagCount =0;
    private ViewHolder holder;

    @Override
    public void setTitleView() {
        initData();
        if (getString(R.string.interest).endsWith(name)) {
            mTVCenter.setText(this.getText(R.string.interest_tags));
        } else if (getString(R.string.social_account).equals(name)){
            mTVCenter.setText(this.getText(R.string.bind_social_account));
        } else {
            mTVCenter.setText(this.getText(R.string.please_write) + name);
        }
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(getString(R.string.submit));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_be_kol_detail, mLLContent, true);
        ButterKnife.bind(this);
        updateView();
        mBottomTv.setOnClickListener(this);
    }

    private void updateView() {
        if (getString(R.string.nickname).endsWith(name)) {//昵称
            llEtContent.setVisibility(View.VISIBLE);
            mLimitNumber = 10;
            etContent.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (getString(R.string.age).endsWith(name)) {//年龄
            llEtContent.setVisibility(View.VISIBLE);
            tvEtNum.setVisibility(View.GONE);
            mLimitNumber = 3;
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (getString(R.string.professional).endsWith(name)) {//职业
            llEtContent.setVisibility(View.VISIBLE);
            mLimitNumber = 10;
            etContent.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if (getString(R.string.interest).endsWith(name)) {//兴趣
            gvTags.setVisibility(View.VISIBLE);
            initGridData();
            gvTags.setAdapter(new MyGridAdapter());
        } else if (getString(R.string.introduction).endsWith(name)) {//简介
            rlEtContent.setVisibility(View.VISIBLE);
            mLimitNumber = 100;
            if (TextUtils.isEmpty(content)) {
                etDesc.setHint(this.getText(R.string.please_write) + name);
            } else {
                etDesc.setText(content);
                tvDescNum.setText(String.valueOf(content.length()) + SLASH + String.valueOf(mLimitNumber));
                etDesc.setSelection(etDesc.length());
            }
            etDesc.addTextChangedListener(new MyTextWatcher(tvDescNum, etDesc));
        }

        etContent.addTextChangedListener(new MyTextWatcher(tvEtNum, etContent));
        if (TextUtils.isEmpty(content)) {
            etContent.setHint(this.getText(R.string.please_write) + name);
        } else {
            etContent.setText(content);
            tvEtNum.setText(String.valueOf(content.length()) + SLASH + String.valueOf(mLimitNumber));
            etContent.setSelection(etContent.length());
        }
    }

    private void initGridData() {
        mGridData.clear();
        String[] stringArray = getResources().getStringArray(R.array.first_page_tag_item_total);
        for (int i = 0; i < stringArray.length; i++) {
            TagsItem item = new TagsItem();
            item.name = stringArray[i];
            mGridData.add(item);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        content = intent.getStringExtra("content");
    }

    private String getTags() {
        StringBuffer sb = new StringBuffer();
        mTagCount = 0;
        if (mGridData == null || mGridData.size() == 0) {
            LogUtil.LogShitou("is?","===========");
            return "";
        }else {
            LogUtil.LogShitou("have","==>"+mGridData.size());
            for (int i = 0; i < mGridData.size(); i++) {
                TagsItem tagsItem = mGridData.get(i);
                if (tagsItem.isSelected) {
                    sb.append(tagsItem.name).append(",");
                    mTagCount++;
                }
            }
            if (sb.length()==0){
                return "";
            }else {
                return sb.substring(0, sb.length() - 1);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        switch (v.getId()) {
            case R.id.tv_bottom:
                confirm();
                break;
        }
    }

    private void confirm() {
        if (getString(R.string.nickname).endsWith(name)) {//昵称
            content = etContent.getText().toString();
        } else if (getString(R.string.age).endsWith(name)) {//年龄
            content = etContent.getText().toString();
        } else if (getString(R.string.professional).endsWith(name)) {//职业
            content = etContent.getText().toString();
        } else if (getString(R.string.interest).endsWith(name)) {//兴趣
            content = getTags();
            if(mTagCount<2 || mTagCount>5){
                CustomToast.showShort(this,getString(R.string.counts_2to5));
                return;
            }
        } else if (getString(R.string.introduction).endsWith(name)) {//简介
            content = etDesc.getText().toString();
        }

        if (TextUtils.isEmpty(content)) {
            CustomToast.showShort(this,getString(R.string.please_write)+name);
            return;
        }

        Intent intent = getIntent();
        intent.putExtra("name", name);
        intent.putExtra("content", content);
        setResult(SPConstants.BE_KOL_DETAIL, intent);
        finish();
    }

    private class MyTextWatcher implements TextWatcher {

        private TextView tv;
        private EditText et;

        public MyTextWatcher(TextView tv, EditText et) {
            this.tv = tv;
            this.et = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                tv.setText(String.valueOf(s.length()) + SLASH + String.valueOf(mLimitNumber));
            } else {
                et.setHint(BeKolDetailActivity.this.getText(R.string.please_write) + name);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class MyGridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mGridData.size();
        }

        @Override
        public TagsItem getItem(int position) {
            return mGridData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TagsItem item = getItem(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BeKolDetailActivity.this).inflate(R.layout.item_detail_be_kol_grid, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(item.name);
            holder.tvName.setOnClickListener(new MyOnClickListener(holder.tvName, item));
            return convertView;
        }
    }

    private class ViewHolder {
        public TextView tvName;
    }

    private class MyOnClickListener implements View.OnClickListener {
        private TextView tv;
        private TagsItem item;

        public MyOnClickListener(TextView tv, TagsItem item) {
            this.tv = tv;
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (tv.isSelected()) {
                item.isSelected = false;
            } else {
                item.isSelected = true;
            }
            tv.setSelected(item.isSelected);
        }
    }

    private class TagsItem {
        public String name;
        public boolean isSelected;
    }
}
