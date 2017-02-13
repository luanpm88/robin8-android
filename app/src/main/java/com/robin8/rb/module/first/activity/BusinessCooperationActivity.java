package com.robin8.rb.module.first.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;


/**
 * Created by IBM on 2016/8/8.
 */
public class BusinessCooperationActivity extends BaseActivity {

    private static final String CONTENT = "content";
    private static final String KOLID = "KOLID";
    private static final String SIGN_RABBIT = ",";
    private static final String SIGN_COLON = ":";
    private final int ITEM_NMAE = 0;
    private final int ITEM_TELPHONE = 1;
    private final int ITEM_COMPANY = 2;
    private final int ITEM_EMAIL = 3;
    private final int ITEM_KOL = 4;
    private ListView mListView;
    private String[] stringArray = null;
    private String kolName;
    private TextView mTvSubmit;
    private BaseAdapter mAdapter;
    private ArrayList<ViewHolder> mHolders = new ArrayList<>();
    private int kolId;
    private StringBuffer sb = new StringBuffer();
    private int from;

    @Override
    public void setTitleView() {
        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
        mTVCenter.setText(getString(R.string.business_cooperation));
    }

    @Override
    public void initView() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_business_cooperation, mLLContent);
        mListView = (ListView) view.findViewById(R.id.lv_list);
        mTvSubmit = (TextView) view.findViewById(R.id.tv_submit);
        mTvSubmit.setOnClickListener(this);

        Intent intent = getIntent();
        kolName = intent.getStringExtra("kol_name");
        kolId = intent.getIntExtra("kol_id", 0);
        if(from == SPConstants.PRODUCT_LIST){
            stringArray = getResources().getStringArray(R.array.business_cooperation_from_product_list);
        }else {
            stringArray = getResources().getStringArray(R.array.business_cooperation);
        }

        mListView.setAdapter(mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return stringArray.length;
            }

            @Override
            public String getItem(int position) {
                return stringArray[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.registration_item_et, null);
                    holder.tv = (TextView) convertView.findViewById(R.id.tv_item_title);
                    holder.et = (EditText) convertView.findViewById(R.id.et_item_info);
                    mHolders.add(holder);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv.setText(stringArray[position]);
                LoginBean.KolEntity kol = BaseApplication.getInstance().getLoginBean().getKol();
                switch (position) {
                    case ITEM_NMAE:
                        holder.et.setHint(getString(R.string.please_write_name));
                        holder.et.setText(kol.getName());
                        break;
                    case ITEM_TELPHONE:
                        holder.et.setHint(getString(R.string.please_write_telphone));
                        holder.et.setText(kol.getMobile_number());
                        break;
                    case ITEM_COMPANY:
                        holder.et.setHint(getString(R.string.please_write_company));
                        break;
                    case ITEM_EMAIL:
                        holder.et.setHint(getString(R.string.please_write_email));
                        break;
                    case ITEM_KOL:
                        holder.et.setHint(getString(R.string.please_write_kol));
                        holder.et.setText(kolName);
                        break;
                }
                return convertView;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (isDoubleClick()) {
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {

        if (!checkParams()) {
            return;
        }

        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put(SPConstants.APP_VERSION, AppUtils.getVersionName(this.getApplicationContext()));
        params.put(SPConstants.APP_PLATFORM, SPConstants.ANDROID);
        params.put(SPConstants.OS_VERSION, AppUtils.getSystemVersion());
        params.put(SPConstants.DEVICE_MODEL, AppUtils.getPhoneModel());
        params.put(CONTENT, getParams());
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.FEED_BACK_URL), params, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean != null && bean.getError() == 0) {
                    CustomToast.showShort(BusinessCooperationActivity.this, BusinessCooperationActivity.this.getString(R.string.business_submit_success));
                    BusinessCooperationActivity.this.finish();
                }
            }
        });
    }

    private boolean checkParams() {

        for (int i = 0; i < mHolders.size(); i++) {
            String content = mHolders.get(i).et.getText().toString();
            switch (i) {
                case ITEM_NMAE:
                    if (TextUtils.isEmpty(content)) {
                        CustomToast.showShort(BusinessCooperationActivity.this, getString(R.string.please_write_name));
                        return false;
                    }
                    break;
                case ITEM_TELPHONE:
                    if (TextUtils.isEmpty(content)) {
                        CustomToast.showShort(BusinessCooperationActivity.this, getString(R.string.please_write_telphone));
                        return false;
                    }
                    break;
                case ITEM_KOL:
                    if (TextUtils.isEmpty(content)) {
                        CustomToast.showShort(BusinessCooperationActivity.this, getString(R.string.please_write_kol));
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * 我是品牌主,我要特邀KOL\n姓名:%@,电话:%@,公司:%@,邮箱:%@,特邀KOL:%@,KOLID:%@
     */
    private String getParams() {
        sb.delete(0, sb.length());
        if(from == SPConstants.PRODUCT_LIST){
            sb.append(getString(R.string.business_cop_content_fir_from_product_list));
        }else {
            sb.append(getString(R.string.business_cop_content_fir));
        }

        if (mHolders == null || mHolders.size() == 0) {
            return "";
        }

        for (int i = 0; i < mHolders.size(); i++) {
            sb.append(stringArray[i]).append(SIGN_COLON).append(mHolders.get(i).et.getText().toString()).append(SIGN_RABBIT);
        }
        if(from == SPConstants.PRODUCT_LIST){
            sb.delete(sb.length()-1,sb.length());
        }else {
            sb.append(KOLID).append(SIGN_COLON).append(kolId);
        }
        return sb.toString();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    class ViewHolder {

        public TextView tv;
        public EditText et;
    }
}
