package com.example.administrator.ourapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.question_and_answer.Mission_question;

/**
 * Created by Longze on 2016/10/23.
 */
public class reject_authentication_reason extends Activity {

    private TextView reject_reason;
    private TextView reapprove;
    private TextView return_bt = null;
    private TextView info_title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reject_authentication_reason);
        initWidget();
    }//onCreate

    private void initWidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);
        reject_reason = (TextView) findViewById(R.id.reason);
        reapprove = (TextView) findViewById(R.id.submit);
        return_bt.setText("返回");
        info_title.setText("认证失败");
        Intent intent = getIntent();

        String reason = (String)intent.getSerializableExtra("reason");
        reject_reason.setText(reason);

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_authentication_reason.this.finish();
            }
        });

        reapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_authentication_reason.this.finish();
            }
        });

    }//initWidget
}
