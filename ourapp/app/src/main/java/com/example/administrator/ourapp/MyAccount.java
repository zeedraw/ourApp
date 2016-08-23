package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MyAccount extends AppCompatActivity {
    Button rt_button,edit_button;
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation);
        initWidget();


    }

    private void initWidget()
    {
        rt_button=(Button)findViewById(R.id.lbt);
        edit_button=(Button)findViewById(R.id.rbt);
        title=(TextView)findViewById(R.id.title);

        rt_button.setText("返回");
        edit_button.setText("编辑");
        title.setText("个人信息");
        rt_button.setVisibility(View.VISIBLE);
        edit_button.setVisibility(View.VISIBLE);

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}


