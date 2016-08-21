package com.example.administrator.ourapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/8/20.
 */
public class MissonInfo extends AppCompatActivity {

    Button return_bt,commit_bt;//标题上的左右按钮
    TextView info_title;//标题
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_info);
        initWidget();
    }

    private void initWidget(){

        return_bt=(Button)findViewById(R.id.lbt);
        commit_bt=(Button)findViewById(R.id.rbt);
        return_bt.setText("返回");
        return_bt.setTextSize(21);
        return_bt.setTextColor(getResources().getColor(R.color.colorblue));
        return_bt.setVisibility(View.VISIBLE);
        commit_bt.setText("报名");
        commit_bt.setTextSize(21);
        commit_bt.setTextColor(getResources().getColor(R.color.colorblue));
        commit_bt.setVisibility(View.VISIBLE);

        info_title=(TextView)findViewById(R.id.title);
        info_title.setText("任务详情");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp=new ComponentName(MissonInfo.this,MainActivity.class);
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
                MissonInfo.this.finish();


            }
        });

    }
}
