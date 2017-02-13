package com.robin8.rb.model;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/2/23 13:27
 */
public class UpdateBean extends BaseBean {
    //    had_upgrade	是否有更新, true 表示有新版本，false 表示无新版本	boolean
//    newest_version['app_platform']	版本平台	string
//    newest_version['app_version']	版本号	string
//    newest_version['download_url']	下载地址	string
//    newest_version['release_note']	最新版本更新内容	string
//    newest_version['force_upgrade']	是否需要强制更新到此版本	boolean
//    newest_version['release_at']	发布时间	datetime
    public boolean isHad_upgrade() {
        return had_upgrade;
    }

    public void setHad_upgrade(boolean had_upgrade) {
        this.had_upgrade = had_upgrade;
    }

    private UpdateInfoBean newest_version;

    public UpdateInfoBean getNewest_version() {
        return newest_version;
    }

    public void setNewest_version(UpdateInfoBean newest_version) {
        this.newest_version = newest_version;
    }

    private boolean had_upgrade;

    public static class UpdateInfoBean {
        private String app_platform;
        private String app_version;
        private String download_url;
        private String release_note;
        private boolean force_upgrade;
        private String release_at;

        public String getApp_platform() {
            return app_platform;
        }

        public void setApp_platform(String app_platform) {
            this.app_platform = app_platform;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public String getDownload_url() {
            return download_url;
        }

        public void setDownload_url(String download_url) {
            this.download_url = download_url;
        }

        public String getRelease_note() {
            return release_note;
        }

        public void setRelease_note(String release_note) {
            this.release_note = release_note;
        }

        public boolean isForce_upgrade() {
            return force_upgrade;
        }

        public void setForce_upgrade(boolean force_upgrade) {
            this.force_upgrade = force_upgrade;
        }

        public String getRelease_at() {
            return release_at;
        }

        public void setRelease_at(String release_at) {
            this.release_at = release_at;
        }
    }
}
