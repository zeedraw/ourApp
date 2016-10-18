package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/10/15.
 */

public class CheckMission extends FragmentActivity {
    private TextView rt,title;
    private CheckMissionFrag mFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_listview);
        initWidget();
    }

    private void initWidget()
    {
        rt=(TextView)findViewById(R.id.lbt);
        rt.setText("返回");
        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title=(TextView)findViewById(R.id.mission_title);
        title.setText("任务履历");

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        mFrag=new CheckMissionFrag();
        ft.replace(R.id.container,mFrag);
        ft.commit();

    }
}
