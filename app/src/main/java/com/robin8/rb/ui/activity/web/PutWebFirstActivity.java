package com.robin8.rb.ui.activity.web;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.util.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.rong.imageloader.utils.L;

public class PutWebFirstActivity extends BaseActivity {

    private WebView webView;
    private int FILE_CHOOSER_RESULT_CODE = 1000;

    @Override
    public void setTitleView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_put_web_first, mLLContent, true);
        webView = ((WebView) view.findViewById(R.id.webview));
    }

    @Override
    public void initView() {
        final String loadurl = CommonConfig.PUT_WALLET;

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(loadurl);
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                L.e("error", error + "");
                handler.proceed();
            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        //        @Override
        //        public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
        //            Log.i("tag", "url="+url);
        //            Log.i("tag", "userAgent="+userAgent);
        //            Log.i("tag", "contentDisposition="+contentDisposition);
        //            Log.i("tag", "mimetype="+mimetype);
        //            Log.i("tag", "contentLength="+contentLength);
        //            Uri uri = Uri.parse(url);
        //            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //            startActivity(intent);
        //        }
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Toast t = Toast.makeText(PutWebFirstActivity.this, "需要SD卡。", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }
//            DownloaderTask task = new DownloaderTask();
//            task.execute(url);
        }
    }
//
//    //内部类
//    private class DownloaderTask extends AsyncTask<String, Void, String> {
//
//        public DownloaderTask() {
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            String url = params[0];
//            //          Log.i("tag", "url="+url);
//            String fileName = url.substring(url.lastIndexOf("/") + 1);
//            fileName = URLDecoder.decode(fileName);
//            Log.i("tag", "fileName=" + fileName);
//
//            //   File directory = getExternalFilesDir(null);
//            File directory = Environment.getExternalStorageDirectory();
//
//            File file = new File(directory, fileName);
//            Log.i("tag", "Path111=" + file.getAbsolutePath());
//            Log.i("tag", "Path222=" + file.getPath());
//            if (file.exists()) {
//                Log.i("tag", "The file has already exists.");
//                return fileName;
//            }
//            try {
//                HttpClient client = new DefaultHttpClient();
//                //                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
//                HttpGet get = new HttpGet(url);
//                HttpResponse response = client.execute(get);
//                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
//                    HttpEntity entity = response.getEntity();
//                    InputStream input = entity.getContent();
//
//                    writeToSDCard(fileName, input);
//
//                    input.close();
//                    //                  entity.consumeContent();
//                    return fileName;
//                } else {
//                    return null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            // TODO Auto-generated method stub
//            super.onCancelled();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            closeProgressDialog();
//            if (result == null) {
//                Toast t = Toast.makeText(PutWebFirstActivity.this, "连接错误！请稍后再试！", Toast.LENGTH_LONG);
//                t.setGravity(Gravity.CENTER, 0, 0);
//                t.show();
//                return;
//            }
//
//            Toast t = Toast.makeText(PutWebFirstActivity.this, "已保存到SD卡。", Toast.LENGTH_LONG);
//            t.setGravity(Gravity.CENTER, 0, 0);
//            t.show();
//            // File directory =getExternalFilesDir(null);
//            File directory = Environment.getExternalStorageDirectory();
//            File file = new File(directory, result);
//            Log.i("tag", "Path=" + file.getAbsolutePath());
//
//            Intent intent = getFileIntent(file);
//
//            startActivity(intent);
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {
//            // TODO Auto-generated method stub
//            super.onProgressUpdate(values);
//        }
//    }

    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;

    private class MyWebChromeClient extends WebChromeClient {


        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖 webview的WebViewClient对象。
        public boolean shouldOverviewUrlLoading(WebView view, String url) {
            L.i("shouldOverviewUrlLoading");
            view.loadUrl(url);
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            L.i("onPageStarted");
            //  showProgress();
        }

        public void onPageFinished(WebView view, String url) {
            L.i("onPageFinished");
            //  closeProgress();
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            L.i("onReceivedError");
            //  closeProgress();
        }

        //For Android API < 11 (3.0 OS)
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            uploadMessage = valueCallback;
            openFileChooser(uploadMessage);
        }

        // Android 3.0以上
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType);
        }

        //For Android API >= 11 (3.0 OS)
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            uploadMessage = valueCallback;
            openFileChooser(uploadMessage, acceptType, null);
        }

        //For Android API >= 21 (5.0 OS)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            uploadMessageAboveL = filePathCallback;
            final boolean allowMultiple = fileChooserParams.getMode() == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选
            openFileInput(null, filePathCallback, allowMultiple);
            return true;
        }
    }

    @SuppressLint("NewApi")
    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
        //Android 5.0以下版本
        if (uploadMessage != null) {
            uploadMessage.onReceiveValue(null);
        }
        uploadMessage = fileUploadCallbackFirst;

        //Android 5.0及以上版本
        if (uploadMessageAboveL != null) {
            uploadMessageAboveL.onReceiveValue(null);
        }
        uploadMessageAboveL = fileUploadCallbackSecond;
        //  File file = getExternalFilesDir(null);
        File file = Environment.getExternalStorageDirectory();
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= 18) {
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }
        //  i.addCategory("android.intent.category.DEFAULT");
        i.setDataAndType(Uri.fromFile(file), "*/*");
        LogUtil.LogShitou("选择文件路径", "===" + Uri.fromFile(file));
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    //Android 5.0以下版本
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(intent.getData());
                        uploadMessage = null;
                    } else if (uploadMessageAboveL != null) {//Android 5.0及以上版本
                        Uri[] dataUris = null;

                        try {
                            if (intent.getDataString() != null) {
                                dataUris = new Uri[]{Uri.parse(intent.getDataString())};
                            } else {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    if (intent.getClipData() != null) {
                                        final int numSelectedFiles = intent.getClipData().getItemCount();

                                        dataUris = new Uri[numSelectedFiles];

                                        for (int i = 0; i < numSelectedFiles; i++) {
                                            dataUris[i] = intent.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        uploadMessageAboveL.onReceiveValue(dataUris);
                        uploadMessageAboveL = null;
                    }
                }
            } else {
                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                } else if (uploadMessageAboveL != null) {
                    uploadMessageAboveL.onReceiveValue(null);
                    uploadMessageAboveL = null;
                }
            }
        }
    }

    private void openImageChooserActivity() {
        ///storage/emulated/0/keys.txt
        File file = new File("/storage/emulated/0/keys.txt");
        File file1 = new File(file.getParent());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(file1), "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_CHOOSER_RESULT_CODE);
    }


    private ProgressDialog mDialog;

    private void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(PutWebFirstActivity.this);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    mDialog = null;
                }
            });
            mDialog.show();

        }
    }

    private void closeProgressDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public Intent getFileIntent(File file) {
        //       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type=" + type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public void writeToSDCard(String fileName, InputStream input) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //  File directory = getExternalFilesDir(null);
            File directory = Environment.getExternalStorageDirectory();

            File file = new File(directory, fileName);
            //          if(file.exists()){
            //              Log.i("tag", "The file has already exists.");
            //              return;
            //          }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int j = 0;
                while ((j = input.read(b)) != - 1) {
                    fos.write(b, 0, j);
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Log.i("tag", "NO SDCard.");
        }
    }

    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
      /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";//
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
        /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        }
        //      else if(end.equals("pptx")||end.equals("ppt")){
        //        type = "application/vnd.ms-powerpoint";
        //      }else if(end.equals("docx")||end.equals("doc")){
        //        type = "application/vnd.ms-word";
        //      }else if(end.equals("xlsx")||end.equals("xls")){
        //        type = "application/vnd.ms-excel";
        //      }
        else {
            //        /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }

    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }
}
