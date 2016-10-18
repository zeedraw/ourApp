package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.R;

/**
 * Created by Longze on 2016/9/20.
 * 单个问答的详细界面
 */
public class question_and_answer_detail extends Activity {

    TextView return_bt;//标题上的返回按钮
    TextView info_title;//标题
    TextView question_content;
    TextView answer_content;
    TextView question_date;
    private Mission_question question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_and_answer_detail);
        initWidget();
    }//onCreate

    private void initWidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);
        question_content = (TextView) findViewById(R.id.detail_question_content);
        answer_content = (TextView) findViewById((R.id.detail_answer_content));
        question_date = (TextView) findViewById(R.id.detail_question_date);

        Intent intent = getIntent();

        return_bt.setText("返回");
        info_title.setText("问答");
        question = (Mission_question)intent.getSerializableExtra("question");

        question_content.setText(question.getContent());
        answer_content.setText(question.getanswer().getContent());
        question_date.setText(question.getCreatedAt());

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                question_and_answer_detail.this.finish();
            }
        });
    }//initWidget
}//question_and_answer_detail
