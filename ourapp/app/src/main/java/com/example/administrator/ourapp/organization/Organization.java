package com.example.administrator.ourapp.organization;

import com.example.administrator.ourapp.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Longze on 2016/12/7.
 */
public class Organization extends BmobObject {
    private String name;    //机构名称
    private String website; //机构网站
    private String contact_number;  //机构联系方式
    private MyUser principal;  //机构负责人账户
    private String location;    //机构所在地

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public MyUser getPrincipal() {
        return principal;
    }

    public void setPrincipal(MyUser principal) {
        this.principal = principal;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
