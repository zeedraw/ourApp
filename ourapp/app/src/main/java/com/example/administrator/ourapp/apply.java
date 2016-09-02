package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class apply extends AppCompatActivity {
    TextView rt_button, sub_button;
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply);
        initWidget();
    }

    private void initWidget()
    {
        rt_button=(TextView)findViewById(R.id.lbt);
        sub_button=(TextView)findViewById(R.id.rbt);
        title=(TextView)findViewById(R.id.title);
        rt_button.setText("返回");
        sub_button.setText("提交");
        rt_button.setVisibility(View.VISIBLE);
        sub_button.setVisibility(View.VISIBLE);
        title.setText("任务申请");

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
