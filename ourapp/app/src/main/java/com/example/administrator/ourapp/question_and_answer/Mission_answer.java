package com.example.administrator.ourapp.question_and_answer;

import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Longze on 2016/9/18.
 * 问答功能的答案表
 */
public class Mission_answer extends BmobObject {
    private String content; //回答内容
    private MyUser User;    //回答者
//    private Mission_question question;   //对应问题
    private Mission mission;    //对应任务

    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    public MyUser getUser(){return User;}
    public void setMyUser(MyUser User){this.User = User;}

//    public Mission_question getQuestion(){return question;}
//    public void setQuestion(Mission_question question){this.question = question;}

    public Mission getMission(){return mission;}
    public void setMission(Mission mission){this.mission = mission;}
}
