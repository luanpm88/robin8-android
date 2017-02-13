package com.robin8.rb.http.xutil;

import android.text.TextUtils;
import android.util.Log;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/1/25 11:16
 */
public abstract class DefaultHttpCallBack extends IHttpCallBack {
    public DefaultHttpCallBack(String tag) {
        super(tag);
    }

    @Override
    public void onLoading(ResponceBean responceBean) {

    }

    @Override
    public void onFailure(ResponceBean responceBean) {
        Log.e("xxfigo", "response2="+responceBean.errorCode+responceBean.pair.first + responceBean.pair.second);
        try {
            BaseBean baseBean = GsonTools.jsonToBean(responceBean.pair.second, BaseBean.class);
            if (!TextUtils.isEmpty(baseBean.getDetail()))
                CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
            else
                CustomToast.showShort(BaseApplication.getContext(), responceBean.pair.second);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
