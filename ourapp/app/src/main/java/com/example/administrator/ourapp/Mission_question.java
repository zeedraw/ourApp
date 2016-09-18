package com.example.administrator.ourapp;

import cn.bmob.v3.BmobObject;

/**
 * Created by Longze on 2016/9/18.
 */
public class Mission_question extends BmobObject {
    private String content; //问题内容
    private MyUser User;    //提问者
    private Mission_answer answer; //对应答案

    public String getContent(){return content;}
    public void setContent(String content){this.content = content;}

    public MyUser getUser(){return User;}
    public void setMyUser(MyUser User){this.User = User;}

    public Mission_answer  getanswer(){return answer;}
    public void setAnswer(Mission_answer answer){this.answer = answer;}


}
