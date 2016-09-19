package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ourapp.R;

/**
 * Created by Longze on 2016/9/19.
 */
public class edit_answer extends Activity {

    private EditText EditAnswer = null;   //问题编辑文本框
    TextView return_bt,commit_bt;//标题上的左右按钮
    TextView info_title;//标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_answer);
    }//onCreate

    private void initWidget() {
        EditAnswer = (EditText) findViewById(R.id.edit_question);
        return_bt = (TextView) findViewById(R.id.lbt);
        commit_bt = (TextView) findViewById(R.id.rbt);
        info_title = (TextView) findViewById(R.id.title);

        return_bt.setText("返回");
        info_title.setText("编辑回答");
        commit_bt.setText("提交");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_answer.this.finish();
            }
        });
    }//initWidget
}
