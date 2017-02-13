package com.robin8.rb.model;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/2/23 17:03
 */
public class PushBean extends BaseBean {

    //    action	'income','announcement' 分别代表收入、公告类型	int
//    new_income	当消息类型为'income'时则返回,总计新收入	float
//    unread_message_count	当消息类型为'income'时则返回,未读消息数量	array[object]
    private String action;
    private String new_income;
    private String unread_message_count;
    private String title;
    private String name;
    private String sender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNew_income() {
        return new_income;
    }

    public void setNew_income(String new_income) {
        this.new_income = new_income;
    }

    public String getUnread_message_count() {
        return unread_message_count;
    }

    public void setUnread_message_count(String unread_message_count) {
        this.unread_message_count = unread_message_count;
    }
}
