package com.robin8.rb.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * @author Figo
 */
public class BitmapUtil {

    static {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            path = Environment.getExternalStorageDirectory() + File.separator + "robin8" + File.separator;
        else
            path = BaseApplication.getContext().getCacheDir() + File.separator;
    }

    private static final String TAG = "BtimapUtil";

    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/articles_images/";

    private static int arrId[] = {R.mipmap.pic_kol_avatar_0, R.mipmap.pic_kol_avatar_1,
            R.mipmap.pic_kol_avatar_2, R.mipmap.pic_kol_avatar_3, R.mipmap.pic_kol_avatar_4};
    public static String path;

    /**
     * @param context
     * @return
     */
    public static String getPath(Context context) {
        String path = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName();
        if (hasSDCard) {
            path = ALBUM_PATH;
        } else {
            path = "/data/data/" + packageName;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public static Bitmap cropImage(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int x = 0;
        int y = 0;
        if (width < 1000 && height < 1000) {
            return bmp;
        }
        if (width > 1000) {
            x = (width - 1000) / 2;
            width = 1000;
        }
        if (height > 1000) {
            y = (height - 1000) / 2;
            height = 1000;
        }
        Bitmap cropBmp = Bitmap.createBitmap(bmp, x, y, width, height);
        bmp.recycle();
        return cropBmp;
    }

    public static void downLoadImage(Context context, String imgUrl) {
        DrawableTypeRequest<String> request = Glide.with(context).load(imgUrl);
        request.downloadOnly(200, 200);
    }

    public static void loadImage(Context context, String mImageUrl, ImageView to, int resid) {
        try {
            Glide.with(context).load(mImageUrl).skipMemoryCache(true).centerCrop().placeholder(resid).into(to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Context context, String mImageUrl, ImageView to, int resid,
                                 BitmapTransformation transformation) {
        Glide.with(context).load(mImageUrl).centerCrop().transform(transformation).placeholder(resid)
                // .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(to);
    }

    public static void loadImage(Context context, String mImageUrl, ImageView to) {
        if (TextUtils.isEmpty(mImageUrl) || to == null) {
            return;
        }
        try {
            Glide.with(context).load(mImageUrl).centerCrop().into(to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageNocrop(Context context, String mImageUrl, ImageView to) {
        if (TextUtils.isEmpty(mImageUrl) || to == null) {
            return;
        }
        try {
            Glide.with(context).load(mImageUrl).into(to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(Fragment fragment, String mImageUrl, ImageView to, int resid) {
        Glide.with(fragment).load(mImageUrl).centerCrop().fallback(resid).placeholder(resid)
                // .skipMemoryCache(true)
                .crossFade().into(to);
    }

    public static void loadImage(Fragment fragment, String mImageUrl, ImageView to) {
        Glide.with(fragment).load(mImageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(to);
    }

    public static void loadLocalImage(Context context, String uri, ImageView iv) {
        Glide.with(context).load(new File(uri)).centerCrop().into(iv);
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

    @SuppressLint("NewApi")
    public static long getBitmapSize(Bitmap bitmap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();

    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static String getCompressImagePath(String srcPath) {
        if (TextUtils.isEmpty(srcPath) || !new File(srcPath).exists())
            return null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1080f;//这里设置高度为800f
        float ww = 720;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = Math.round(newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = Math.round(newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(srcPath, bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 压缩图片
     *
     * @return
     */
    public static String compressImage(String path, Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        if (baos.toByteArray().length / 1024 > 1000)
            options = 20;
        else if (baos.toByteArray().length / 1024 > 2000)
            options = 10;
        while (baos.toByteArray().length / 1024 > 200) {  //循环判断如果压缩后图片是否大于200kb,大于继续压缩
            if (options <= 0)
                break;
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            //这里压缩options%，把压缩后的数据存放到baos中
            options -= 4;//每次都减少10
        }
        image.recycle();
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static int getBg() {
        int random = (int) (Math.random() * (5));
        return arrId[random];
    }

    /**
     * 保存方法
     */
    public static String saveBitmap(String path, Bitmap bitmap) {
        try {
            File file = new File(path + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            bitmap.recycle();
            out.flush();
            out.close();
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteBm(String mFinalPicturePath) {
        File file = new File(mFinalPicturePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 裁剪图片方法实现
     */
    public static void cropImageUri(Uri uri, int outputX, int outputY, int requestCode, int aspectX, int aspectY, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("scale", true);//黑边
        intent.putExtra("scaleUpIfNeeded", true);//黑边
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        activity.startActivityForResult(intent, requestCode);
    }

    public static Bitmap decodeUriAsBitmap(Uri uri, Activity activity) {
        Bitmap bitmap = null;
        try {
            //bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uri);
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
