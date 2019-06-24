package com.robin8.rb.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.robin8.rb.ui.module.mine.model.GlobalCityModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    @SuppressLint("NewApi")
    public static boolean saveFile(Context context, String sFileName, String filePath, String sData) {
        if (Build.VERSION.SDK_INT < 8) {
            return false;
        }

        boolean bRet = true;
        FileOutputStream fos;
        try {
            File file = context.getExternalFilesDir(filePath);
            String path = file.getAbsolutePath();

            fos = new FileOutputStream(new File(path + "/" + sFileName));
            // fos = context.openFileOutput(sFileName, context.MODE_PRIVATE);
            fos.write(sData.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            bRet = false;
        }

        return bRet;
    }

    public static boolean saveInterFile(Context context, String sFileName, String filePath, String sData) {
        boolean bRet = true;
        FileOutputStream fos;
        try {
            File fileRoot = context.getFilesDir();
            File file = new File(fileRoot, filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            String path = file.getAbsolutePath();

            fos = new FileOutputStream(new File(path + "/" + sFileName));
            // fos = context.openFileOutput(sFileName, context.MODE_PRIVATE);
            fos.write(sData.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            bRet = false;
        }

        return bRet;
    }

    public static boolean saveToPhoto(Context context, String sFileName, byte[] sData, boolean needRefresh) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = sFileName;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sData);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                + file.getAbsolutePath())));
        return true;
    }

    @SuppressLint("NewApi")
    public static boolean saveImgFile(Context context, String sFileName, byte[] sData, boolean needRefresh) {
        boolean bRet = true;
        FileOutputStream fos;
        try {
            File file = context.getExternalFilesDir("photo");
            String path = file.getAbsolutePath();
            File imgFile = new File(path + "/" + sFileName);
            fos = new FileOutputStream(imgFile);
            fos.write(sData);
            fos.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(imgFile);
            intent.setData(uri);
            context.sendBroadcast(intent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bRet = false;
        } catch (IOException e) {
            e.printStackTrace();
            bRet = false;
        }

        return bRet;
    }

    @SuppressLint("NewApi")
    public static boolean saveImgFile(Context context, String sFileName, byte[] sData) {
        if (Build.VERSION.SDK_INT < 8) {
            return false;
        }

        boolean bRet = true;
        FileOutputStream fos;
        try {
            File file = context.getExternalFilesDir("webCache");
            String path = file.getAbsolutePath();

            fos = new FileOutputStream(new File(path + "/" + sFileName));
            // fos = context.openFileOutput(sFileName, context.MODE_PRIVATE);
            fos.write(sData);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
            bRet = false;
        }

        return bRet;
    }

    public static String saveBitmapToSD(Bitmap bmp, String fileName) throws IOException {

        if (FileUtils.isSDcardExist()) {
            String path = FileUtils.getExtendedStorePath() + "/Photo/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(path + fileName);

            FileOutputStream fos = new FileOutputStream(file);
            try {
                if (fos != null) {
                    // 第一参数是图片格式，第二个是图片质量，第三个是输出流
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    // 用完关闭
                    fos.flush();
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                return file.getAbsolutePath();
            }
        }

        return "";
    }


    /**
     * 判断存储卡是否存在
     */
    public static boolean isSDcardExist() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    public static String getInterFilePath(String fpath, String sFileName, Context context) {
        File fileRoot = context.getFilesDir();
        File filePath = new File(fileRoot, fpath);
        String path = filePath.getAbsolutePath() + "/" + sFileName;
        return path;
    }

    public static String getAppFilePath(String sFileName, Context context) {
        File file = context.getFilesDir();

        String path = file.getAbsolutePath() + "/" + sFileName;
        return path;
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

    public static void writeObjectData(Context context, String path, String fileName, Object paramObject) {
        try {
            File localFile = new File(getExternalCacheDir(context, path), fileName);
            if (!localFile.exists())
                localFile.createNewFile();
            ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(new FileOutputStream(localFile));
            localObjectOutputStream.writeObject(paramObject);
            localObjectOutputStream.close();
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }

    }

    public static void deleteFileCacheByPath(Context context, String path) {
        try {
            File externalCacheDir = getExternalCacheDir(context, path);
            deleteFilesByDirectory(externalCacheDir);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 删除
     *
     * @param directory
     */
    public static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFilesByDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
    }


    /**
     * 将图片从webCache目录拷贝到Boohee目录
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static int copyWebImgToPhoto(Context context, String fileName) {
        if (Build.VERSION.SDK_INT < 8) {
            return -1;
        }

        try {
            File file = context.getExternalFilesDir("webCache");
            String srcPath = file.getAbsolutePath() + "/" + fileName;
            InputStream fosfrom = new FileInputStream(srcPath);

            File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            File targetFile = new File(appDir, fileName);
            OutputStream fosto = new FileOutputStream(targetFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(targetFile);
            intent.setData(uri);
            context.sendBroadcast(intent);

            return 0;

        } catch (Exception ex) {
            return -1;
        }

    }

    /**
     * @param context
     */
    public static boolean write(Context context, String fileName, String content) {
        if (fileName == null || content == null) {
            return false;
        }

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }

    }

    public static long getFileSize(File f) throws Exception {
        long size = 0;
        if (f == null) {
            return size;
        }
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS == 0) {
            fileSizeString = "0.00B";
        } else if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取扩展的存储介质的路径，比如SD卡。
     */
    public static String getExtendedStorePath() {
        if (!isSDcardExist()) {
            return null;
        }
        File extendedStoreDir = Environment.getExternalStorageDirectory();

        if (extendedStoreDir == null) {
            return null;
        }
        return extendedStoreDir.toString() + "/";
    }

    /**
     * 根据uri获取图片的绝对路径
     *
     * @param activity
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Activity activity, Uri uri) {
        String path = "";
        try {
            // can post image
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri,
                    proj,                 // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null);                 // Order-by clause (ascending by name)

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            path = cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            path = uri.getPath();
        }
        return path;
    }


    //读取CSV文件
    public static List<GlobalCityModel> readCSV(String path, Activity activity){

        List<GlobalCityModel> list=new ArrayList<GlobalCityModel>();
        Scanner scanner;
        try {
            scanner=new Scanner(activity.getResources().getAssets().open(path),"UTF-8");
            scanner.nextLine();//读下一行,把表头越过。不注释的话第一行数据就越过去了
            int a=0;
            while (scanner.hasNextLine()) {
                String sourceString = scanner.nextLine();
                Log.e("source-->", sourceString);
                Pattern pattern = Pattern.compile("[^,]*,");
                Matcher matcher = pattern.matcher(sourceString);
                String[] lines=new String[8];
                int i=0;
                while(matcher.find()) {
                    String find = matcher.group().replace(",", "");
                    lines[i]=find.trim();
                    Log.e("City", "find="+find+",i="+i+",lines="+lines[i]);
                    i++;
                }
                GlobalCityModel bean = new GlobalCityModel(lines[0],lines[1]);
                list.add(bean);
                a++;
                i=0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
