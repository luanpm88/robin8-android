package com.robin8.rb.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zc on 2017/7/20.
 */

public class IndentyBean extends BaseBean {

    /**
     * error : 0
     * identities : [{"provider":"qq","uid":"A900A3812AE00A23C6F01686E5388C51","token":"7A248ACAAEA4E7C627DFA3EA34D3A951","name":"烛黎","url":"","avatar_url":"http://q.qlogo.cn/qqapp/1105224850/A900A3812AE00A23C6F01686E5388C51/40","desc":"null"},{"provider":"wechat","uid":"o0yISwZAnRraY_w9a8P8xA2cn5T0","token":"YPjubfdghtaTHgKlRrW5R72IGPHB-1G81ZFotrh5uC91-hUtelb-bnnwJDAugH7wcqJ1ZCRddisCDDPo-_ahDBIQO92yI2osFgLSWuqrAbA","name":"mirror","url":"","avatar_url":"http://wx.qlogo.cn/mmopen/vyIdUlr1et0YzciavQRETP2LJthTaV0LvYeWVHRSCAsJ8ic3BibSwiazcxmnn8ULWobQUKWIiabjX4tdqfhMIT9gANRkQyKHkMLPr/0","desc":"null"}]
     */

    private List<IdentitiesBean> identities;

    public List<IdentitiesBean> getIdentities() {
        return identities;
    }

    public void setIdentities(List<IdentitiesBean> identities) {
        this.identities = identities;
    }

    public static class IdentitiesBean implements Serializable{
        /**
         * provider : qq
         * uid : A900A3812AE00A23C6F01686E5388C51
         * token : 7A248ACAAEA4E7C627DFA3EA34D3A951
         * name : 烛黎
         * url :
         * avatar_url : http://q.qlogo.cn/qqapp/1105224850/A900A3812AE00A23C6F01686E5388C51/40
         * desc : null
         */

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
