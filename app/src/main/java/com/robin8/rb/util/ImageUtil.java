package com.robin8.rb.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;

public class ImageUtil {

    public static String getImageUri(Context context, String phone) {
        StringBuilder sb = new StringBuilder().append("file://").append(getExternalCacheDir(context, "icon"))
                .append("/").append(phone).append(".png");
        return sb.toString();
    }

    public static File getExternalCacheDir(Context context, String path) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), path);
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }

        }
        return appCacheDir;
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        int i = drawable.getIntrinsicWidth();
        int j = drawable.getIntrinsicHeight();
        Bitmap.Config config;

        if (drawable.getOpacity() != -1)
            config = Bitmap.Config.ARGB_8888;
        else
            config = Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(i, j, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, i, j);
        drawable.draw(canvas);
        return bitmap;
    }


}
