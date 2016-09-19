package com.example.administrator.ourapp.question_and_answer;

import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by Longze on 2016/9/18.
 * 问答功能的问题表
 */
public class Mission_question extends BmobObject {
    private String content; //问题内容
    private MyUser User;    //提问者
    private Mission_answer answer; //对应答案
    private Mission mission;    //对应任务

    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    public MyUser getUser(){return User;}
    public void setMyUser(MyUser User){this.User = User;}

    public Mission_answer  getanswer(){return answer;}
    public void setAnswer(Mission_answer answer){this.answer = answer;}

    public Mission getMission(){return mission;}
    public void setMission(Mission mission){this.mission = mission;}


}
