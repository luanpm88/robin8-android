package com.robin8.rb.activity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.util.ActivityManagerUtils;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.RegExpUtil;

public class ChangeHttpActivity extends BaseActivity {

    public EditText edit_one;
    public EditText edit_two;
    public EditText edit_three;
    public TextView tvDel;
    public TextView tvDelTwo;
    public TextView tvDelThree;


    @Override
    public void setTitleView() {
        mTVCenter.setText("域名更换");
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setOnClickListener(this);
        mBottomTv.setText("提交域名");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_change_http, mLLContent, true);
        RelativeLayout llOne = (RelativeLayout) view.findViewById(R.id.ll_two);
        RelativeLayout llThree = (RelativeLayout) view.findViewById(R.id.ll_three);

        edit_one = ((EditText) view.findViewById(R.id.edit_one));
        edit_two = ((EditText) view.findViewById(R.id.edit_two));
        edit_three = ((EditText) view.findViewById(R.id.edit_three));

        tvDel = ((TextView) view.findViewById(R.id.tv_delete));
        tvDelTwo = ((TextView) view.findViewById(R.id.tv_delete_two));
        tvDelThree = ((TextView) view.findViewById(R.id.tv_delete_three));
        TextView tv_now_http = (TextView) view.findViewById(R.id.tv_now);
        if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.SERVER))){
            tv_now_http.setText(("当前域名==>> "+ CommonConfig.SERVICE+""));
        }else {
            tv_now_http.setText(("当前域名==>>  "+HelpTools.getCommonXml(HelpTools.SERVER)+""));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager cm = (ClipboardManager) BaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm.getPrimaryClipDescription() != null && cm.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData cd = cm.getPrimaryClip();
                ClipData.Item item = cd.getItemAt(0);
                String itemStr = item.getText().toString();
                if (! TextUtils.isEmpty(itemStr)) {
                    edit_one.setText(itemStr);
                    tvDel.setVisibility(View.VISIBLE);
                }
            }
        }
        tvDel.setOnClickListener(this);
        IconFontHelper.setTextIconFont(tvDel, R.string.icons_delete2);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String edit_text = edit_one.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tv_bottom:
                if (TextUtils.isEmpty(edit_text)) {
                    CustomToast.showShort(ChangeHttpActivity.this, "请输入域名");
                    return;
                } else {
                    if (! RegExpUtil.isUrl(edit_text)) {
                        CustomToast.showShort(ChangeHttpActivity.this, "域名格式错误");
                    } else {
                        if (!(edit_text).endsWith("/")) {
                            HelpTools.insertCommonXml(HelpTools.SERVER, (edit_text + "/"));
                        } else {
                            HelpTools.insertCommonXml(HelpTools.SERVER, (edit_text));
                        }
                        CacheUtils.putString(ChangeHttpActivity.this, SPConstants.MINE_DATA, null);
                        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN_OUT);
                        //HelpTools.insertCommonXml(HelpTools.NATIVE,"");
                        HelpTools.insertCommonXml(HelpTools.MyKolId, "");
                        HelpTools.insertCommonXml(HelpTools.PagerData, "");
                        HelpTools.insertCommonXml(HelpTools.isLeader, "");
                        HelpTools.clearInfo();
                        ActivityManagerUtils.getInstance().exit();
                    }
                }
                break;
            case R.id.tv_delete:
                edit_one.setText("");
                tvDel.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
