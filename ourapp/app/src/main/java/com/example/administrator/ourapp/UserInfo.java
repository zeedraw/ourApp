package com.example.administrator.ourapp;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/12.
 */

public class UserInfo extends BmobObject {
    private MyUser user;
    private Float rating;
    private Integer missionSum;

    public Integer getMissionSum() {
        return missionSum;
    }

    public void setMissionSum(Integer missionSum) {
        this.missionSum = missionSum;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
