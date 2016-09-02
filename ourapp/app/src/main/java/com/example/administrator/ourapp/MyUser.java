package com.example.administrator.ourapp;

import android.provider.ContactsContract;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/9/2.
 */
//用户表
public class MyUser extends BmobUser {
    private String age;
    private Boolean sex;//true为男 false 为女
    private String qualification;//学历
    private String location;//所在地
    private Double rating;//评分
    private BmobRelation friends;//好友
    private Boolean isIdentified;//是否被认证 true被认证 false没被认证

    //认证资质
    private String identification;//“edu”教育,”act”活动,”trans”交通,”scene"景点
    private String description;//资格描述

    //getter setter方法
    public void setAge(String age) {
        this.age = age;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }

    public void setIdentified(Boolean identified) {
        this.isIdentified = identified;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public BmobRelation getFriends() {
        return friends;
    }

    public String getIdentification() {
        return identification;
    }

    public Boolean getIdentified() {
        return isIdentified;
    }

    public String getLocation() {
        return location;
    }

    public String getQualification() {
        return qualification;
    }

    public Double getRating() {
        return rating;
    }

    public Boolean getSex() {
        return sex;
    }
}
