package com.example.administrator.ourapp.message;

import com.example.administrator.ourapp.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Longze on 2016/9/25.
 * 消息表
 */
public class Message extends BmobObject {
    private String content; //消息的内容
    private MyUser sender;  //消息的发送者
    private MyUser receiver;    //消息的接收者
    private boolean be_viewed = false;  //消息是否已读



    private String remark;
    Integer type;   //消息的类型

    /*
    消息的类型中 0代表好友请求消息
                1代表同意添加好友消息
                2代表拒绝添加好友消息
                3代表申请任务成功
                4代表报名任务成功
                5代表申请的任务开始消息
                6代表
                7代表有人对用户的任务进行提问的消息
                8代表提问有新的回答的消息
     */

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MyUser getSender() {
        return sender;
    }

    public void setSender(MyUser sender) {
        this.sender = sender;
    }

    public MyUser getReceiver() {
        return receiver;
    }

    public void setReceiver(MyUser receiver) {
        this.receiver = receiver;
    }

    public boolean getBe_viewed() {
        return be_viewed;
    }

    public void setBe_viewed(boolean be_viewed) {
        this.be_viewed = be_viewed;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
