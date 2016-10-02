package com.example.administrator.ourapp.message;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

/**
 * Created by Longze on 2016/10/1.
 */

public class MyBmobInstallation extends BmobInstallation {

    /**
     * 用户id-这样可以将设备与用户之间进行绑定
     */
    private String uid;

    public MyBmobInstallation(Context context) {
        super();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}