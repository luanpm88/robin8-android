package com.robin8.rb.module.mine.presenter;

import android.content.Context;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.UIUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;

/**
 * @author Figo
 */
public class BindSocialPresenter extends BindSocialPresenterListener {

    private String name;
    private Context mContext;
    private TextView tvName;
    private OnBindListener listener;

    public interface OnBindListener {
        void onResponse(String userName);
    }

    public void setOnBindListener(OnBindListener listener) {
        this.listener = listener;
    }

    public BindSocialPresenter(Context context, TextView tvName, String name) {
        super(context);
        mContext = context;
        this.tvName = tvName;
        this.name = name;
    }

    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
        }
    }

    @Override
    public void onComplete(final Platform platform, int action, final HashMap<String, Object> res) {
        super.onComplete(platform, action, res);

        //第三方登录成功
        if (action == Platform.ACTION_USER_INFOR) {
            final PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
            final String token = platDB.getToken();
            final String userGender = platDB.getUserGender();
            final String userIcon = platDB.getUserIcon();
            final String userId = platDB.getUserId();
            final String userName = platDB.getUserName();

            UIUtils.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    RequestParams mRequestParams = new RequestParams();
                    mRequestParams.put("provider", provider);
                    mRequestParams.put("token", token);
                    mRequestParams.put("name", userName);
                    mRequestParams.put("uid", userId);
                    mRequestParams.put("url", "");
                    mRequestParams.put("avatar_url", userIcon);
                    mRequestParams.put("desc", String.valueOf(res.get("description")));
                    mRequestParams.put("serial_params", GsonTools.mapToJson(res));
                    if ("SinaWeibo".equals(platform.getName())) {
                        mRequestParams.put("uid", userId);
                        mRequestParams.put("followers_count", String.valueOf(res.get("followers_count")));//粉丝数
                        mRequestParams.put("statuses_count", String.valueOf(res.get("statuses_count")));//微博数
                        mRequestParams.put("registered_at", String.valueOf(res.get("created_at")));//微博注册时间
                        mRequestParams.put("verified", String.valueOf(res.get("verified")));//微博是否加V验证
                        mRequestParams.put("refresh_token", String.valueOf(res.get("refresh_token")))
                        ;//微博令牌刷新token
                    } else if ("Wechat".equals(platform.getName())) {
                        mRequestParams.put("uid", String.valueOf(res.get("openid")));
                        mRequestParams.put("unionid", String.valueOf(res.get("unionid")));
                    }

                    mRequestParams.put("province", String.valueOf(res.get("province")));
                    mRequestParams.put("city", String.valueOf(res.get("city")));
                    mRequestParams.put("gender", String.valueOf(res.get("gender")));
                    mRequestParams.put("is_vip", String.valueOf(res.get("is_vip")));
                    mRequestParams.put("is_yellow_vip", String.valueOf(res.get("is_yellow_vip")));
                    getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.KOLS_IDENTITY_BIND_URL), mRequestParams, new RequestCallback() {
                        @Override
                        public void onError(Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            tvName.setText(name + mContext.getString(R.string.has_binded) + userName);
                            if (listener != null) {
                                listener.onResponse(userName);
                            }
                        }
                    });
                }
            });
        }
    }
}