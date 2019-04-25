package com.robin8.rb.ui.module.reword.bean;

import java.util.List;

/**
 * Created by IBM on 2016/8/19.
 */
public class CampaignMaterialsModel  {

    /**
     * error : 0
     * campaign_materials : [{"url_type":"image","url":"http://7xozqe.com1.z0.glb.clouddn.com/o_1aqgi4phk1o4l1rl110ia1fgm10ca18.jpg"},{"url_type":"article","url":"https://www.baidu.com/"},{"url_type":"video","url":"http://v.youku.com/v_show/id_XOTI2NzA2MTY0.html"},{"url_type":"file","url":"http://7xozqe.com1.z0.glb.clouddn.com/o_1aqgi7mna17qfcaa1av5g1d1gv01d.log"}]
     */

    private int error;
    /**
     * url_type : image
     * url : http://7xozqe.com1.z0.glb.clouddn.com/o_1aqgi4phk1o4l1rl110ia1fgm10ca18.jpg
     */

    private List<CampaignMaterialsBean> campaign_materials;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<CampaignMaterialsBean> getCampaign_materials() {
        return campaign_materials;
    }

    public void setCampaign_materials(List<CampaignMaterialsBean> campaign_materials) {
        this.campaign_materials = campaign_materials;
    }

    public static class CampaignMaterialsBean {
        private String url_type;
        private String url;

        public String getUrl_type() {
            return url_type;
        }

        public void setUrl_type(String url_type) {
            this.url_type = url_type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
