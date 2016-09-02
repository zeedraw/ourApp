package com.example.administrator.ourapp;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/9/2.
 */
//任务表
public class Mission extends BmobObject {
    private String name;
    private Integer need_people;//需要人数
    private String detail;//任务详情
    private String abs;//任务简介
    private String location;//任务所在地
    private MyUser pub_user;//发布人
    private BmobDate pub_time;//发布时间
    private BmobDate start_time;//任务开始时间
    private BmobDate end_time;//结束时间
    private Integer cur_people;//即时人数
    private BmobRelation get_user;//接收人
    private String state;//任务状态： onWating等待,onGoing进行中,onFinish完成
    private String tag;//任务类型 edu,act,trans,scene

    //getter&setter


    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public Integer getCur_people() {
        return cur_people;
    }

    public void setCur_people(Integer cur_people) {
        this.cur_people = cur_people;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BmobRelation getGet_user() {
        return get_user;
    }

    public void setGet_user(BmobRelation get_user) {
        this.get_user = get_user;
    }

    public BmobDate getEnd_time() {
        return end_time;
    }

    public void setEnd_time(BmobDate end_time) {
        this.end_time = end_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNeed_people() {
        return need_people;
    }

    public void setNeed_people(Integer need_people) {
        this.need_people = need_people;
    }

    public BmobDate getPub_time() {
        return pub_time;
    }

    public void setPub_time(BmobDate pub_time) {
        this.pub_time = pub_time;
    }

    public MyUser getPub_user() {
        return pub_user;
    }

    public void setPub_user(MyUser pub_user) {
        this.pub_user = pub_user;
    }

    public BmobDate getStart_time() {
        return start_time;
    }

    public void setStart_time(BmobDate start_time) {
        this.start_time = start_time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}