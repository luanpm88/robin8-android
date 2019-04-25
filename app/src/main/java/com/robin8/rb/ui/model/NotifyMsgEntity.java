package com.robin8.rb.ui.model;

/**
 * 通知实体类,用于NotifyManager向所有观察者发送消息
 * 
 * @author figo
 * 
 */
public class NotifyMsgEntity {

	private int code;// 消息类别代码
	private Object data;// 消息数据实体

	public NotifyMsgEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotifyMsgEntity(int code, Object data) {
		super();
		this.code = code;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
