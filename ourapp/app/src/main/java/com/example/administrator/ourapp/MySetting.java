package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MySetting extends AppCompatActivity {
    TextView rt_button;
    TextView title;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initWidget();

    }

    private void initWidget()
    {
        rt_button=(TextView) findViewById(R.id.lbt);
        title=(TextView)findViewById(R.id.title);
        rt_button.setText("返回");
        rt_button.setVisibility(View.VISIBLE);
        title.setText("设置");

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MySetting.this.finish();
            }
        });
    }
}
