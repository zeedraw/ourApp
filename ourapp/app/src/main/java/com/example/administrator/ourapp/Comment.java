package com.example.administrator.ourapp;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/11.
 */

public class Comment extends BmobObject {
    private MyUser user; //被评价的人
    private Mission mission; //评价关联的mission
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
