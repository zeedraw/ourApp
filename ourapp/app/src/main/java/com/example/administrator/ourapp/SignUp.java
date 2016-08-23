package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/21.
 */
public class SignUp extends AppCompatActivity {
    Button rt_button;
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initWidget();
    }

    private void initWidget()
    {
        rt_button=(Button)findViewById(R.id.lbt);
        title=(TextView)findViewById(R.id.title);
        rt_button.setText("返回");
        rt_button.setVisibility(View.VISIBLE);
        title.setText("任务申请");

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
