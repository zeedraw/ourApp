package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MineFrag extends Fragment implements IListener{

    private TextView name_tv,location_tv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("MineFrag",this);
        View view=inflater.inflate(R.layout.main_person, container, false);
        name_tv=(TextView)view.findViewById(R.id.mine_name_tv);
        location_tv=(TextView)view.findViewById(R.id.mine_location_tv);
        return view;
    }

    @Override
    public void upData() {
            if (BmobUser.getCurrentUser(MyUser.class)==null)
            {
                name_tv.setText("未知用户");
                location_tv.setText("所在地未知");

            }
        else
            {
                name_tv.setText((String)BmobUser.getObjectByKey("name"));
                Log.i("naosumi",(String)BmobUser.getObjectByKey("name"));
                if(BmobUser.getObjectByKey("location")!=null)
                {
                    location_tv.setText((String)BmobUser.getObjectByKey("location"));
                }

            }
    }
}
