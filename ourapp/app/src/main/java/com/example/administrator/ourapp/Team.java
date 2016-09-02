package com.example.administrator.ourapp;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/9/2.
 */
public class Team extends BmobObject {
    private String name;
    private MyUser leader;
    private Integer num;//团队人数
    private BmobRelation members;//团队成员
    private String introduction;//团队介绍

    //setter&getter


    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public MyUser getLeader() {
        return leader;
    }

    public void setLeader(MyUser leader) {
        this.leader = leader;
    }

    public BmobRelation getMembers() {
        return members;
    }

    public void setMembers(BmobRelation members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
