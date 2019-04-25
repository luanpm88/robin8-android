package com.robin8.rb.ui.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/2/15 20:39
 */
public class OtherLoginListBean extends BaseBean implements Serializable {
    private String kol_uuid;

    private ArrayList<OtherLoginBean> identities;
    private boolean uploaded_contacts;//是否上传过通讯录

    public boolean isUploaded_contacts() {
        return uploaded_contacts;
    }

    public void setUploaded_contacts(boolean uploaded_contacts) {
        this.uploaded_contacts = uploaded_contacts;
    }

    public OtherLoginListBean(String kol_uuid, ArrayList<OtherLoginBean> kol_identities) {
        this.kol_uuid = kol_uuid;
        this.identities = kol_identities;
    }

    public ArrayList<OtherLoginBean> getIdentities() {
        return identities;
    }

    public void setIdentities(ArrayList<OtherLoginBean> identities) {
        this.identities = identities;
    }

    public String getKol_uuid() {
        return kol_uuid;
    }

    public void setKol_uuid(String kol_uuid) {
        this.kol_uuid = kol_uuid;
    }

    public static class OtherLoginBean implements Serializable {
        //        object['provider']	object指第三方账号对象，provider值为['weibo','wechat'] 分别代表微博、微信账号
        // string
//        object['uid']	object指第三方账号对象，uid 表示第三方账号标识码	string
//        object['token']	object指第三方账号对象，token表示第三方账号 验证码	string
//        object['name']	object指第三方账号对象，name表示第三方账号 名称	string
//        object['url']	object指第三方账号对象，url表示第三方账号 个人主页地址	string
//        object['avatar_url']	object指第三方账号对象，avatar_url表示第三方账号 头像地址	string
//        object['desc'] object指第三方账号对象，desc表示第三方账号 个人签名
        private String provider;
        private String uid;
        private String token;
        private String name;
        private String url;
        private String avatar_url;
        private String desc;

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
