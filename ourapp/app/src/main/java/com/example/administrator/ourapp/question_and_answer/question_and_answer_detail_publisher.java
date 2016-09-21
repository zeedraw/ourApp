package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.R;

/**
 * Created by Longze on 2016/9/21.
 */
public class question_and_answer_detail_publisher extends Activity {
    private TextView return_bt, edit;//标题上的返回按钮
    private TextView info_title;//标题
    private TextView question_content;
    private TextView answer_content;
    private TextView question_date;
    private String s_question_content;
    private String s_question_date;
    private String s_answer_content;
    private String s_question_ID;
//    private Mission mission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_and_answer_detail);
        initWidget();
    }//onCreate

    private void initWidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        edit = (TextView) findViewById(R.id.rbt);
        info_title = (TextView) findViewById(R.id.title);
        question_content = (TextView) findViewById(R.id.detail_question_content);
        answer_content = (TextView) findViewById((R.id.detail_answer_content));
        question_date = (TextView) findViewById(R.id.detail_question_date);

        Intent intent = getIntent();
        s_answer_content = intent.getStringExtra("answer_content");
        s_question_content = intent.getStringExtra("question_content");
        s_question_date = intent.getStringExtra("question_date");
        s_question_ID = intent.getStringExtra("question_ID");
//        Bundle bundle = new Bundle();
//        bundle = this.getIntent().getExtras();
//        mission = (Mission) bundle.getSerializable("mission");

        return_bt.setText("返回");
        info_title.setText("问答");
        edit.setText("编辑");
        question_content.setText(s_question_content);
        answer_content.setText(s_answer_content);
        question_date.setText(s_question_date);

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                question_and_answer_detail_publisher.this.finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intents = new Intent(question_and_answer_detail_publisher.this, edit_answer.class);
                // 在Intent中传递数据
//                Bundle bundle=new Bundle();
//                bundle.putSerializable("mission",mission);
//                intents.putExtras(bundle);
                intents.putExtra("question_content", s_question_content);
                intents.putExtra("answer_content",s_answer_content);
                intents.putExtra("question_ID", s_question_ID);
                intents.putExtra("question_date", s_question_date);
                startActivity(intents);
            }
        });
    }//initWidget
}
