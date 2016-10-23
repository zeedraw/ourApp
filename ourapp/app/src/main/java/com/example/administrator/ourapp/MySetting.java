package com.example.administrator.ourapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ourapp.authenticate.real_name_authenticate;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MySetting extends Activity {
    private TextView rt_button;
    private TextView title;
    private TextView advice_feedback;
    private TextView loginout_bt;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initWidget();

    }

    private void initWidget()
    {
        rt_button=(TextView) findViewById(R.id.lbt);
        title=(TextView)findViewById(R.id.mission_title);
        advice_feedback=(TextView)findViewById(R.id.advice_feedback);
        loginout_bt=(TextView)findViewById(R.id.loginout_bt);

        rt_button.setText("返回");
        rt_button.setVisibility(View.VISIBLE);
        title.setText("设置");

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySetting.this.finish();
            }
        });

        loginout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag"});
                finish();
            }
        });

        advice_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp=new ComponentName(MySetting.this,advice_feedback.class);
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
            }//onClick
        });

        if(BmobUser.getCurrentUser(MyUser.class)!=null)
        {
            loginout_bt.setVisibility(View.VISIBLE);
        }
        else
        {
            loginout_bt.setVisibility(View.INVISIBLE);
        }
    }
}
