package com.robin8.rb.module.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.module.mine.model.InviteFridensModel;
import com.robin8.rb.module.mine.model.InviteModel;
import com.robin8.rb.module.mine.model.MyInviteModel;
import com.robin8.rb.module.share.CustomShareHelper;
import com.robin8.rb.module.social.view.LinearLayoutForListView;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 Created by IBM on 2016/8/14. */
public class InviteFriendsActivity extends BaseActivity {
    private static final String IMAGE_URL = "http://7xq4sa.com1.z0.glb.clouddn.com/robin8_icon.png";
    private static final String TITLE_URL = CommonConfig.SERVICE + "/invite?inviter_id=";

   private TextView tvRewordInfo;
   private TextView tvRewordInfoTwo;
   private TextView tvInviteNumber;
   private TextView tvRewordMoney;

    private CustomDialogManager mCustomDialogManager;
    private CustomShareHelper mCustomShareHelper;
    private ImageView mImgDown;
    private LinearLayout mRuleLayout;
    private LinearLayoutForListView mListView;

    private ArrayList<OtherLoginListBean.OtherLoginBean> mOtherLoginList;
    private boolean mUploadedContactsB;
    private LinearLayout mEmptyLayout;
    private List<InviteModel.KolUsersBean> mDataList;
    private TextView tvInvite;
    private WProgressDialog mWProgressDialog;
    private List<MyInviteModel.UserBean> myList;

    @Override
    public void setTitleView() {
        mTVCenter.setText(getString(R.string.invite_friends));
    }
    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_invite_friends, mLLContent, true);
        mBottomTv.setVisibility(View.GONE);
        mBottomTv.setOnClickListener(this);
        mBottomTv.setText(R.string.invite_instantly);
        tvRewordMoney = ((TextView) view.findViewById(R.id.tv_reword_money));
        tvInviteNumber = ((TextView) view.findViewById(R.id.tv_invite_number));
        tvRewordInfoTwo = ((TextView) view.findViewById(R.id.tv_reword_info_two));
        tvRewordInfo = ((TextView) view.findViewById(R.id.tv_reword_info));
        mImgDown = ((ImageView)view.findViewById(R.id.img_down));
        mRuleLayout = ((LinearLayout)view.findViewById(R.id.ll_rule));
        mImgDown.setOnClickListener(this);
        mListView = ((LinearLayoutForListView)view.findViewById(R.id.list_content));
        mEmptyLayout = ((LinearLayout)view.findViewById(R.id.ll_empty));
      // tvRewordInfo.setText(Html.fromHtml("邀请好友下载并完成一个活动,立得<font color=#ecb200>" + "2元" + "</font>奖励"));
        tvInvite = ((TextView) view.findViewById(R.id.tv_invite_friend));
        tvInvite.setOnClickListener(this);
        //--联系人处理
        try {
            String s = GsonTools.listToJsonByAnnotation(BaseApplication.getInstance().getContactBeans());
            if (selfPermissionGranted(Manifest.permission.READ_PHONE_STATE) == false) {
                ActivityCompat.requestPermissions(InviteFriendsActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                if (TextUtils.isEmpty(s)) {
                    showRejectDialog(InviteFriendsActivity.this);
                } else {
                    if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.InviteDialog))) {
                        showRejectDialog(InviteFriendsActivity.this);
                    } else {
                        if (HelpTools.getCommonXml(HelpTools.InviteDialog).equals("ok")) {
                            startTest();
                        } else {
                            mEmptyLayout.setVisibility(View.VISIBLE);
                            mListView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //---------
        initData();
    }

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private int targetSdkVersion;

    public boolean selfPermissionGranted(String permission) {
        boolean result = true;
        try {
            final PackageInfo info = InviteFriendsActivity.this.getPackageManager().getPackageInfo(InviteFriendsActivity.this.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        result = PermissionChecker.checkSelfPermission(InviteFriendsActivity.this, permission) == PermissionChecker.PERMISSION_GRANTED;
        return result;
    }

    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(InviteFriendsActivity.this, Manifest.permission.READ_CONTACTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (! ActivityCompat.shouldShowRequestPermissionRationale(InviteFriendsActivity.this, Manifest.permission.READ_CONTACTS)) {
                showMessageOKCancel("您已勾选不再询问,请您前往'设置-应用管理'允许相应权限,否则该功能将无法正常使用", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                return;
            }
            showMessageOKCancel("请您允许获取联系人权限,否则此功能将无法正常使用", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(InviteFriendsActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_ASK_PERMISSIONS);
                }
            });
            return;
        } else {
            showRejectDialog(InviteFriendsActivity.this);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(InviteFriendsActivity.this).setMessage(message).setPositiveButton("确定", okListener).setNegativeButton("取消", null).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showRejectDialog(InviteFriendsActivity.this);
                } else {
                    insertDummyContactWrapper();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void startTest() {
        //判断权限是否开启
        //        if (ContextCompat.checkSelfPermission(InviteFriendsActivity.this, Manifest.permission.READ_CONTACTS != PackageManager.PERMISSION_GRANTED)){
        //
        //        }
        waitForGetContact();
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

    private void uploadContacts() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final String s = GsonTools.listToJsonByAnnotation(BaseApplication.getInstance().getContactBeans());
        MyInviteModel myInviteModel = GsonTools.jsonToBean(("{\"user\":" + s + "}"), MyInviteModel.class);
        myList = myInviteModel.getUser();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("contacts", s);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.INVITE_CONTACTS), params, new RequestCallback() {

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
              //  LogUtil.LogShitou("上传联系人", response);
                final InviteModel inviteModel = GsonTools.jsonToBean(response, InviteModel.class);

                if (inviteModel.getKol_users().size() == 0) {
                    CustomToast.showShort(InviteFriendsActivity.this, "您尚未添加联系人");
                    mListView.setVisibility(View.GONE);
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }else {
                    mListView.setVisibility(View.VISIBLE);
                    mEmptyLayout.setVisibility(View.GONE);
                }
                mDataList = inviteModel.getKol_users();
                MyListAdapter adapter = new MyListAdapter(mDataList);
                mListView.setAdapter(adapter);
                //                LogUtil.LogShitou("联系人", "==>" + mDataList.get(1).getMobile_number().getName());
                //   mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //
                //     @Override
                //     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //
                //       }
                //  });
            }
        });

    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INVITE;
        super.onResume();
    }

    private void initData() {
        BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.INVITE_FRIENDS_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.logXXfigo("邀请好友+" + response);
                InviteFridensModel inviteFridensModel = GsonTools.jsonToBean(response, InviteFridensModel.class);
                if (inviteFridensModel != null && inviteFridensModel.getError() == 0) {
                    updateView(inviteFridensModel);
                }
            }
        });
    }

    private void updateView(InviteFridensModel inviteFridensModel) {
        tvRewordMoney.setText("¥ " + StringUtil.deleteZero(inviteFridensModel.getInvite_amount()));
        tvInviteNumber.setText(String.valueOf(inviteFridensModel.getInvite_count()));
        if (inviteFridensModel.getInvite_code() == 0) {
            tvRewordInfo.setText(Html.fromHtml("邀请好友下载并完成一个活动,立得<font color=#ecb200>" + "2元" + "</font>奖励"));
        } else {
           // String s = "邀请好友下载登录并完成一个活动";
            tvRewordInfo.setText(Html.fromHtml("邀请好友下载登录并完成一个<font color=#ecb200>活动</font>"));
           // StringUtil.setTextViewSpan(tvRewordInfo,0,13,15,getResources().getColor(R.color.yellow_custom));
            tvRewordInfoTwo.setText(Html.fromHtml("立得 <font color=#ecb200><b>2</b>元</font> 现金奖励（您的邀请码："+String.valueOf(inviteFridensModel.getInvite_code())+"）"));

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
        switch (v.getId()) {
            case R.id.tv_invite_friend:
                startTest();
                break;
            case R.id.tv_bottom:
                showInviteDialog();
                break;
            case R.id.tv_weixin:
                share(Wechat.NAME);
                break;
            case R.id.tv_wechatmoments:
                share(WechatMoments.NAME);
                break;
            case R.id.tv_weibo:
                share(SinaWeibo.NAME);
                break;
            case R.id.tv_qq:
                share(QQ.NAME);
                break;
            case R.id.tv_qonze:
                share(QZone.NAME);
                break;
            case R.id.img_down:
                if (mRuleLayout.getVisibility() == View.VISIBLE) {
                    mImgDown.setImageResource(R.mipmap.icon_down);
                    mRuleLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_out));
                    mRuleLayout.setVisibility(View.GONE);

                } else {
                    mRuleLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_in));
                    mRuleLayout.setVisibility(View.VISIBLE);
                    mImgDown.setImageResource(R.mipmap.icon_invite_up);
                }
                break;
        }
    }

    /**
     弹出分享面板
     @param platName
     */
    private void share(String platName) {

        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }

        int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
        CustomToast.showShort(this, "正在前往分享...");
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(getString(R.string.share_invite_friends_text) + TITLE_URL + String.valueOf(id));
        } else {
            oks.setText(getString(R.string.share_invite_friends_text));
        }
        oks.setTitle(getString(R.string.share_invite_friends_title));

        oks.setTitleUrl(TITLE_URL + String.valueOf(id));
        oks.setImageUrl(IMAGE_URL);
        if (Wechat.NAME.equals(platName) || WechatMoments.NAME.equals(platName)){
            oks.setUrl(TITLE_URL + String.valueOf(id));
        }
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(this);
    }

    private void showInviteDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.invite_friends_dialog, null);
        TextView weixinTV = (TextView) view.findViewById(R.id.tv_weixin);
        TextView wechatmomentsTV = (TextView) view.findViewById(R.id.tv_wechatmoments);
        TextView weiboTV = (TextView) view.findViewById(R.id.tv_weibo);
        TextView qqTV = (TextView) view.findViewById(R.id.tv_qq);
        TextView qonzeTV = (TextView) view.findViewById(R.id.tv_qonze);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        mCustomDialogManager = new CustomDialogManager(this, view);
        weixinTV.setOnClickListener(this);
        wechatmomentsTV.setOnClickListener(this);
        weiboTV.setOnClickListener(this);
        qqTV.setOnClickListener(this);
        qonzeTV.setOnClickListener(this);
        cancelTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(InviteFriendsActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(InviteFriendsActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(InviteFriendsActivity.this, "分享取消");
        }
    }

    public void showRejectDialog(final Activity activity) {
        // String rejectReason = campaignInviteEntity.getReject_reason();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_white, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_cancel);

        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取通讯录权限
                HelpTools.insertCommonXml(HelpTools.InviteDialog, "ok");
                cdm.dismiss();
                startTest();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cdm.dismiss();
                HelpTools.insertCommonXml(HelpTools.InviteDialog, "no");
                mEmptyLayout.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    class MyListAdapter extends BaseAdapter {
        private List<InviteModel.KolUsersBean> mDatas;

        public MyListAdapter(List<InviteModel.KolUsersBean> mList) {
            this.mDatas = mList;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public InviteModel.KolUsersBean getItem(int i) {
            return mDatas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(InviteFriendsActivity.this).inflate(R.layout.invite_contact_item, null);
                convertView.setTag(new Holder(convertView));
            }
            final Holder holder = (Holder) convertView.getTag();
            final InviteModel.KolUsersBean item = getItem(i);
            if (myList != null) {
                if (myList.size() != 0) {
                    for (int j = 0; j < myList.size(); j++) {
                        if (! TextUtils.isEmpty(myList.get(i).getMobile())) {
                            if (myList.get(i).getMobile().equals(mDatas.get(i).getMobile_number())) {
                                if (TextUtils.isEmpty(myList.get(i).getName())) {
                                    holder.mTvName.setText("空");
                                } else {
                                    holder.mTvName.setText(myList.get(i).getName());
                                }
                            }
                        }
                    }
                }
            }

            holder.mTvMobile.setText(item.getMobile_number());
            if (item.getStatus().equals("not_invited")) {
                holder.mTvStatus.setText("邀请");
                holder.mTvStatus.setBackgroundResource(R.drawable.shape_solid_blue);
                holder.mTvStatus.setClickable(true);
                holder.mTvStatus.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        BasePresenter mBasePresenter = new BasePresenter();
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("mobile_number", item.getMobile_number());
                        //LogUtil.LogShitou("发送的联系人", "==>" + i);
                        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.INVITE_SEND_SMS), requestParams, new RequestCallback() {

                            @Override
                            public void onError(Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                                if (baseBean.getError() == 0) {
                                    holder.mTvStatus.setText("已邀请");
                                    holder.mTvStatus.setBackgroundResource(R.drawable.shape_solid_gray);
                                } else {
                                    CustomToast.showShort(InviteFriendsActivity.this, "邀请失败");
                                }


                            }
                        });
                    }
                });
            } else if (item.getStatus().equals("already_invited")) {
                holder.mTvStatus.setText("已邀请");
                holder.mTvStatus.setBackgroundResource(R.drawable.shape_solid_gray);
                holder.mTvStatus.setClickable(false);
            } else if (item.getStatus().equals("already_kol")) {
                holder.mTvStatus.setText("KOL");
                holder.mTvStatus.setBackgroundResource(R.drawable.shape_solid_blue);
                holder.mTvStatus.setClickable(false);
            }

            return convertView;
        }

    }

    class Holder {
        private TextView mTvName;
        private TextView mTvMobile;
        private TextView mTvStatus;

        Holder(View view) {

            mTvName = ((TextView) view.findViewById(R.id.tv_user_name));
            mTvMobile = ((TextView) view.findViewById(R.id.tv_mobile));
            mTvStatus = ((TextView) view.findViewById(R.id.tv_status));

        }
    }
}
