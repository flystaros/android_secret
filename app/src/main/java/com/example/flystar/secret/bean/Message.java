package com.example.flystar.secret.bean;

/**
 * Created by flystar on 2015/2/27.
 */
public class Message
{
    public Message(String msgId, String msg , String phone_md5) {
        this.msg = msg;
        this.phone_md5 = phone_md5;
        this.msgId = msgId;
    }

    private String msg;
    private String phone_md5;
    private String msgId;

    public String getMsg() {
        return msg;
    }

    public String getPhone_md5() {
        return phone_md5;
    }

    public String getMsgId() {
        return msgId;
    }


    //不需要setter
    //因为这些数据都是不能被更改的
}
