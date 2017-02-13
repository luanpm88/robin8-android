package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class MessageModel extends BaseBean {

    /**
     * id : 34550
     * title : 截图已经通过审核，快来查查你的收益
     * message_type : screenshot_passed
     * is_read : false
     * url : null
     * logo_url : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/image/avatar/436/b28675ed9d.jpg
     * sender : Incor
     * item_id : 855304
     * name : Android debug5
     * sub_message_type : invite
     * desc : Android debug5
     * read_at : null
     * created_at : 2016-08-19T16:25:40+08:00
     */

    private List<MessagesBean> messages;

    public List<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean implements Serializable {
        private int id;
        private String title;
        private String message_type;
        private boolean is_read;
        private String url;
        private String logo_url;
        private String sender;
        private int item_id;
        private String name;
        private String sub_message_type;
        private String sub_sub_message_type;
        private String desc;
        private String read_at;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(String message_type) {
            this.message_type = message_type;
        }

        public boolean isIs_read() {
            return is_read;
        }

        public void setIs_read(boolean is_read) {
            this.is_read = is_read;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSub_message_type() {
            return sub_message_type;
        }

        public void setSub_message_type(String sub_message_type) {
            this.sub_message_type = sub_message_type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Object getRead_at() {
            return read_at;
        }

        public void setRead_at(String read_at) {
            this.read_at = read_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getSub_sub_message_type() {
            return sub_sub_message_type;
        }

        public void setSub_sub_message_type(String sub_sub_message_type) {
            this.sub_sub_message_type = sub_sub_message_type;
        }
    }
}
