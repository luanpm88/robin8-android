package com.robin8.rb.ui.indiana.activity;

import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.indiana.model.AddressModel;
import com.robin8.rb.ui.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;


/**
 * 收货地址
 */
public class ReceiveAddressActivity extends BaseActivity {


    private EditText mNameContentEt;
    private EditText mTelphoneContentEt;
    private EditText mAddressContentEt;
    private TextView mBottomTv;
    private BasePresenter mBasePresenter;
    private RequestParams mRequestParams;
    private WProgressDialog mWProgressDialog;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.delivery_address);
    }

    @Override
    public void initView() {
        mLLContent.setBackgroundResource(R.color.white_custom);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_delivery_address, mLLContent, true);
        View layoutName = view.findViewById(R.id.layout_name);
        TextView nameNameTv = (TextView) layoutName.findViewById(R.id.tv_name);
        nameNameTv.setText(getString(R.string.name));
        mNameContentEt = (EditText) layoutName.findViewById(R.id.et_content);
        mNameContentEt.setInputType(InputType.TYPE_CLASS_TEXT);

        View layoutTelphone = view.findViewById(R.id.layout_telphone);
        TextView nameTelphoneTv = (TextView) layoutTelphone.findViewById(R.id.tv_name);
        nameTelphoneTv.setText(getString(R.string.contact_telphone));
        mTelphoneContentEt = (EditText) layoutTelphone.findViewById(R.id.et_content);
        mTelphoneContentEt.setInputType(InputType.TYPE_CLASS_PHONE);


        View layoutAddress = view.findViewById(R.id.layout_address);
        TextView nameAddressTv = (TextView) layoutAddress.findViewById(R.id.tv_name);
        nameAddressTv.setText(getString(R.string.delivery_address));
        mAddressContentEt = (EditText) layoutAddress.findViewById(R.id.et_content);
        mAddressContentEt.setInputType(InputType.TYPE_CLASS_TEXT);

        mBottomTv = (TextView) view.findViewById(R.id.tv_bottom);
        mBottomTv.setOnClickListener(this);

        initData();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INDIANA_ADDRESS;
        super.onResume();
    }

    private void initData() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        if(mBasePresenter==null){
            mBasePresenter = new BasePresenter();
        }

        mWProgressDialog.show();

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.DELIVERY_ADDRESS_URL), mRequestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                CustomToast.showShort(ReceiveAddressActivity.this, getString(R.string.submit_failed));
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                AddressModel addressModel = GsonTools.jsonToBean(response, AddressModel.class);
                if (addressModel != null && addressModel.getError() == 0) {
                    AddressModel.Address address = addressModel.getAddress();
                    if(address !=null){
                        mNameContentEt.setText(address.getName());
                        mTelphoneContentEt.setText(address.getPhone());
                        mAddressContentEt.setText(address.getLocation());
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                submit();
                break;
        }
    }

    private void submit() {
        if (mNameContentEt == null || mTelphoneContentEt == null || mAddressContentEt == null) {
            return;
        }
        String nameContent = mNameContentEt.getText().toString();
        String telphoneContent = mTelphoneContentEt.getText().toString();
        String addressContent = mAddressContentEt.getText().toString();

        if (TextUtils.isEmpty(nameContent)) {
            CustomToast.showShort(this, getString(R.string.please_write_name));
            return;
        }

        if (TextUtils.isEmpty(telphoneContent)) {
            CustomToast.showShort(this, getString(R.string.please_write_telphone));
            return;
        }

        if (TextUtils.isEmpty(addressContent)) {
            CustomToast.showShort(this, getString(R.string.please_write_address));
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }

        mWProgressDialog.show();

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        mRequestParams.put("name", nameContent);
        mRequestParams.put("phone", telphoneContent);
        mRequestParams.put("location", addressContent);
        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.DELIVERY_ADDRESS_URL), mRequestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                CustomToast.showShort(ReceiveAddressActivity.this, getString(R.string.submit_failed));
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);

                if(bean==null){
                    CustomToast.showShort(ReceiveAddressActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (bean.getError() == 0) {
                    CustomToast.showShort(ReceiveAddressActivity.this, getString(R.string.submit_success));
                    ReceiveAddressActivity.this.finish();
                } else {
                    CustomToast.showShort(ReceiveAddressActivity.this, getString(R.string.submit_failed));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mBasePresenter != null) {
            mBasePresenter = null;
        }
        if (mRequestParams != null) {
            mRequestParams = null;
        }
        if (mWProgressDialog != null) {
            mWProgressDialog = null;
        }
        super.onDestroy();
    }
}
