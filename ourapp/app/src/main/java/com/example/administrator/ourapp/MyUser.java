package com.example.administrator.ourapp;

import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/9/2.
 */
//用户表
public class MyUser extends BmobUser {
    private String borndate;
    private Boolean sex;//true为男 false 为女
    private String qualification;//学历
    private String location;//所在地
    private Float rating;//评分
    private BmobRelation friends;//好友
    private Boolean isIdentifiedStudent;//是否被认证 true被认证 false没被认证
    private Boolean isIdentifiedPublish;
    private String tag;//被认证为的类型 教育，活动，社区，交通
    private Integer ident_state_stu; //学生认证的状态 0为未认证 1为审核中 2为已通过
    private Integer ident_state_pub; //机构的认证状态 0为未认证 1为审核中 2为已通过
    private String tag;//被认证为的类型 教育，活动，社区，景点
    private String name;
    private BmobFile userimage;
    private String introduction;//自我介绍
    private String realname;
    private String idCard;
    private String schoolname;
    private BmobFile cardfront;
    private BmobFile cardback;
    private BmobFile studentcard;
    private BmobFile halfpicture;
    private String stuDescription; //学生认证描述
    private String orgDescription;//机构认证描述
    private Integer missionSum;
    //以下为机构认证所需资料
    private BmobFile agency_pic1;
    private BmobFile agency_pic2;
    private BmobFile agency_pic3;
    private BmobFile agency_pic4;
    private String agency_name;
    private String agency_web;
    private String agency_contact_num;
    private Boolean is_new_message = false;
    private BmobDate message_refresh_time; //最后一次在本地加载新消息的时间

    public BmobDate getMessage_refresh_time() {
        return message_refresh_time;
    }

    public void setMessage_refresh_time(BmobDate message_refresh_time) {
        this.message_refresh_time = message_refresh_time;
    }

    public String getOrgDescription() {
        return orgDescription;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }

    public String getStuDescription() {
        return stuDescription;
    }

    public void setStuDescription(String stuDescription) {
        this.stuDescription = stuDescription;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public BmobFile getCardback() {
        return cardback;
    }

    public void setCardback(BmobFile cardback) {
        this.cardback = cardback;
    }

    public BmobFile getCardfront() {
        return cardfront;
    }

    public void setCardfront(BmobFile cardfront) {
        this.cardfront = cardfront;
    }

    public BmobFile getHalfpicture() {
        return halfpicture;
    }

    public void setHalfpicture(BmobFile halfpicture) {
        this.halfpicture = halfpicture;
    }

    public BmobFile getStudentcard() {
        return studentcard;
    }

    public void setStudentcard(BmobFile studentcard) {
        this.studentcard = studentcard;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Boolean getIdentifiedPublish() {
        return isIdentifiedPublish;
    }

    public void setIdentifiedPublish(Boolean identifiedPublish) {
        isIdentifiedPublish = identifiedPublish;
    }

    public Boolean getIdentifiedStudent() {
        return isIdentifiedStudent;
    }

    public void setIdentifiedStudent(Boolean identifiedStudent) {
        isIdentifiedStudent = identifiedStudent;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    //认证资质
    private String identification;//“edu”教育,”act”活动,”trans”交通,”scene"景点
    private String description;//资格描述

    //getter setter方法



    public void setUserimage(BmobFile userimage) {
        this.userimage = userimage;

    }

    public void setBorndate(String borndate) {
        this.borndate = borndate;
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

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BmobFile getUserimage() {
        return userimage;
    }

    public String getBorndate() {
        return borndate;
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


    public String getLocation() {
        return location;
    }

    public String getQualification() {
        return qualification;
    }

    public Float getRating() {
        return rating;
    }

    public Boolean getSex() {
        return sex;
    }

    public String getName(){return name;}

    public BmobFile getAgency_pic1() {
        return agency_pic1;
    }

    public void setAgency_pic1(BmobFile agency_pic1) {
        this.agency_pic1 = agency_pic1;
    }

    public BmobFile getAgency_pic2() {
        return agency_pic2;
    }

    public void setAgency_pic2(BmobFile agency_pic2) {
        this.agency_pic2 = agency_pic2;
    }
    public BmobFile getAgency_pic3() {
        return agency_pic3;
    }

    public void setAgency_pic3(BmobFile agency_pic3) {
        this.agency_pic3 = agency_pic3;
    }

    public String getAgency_name() {
        return agency_name;
    }

    public void setAgency_name(String agency_name) {
        this.agency_name = agency_name;
    }

    public BmobFile getAgency_pic4() {
        return agency_pic4;
    }

    public void setAgency_pic4(BmobFile agency_pic4) {
        this.agency_pic4 = agency_pic4;
    }

    public String getAgency_web() {
        return agency_web;
    }

    public void setAgency_web(String agency_web) {
        this.agency_web = agency_web;
    }

    public String getAgency_contact_num() {
        return agency_contact_num;
    }

    public void setAgency_contact_num(String agency_contact_num) {
        this.agency_contact_num = agency_contact_num;
    }

    public Boolean getIs_new_message() {
        return is_new_message;
    }

    public void setIs_new_message(Boolean is_new_message) {
        this.is_new_message = is_new_message;
    }


    public Integer getIdent_state_pub() {
        return ident_state_pub;
    }

    public void setIdent_state_pub(Integer ident_state_pub) {
        this.ident_state_pub = ident_state_pub;
    }

    public Integer getIdent_state_stu() {
        return ident_state_stu;
    }

    public void setIdent_state_stu(Integer ident_state_stu) {
        this.ident_state_stu = ident_state_stu;
    }


}
