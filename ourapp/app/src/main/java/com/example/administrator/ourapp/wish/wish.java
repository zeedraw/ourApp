package com.example.administrator.ourapp.wish;

import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.organization.Organization;

import cn.bmob.v3.BmobObject;

/**
 * Created by Longze on 2016/12/7.
 */
public class Wish extends BmobObject {
    private String title;       //心愿标题
    private MyUser wish_user;   //心愿提出者
    private Integer type;       //心愿类型 0代表属于任务之下 1代表个人心愿
    private Organization organization;  //所属机构
    private Mission mission;    //所属任务
    private String location;    //所在地i
    private String content;     //心愿详情
    private boolean is_finished;    //是否完成

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MyUser getWish_user() {
        return wish_user;
    }

    public void setWish_user(MyUser wish_user) {
        this.wish_user = wish_user;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean is_finished() {
        return is_finished;
    }

    public void setIs_finished(boolean is_finished) {
        this.is_finished = is_finished;
    }
}
