package com.robin8.rb.http.xutil;


import android.util.Log;

import com.robin8.rb.util.CommonUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.TextCodec;

/**
 * @author DLJ
 * @Description 网络请求工具类
 * @date ${date} ${time}
 * ${tags}
 */
public class MyHttp {
    private static String key = "Robin888";
    private IHttpUtils utils;
    private ExecutorService executorService;
    private static MyHttp instance;

    private MyHttp(IHttpUtils utils) {
        this.utils = utils;
        init();
    }

    private MyHttp() {
        init();
    }


    public static MyHttp getInstance(IHttpUtils utils) {
        if (instance == null) {
            synchronized (MyHttp.class) {
                if (instance == null)
                    instance = new MyHttp();
            }
        }
        if (instance.utils == null)
            instance.utils = XUtilsHttpUtils.getInstance();
        return instance;
    }

    public void init() {
        //因为格式解析可能费时因此采用线程池
        executorService = Executors.newFixedThreadPool(5);
    }

    public void post(final Map<String, Object> requestMap, final IHttpCallBack callBack, final
    boolean addHeader) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (checkParam(requestMap)) {
                    if (addHeader) {
                        requestMap.put("[header]Authorization", MyHttp.getHeader());
                        Log.e("xxfigo", "[header]Authorization="+MyHttp.getHeader());
                    }
                    utils.post(requestMap, callBack);
                }
            }
        });
    }

    public void post(Map<String, Object> requestMap, IHttpCallBack callBack) {
        this.post(requestMap, callBack, true);
    }

    /**
     * @param requestMap
     * @param callBack
     * @param addHeader  是否加上默认的加密header
     */
    public void get(final Map<String, Object> requestMap, final IHttpCallBack callBack, final
    boolean
            addHeader) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (checkParam(requestMap)) {
                    if (addHeader)
                        requestMap.put("[header]Authorization", MyHttp.getHeader());
                    utils.get(requestMap, callBack);
                }
            }
        });

    }

    public void get(Map<String, Object> requestMap, IHttpCallBack callBack) {
        this.get(requestMap, callBack, true);
    }

    public void put(Map<String, Object> requestMap, IHttpCallBack callBack, boolean addHeader) {
        if (checkParam(requestMap)) {
            if (addHeader)
                requestMap.put("[header]Authorization", MyHttp.getHeader());
            if (utils instanceof XUtilsHttpUtils)
                ((XUtilsHttpUtils) utils).put(requestMap, callBack);
        }
    }

    public void put(Map<String, Object> requestMap, IHttpCallBack callBack) {
        this.put(requestMap, callBack, true);
    }

    /**
     * 检查参数
     *
     * @param requestMap
     * @return
     */
    private boolean checkParam(Map<String, Object> requestMap) {
        if (requestMap != null && requestMap.containsKey("[url]")) {
            return true;
        } else {
            try {
                throw new RuntimeException("the targetUrl of httpRequest is null!!!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    /**
     * 获取加密的header
     *
     * @return
     */
    public static String getHeader() {

        Calendar cal = Calendar.getInstance();
        //取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        //取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        //从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        long timeInMillis = cal.getTimeInMillis();
        TokenBean tokenBean = new TokenBean(CommonUtil.checkString(HelpTools.getLoginInfo
                (HelpTools.Token)), ((int)
                (timeInMillis / 1000)));

        String s1 = GsonTools.beanToJson(tokenBean);
        HashMap<String, Object> headMap = new HashMap<>();
        headMap.put("typ", "JWT");
        headMap.put("alg", "HS256");

        String compact = Jwts.builder().setHeader(headMap).setPayload(s1).signWith
                (SignatureAlgorithm.HS256,
                        TextCodec.BASE64.encode(key))
                .compact();

        return compact;
    }

    /**
     * 解密
     *
     * @param token
     * @return
     */
    public static String decodeToken(String token) {
        Jwt parse = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(key))
                .parse(token);
        DefaultClaims body = (DefaultClaims) parse.getBody();
        Object private_token = body.get("private_token");


        return private_token.toString();
    }


    public void download(Map<String, Object> requestMap, IHttpCallBack callBack, boolean
            autoResume, boolean
                                 autoRename, String targetPath) {

        utils.download(targetPath, requestMap, autoResume, autoRename, callBack);
    }

    /**
     * 加密的bean 用于gson转化为json格式字符串
     */
    public static class TokenBean {
        String private_token;
        long time;


        public TokenBean(String private_token, long time) {
            this.private_token = private_token;
            this.time = time;
        }
    }
}
