package com.robin8.rb.model;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/2/26 21:15
 */
public class UploadSnapBean extends BaseBean {
    private String screenshot_url;
    private String invite_status;

    public String getScreenshot_url() {
        return screenshot_url;
    }

    public void setScreenshot_url(String screenshot_url) {
        this.screenshot_url = screenshot_url;
    }

    public String getInvite_status() {
        return invite_status;
    }
}
