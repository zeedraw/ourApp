package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MySetting extends AppCompatActivity {
    TextView rt_button;
    TextView title;
    Button loginout_bt;
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
        rt_button.setText("返回");
        rt_button.setVisibility(View.VISIBLE);
        title.setText("设置");

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MySetting.this.finish();
            }
        });

        loginout_bt=(Button)findViewById(R.id.loginout_bt);
        loginout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag"});
                finish();
            }
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
