package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.robin8.rb.R;
import com.robin8.rb.module.social.SocialInfluenceActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.IBindSocialView;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;

/**
 * @author Figo
 */
public class BindSocialPresenter extends BindSocialPresenterListener implements PresenterI {

    private final IBindSocialView mIUserView;
    private Activity mActivity;
    private String mKolUuid;
    private ArrayList<OtherLoginListBean.OtherLoginBean> mOtherLoginList;
    private boolean mUploadedContactsB;
    private LayoutInflater mLayoutInflater;

    public BindSocialPresenter(Activity activity, IBindSocialView userView) {

        super(activity);
        mActivity = activity;
        mIUserView = userView;
    }

    public void init() {

        Intent intent = mActivity.getIntent();
        Bundle rootBundle = intent.getExtras();
        mKolUuid = rootBundle.getString("kol_uuid", "");
      //  LogUtil.LogShitou("传递过来的mKolUuid", "===>" + mKolUuid);
        mOtherLoginList = (ArrayList<OtherLoginListBean.OtherLoginBean>) rootBundle.getSerializable("list");
        mUploadedContactsB = rootBundle.getBoolean("uploaded_contacts");

        mLayoutInflater = LayoutInflater.from(mActivity);
    }

    @Override
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

    public void startTest() {
        //判断权限是否开启
//        PackageManager pm = mActivity.getPackageManager();
//        boolean permission = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.READ_CONTACTS", "packageName"));
//        if (permission) {
//            CustomToast.showShort(mActivity,"拥有权限");
//        }else {
//            CustomToast.showShort(mActivity,"权限？？？？");
//        }

        if (mOtherLoginList != null && mOtherLoginList.size() > 0) {
            if (! mUploadedContactsB) {//如果没上传过联系人 开始上传
                waitForGetContact();
              //  LogUtil.LogShitou("上传过","=="+mOtherLoginList.size());
            } else {
                goToResultPage();
            //    LogUtil.LogShitou("没有上传过","=="+mOtherLoginList.size());
            }
        } else{
            CustomToast.showShort(mActivity, "请先绑定社交账号");

        }
       // waitForGetContact();
    }

    private void waitForGetContact() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {
                    try {
                        if (null == BaseApplication.getInstance().getContactBeans()) {
                            Thread.sleep(300);
                        } else {
                            Thread.sleep(300);
                            UIUtils.runInMainThread(new Runnable() {

                                @Override
                                public void run() {

                                    uploadContacts();
                                }
                            });
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 上传通讯录
     */
    public void uploadContacts() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("kol_uuid", mKolUuid);
        requestParams.put("contacts", GsonTools.listToJsonByAnnotation(BaseApplication.getInstance().getContactBeans()));
        LogUtil.LogShitou("上传的联系人", "==>" + GsonTools.listToJsonByAnnotation(BaseApplication.getInstance().getContactBeans()));
        HttpRequest.getInstance().post(true, HelpTools.getUrl("api/v2/influences/bind_contacts"), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {

                LogUtil.LogShitou("上传联系人", response);
                OtherLoginListBean authListBean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                if (authListBean.getError() == 0) {
                    mKolUuid = authListBean.getKol_uuid();
                    goToResultPage();
                }
            }
        });
    }

    private void goToResultPage() {

        Intent intent = new Intent(mActivity, SocialInfluenceActivity.class);
       // intent.putExtra("kol_uuid", mKolUuid);
      //  intent.putExtra("haveResult", true);
        mActivity.startActivity(intent);
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
                        mRequestParams.put("refresh_token", String.valueOf(res.get("refresh_token")));//微博令牌刷新token
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

                            OtherLoginListBean otherLoginListBean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                            if (otherLoginListBean == null) {
                                return;
                            }
                            updateView(otherLoginListBean.getIdentities());
                        }
                    });
                }
            });
        }
    }

    private void updateView(ArrayList<OtherLoginListBean.OtherLoginBean> otherLoginList) {

        mOtherLoginList = otherLoginList;
        if (mOtherLoginList.size() == 1) {
            mIUserView.setBindSocialTV(View.INVISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofFloat(mIUserView.getLinearLayout(), "translationY", 0f, UIUtils.getDimens(R.dimen.bind_social_move_length));
            animation.setDuration(400);
            animation.start();
        } else if (mOtherLoginList.size() == 0) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(mIUserView.getLinearLayout(), "translationY", UIUtils.getDimens(R.dimen.bind_social_move_length), 0f);
            mIUserView.setBindSocialTV(View.VISIBLE);
            animation.setDuration(400);
            animation.start();
        }

        ListView bindListView = mIUserView.getBindListView();
        BindSocialAdapter bindsocialAdapter = new BindSocialAdapter();
        BindSocialOnItemClickListener bindSocialOnItemClickListener = new BindSocialOnItemClickListener();
        bindListView.setAdapter(bindsocialAdapter);
        bindListView.setOnItemClickListener(bindSocialOnItemClickListener);
    }

    public class BindSocialOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            showPopWindow(mOtherLoginList.get(position));
        }
    }

    /**
     * 解除绑定弹窗
     *
     * @param bean
     */
    private void showPopWindow(final OtherLoginListBean.OtherLoginBean bean) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        final CustomDialogManager cdm = new CustomDialogManager(mActivity, view);

        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cdm.dismiss();
                unbindOtherLoginAccount(bean);
            }
        });

        cancelTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cdm.dismiss();
            }
        });

        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.BOTTOM);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_animations);
        cdm.showDialog();
    }

    /**
     * 解除绑定接口
     *
     * @param bean
     */
    private void unbindOtherLoginAccount(OtherLoginListBean.OtherLoginBean bean) {

        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("provider", bean.getProvider());
        mRequestParams.put("uid", bean.getUid());
        mRequestParams.put("kol_uuid", mKolUuid);

        HttpRequest.getInstance().post(true, HelpTools.getUrl(CommonConfig.UNBIND_IDENTITY_URL), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {

                Log.e("xxfigo", "UNBIND  onResponse=" + response);
                OtherLoginListBean otherLoginListBean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                if (otherLoginListBean == null) {
                    return;
                }
                updateView(otherLoginListBean.getIdentities());
            }
        });
    }

    public class BindSocialAdapter extends BaseAdapter {

        private int[] drawables = new int[]{R.mipmap.login_weibo, R.mipmap.login_weixin, R.mipmap.login_qq};

        @Override
        public int getCount() {

            return mOtherLoginList.size();
        }

        @Override
        public OtherLoginListBean.OtherLoginBean getItem(int position) {

            return mOtherLoginList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder = null;
            OtherLoginListBean.OtherLoginBean item = getItem(position);

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.bind_social_list_item, null);
                holder = new Holder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.civHeadimage = (ImageView) convertView.findViewById(R.id.civ_headimage);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvAccount = (TextView) convertView.findViewById(R.id.tv_account);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            BitmapUtil.loadImage(mActivity, item.getAvatar_url(), holder.civHeadimage, R.mipmap.head_default_normal);

            holder.tvName.setText(item.getName());
            holder.tvAccount.setText(item.getUid());
            if (item.getProvider().equals("wechat")) {
                holder.ivIcon.setBackgroundResource(drawables[1]);
                holder.tvAccount.setText("微信");
            } else if (item.getProvider().equals("weibo")) {
                holder.ivIcon.setBackgroundResource(drawables[0]);
                holder.tvAccount.setText("新浪微博");
            } else if (item.getProvider().equals("qq")) {
                holder.ivIcon.setBackgroundResource(drawables[2]);
                holder.tvAccount.setText("QQ");
            }
            return convertView;
        }
    }

    static class Holder {

        public ImageView ivIcon;
        public ImageView civHeadimage;
        public TextView tvName;
        public TextView tvAccount;
    }
}
