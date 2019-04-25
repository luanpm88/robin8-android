package com.robin8.rb.ui.module.share;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.robin8.rb.R;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;


/**
 * 第三方分享辅助类:处理QQ,QQ空间,微信,朋友圈,新浪微博的分享
 *
 * @author weichao
 */
public class CustomShareHelper {

    public static final int TYPE_WECHART = 0;// 分享类型:微信
    public static final int TYPE_WECHATMOMENTS = 1;// 分享类型:微信朋友圈

    public static final int TYPE_QQ = 0;// 分享类型:QQ
    public static final int TYPE_QQZONE = 1;// 分享类型:QQ空间

    public static boolean IS_SHARING = false;// 是否正在分享

    private Context mContext;
    private ShareParams mShareParams;// 分享参数

    private byte[] mThumb;// 微信分享图片byte数组

    public CustomShareHelper(Context context, ShareParams shareParams) {
        this.mContext = context;
        this.mShareParams = shareParams;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        mThumb = BitmapUtil.bitmapToBytes(bitmap);
        bitmap.recycle();
    }

    /**
     * 拷贝到剪贴板
     */
    @SuppressLint("NewApi")
    public void copyToClipboard(String str) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ClipboardManager clipboardManager = (ClipboardManager) mContext
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("simple text", str);
                clipboardManager.setPrimaryClip(clip);
                CustomToast.showShort(mContext, R.string.share_copy_sucess);
            } else {
                CustomToast.showShort(mContext, R.string.system_not_support);
            }
        } catch (Exception e) {
            e.printStackTrace();
            CustomToast.showShort(mContext, R.string.share_copy_failure);
        }

    }

}
