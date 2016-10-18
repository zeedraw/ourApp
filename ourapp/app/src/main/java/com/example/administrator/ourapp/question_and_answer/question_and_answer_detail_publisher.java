package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.IListener;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Longze on 2016/9/21.
 */
public class question_and_answer_detail_publisher extends Activity implements IListener {
    private TextView return_bt, edit;//标题上的返回按钮
    private TextView info_title;//标题
    private TextView question_content;
    private TextView answer_content;
    private TextView question_date;
    private String s_question_content;
    private String s_question_date;
    private String s_answer_content;
    private String s_question_ID;
    private Mission_question question;
//    private Mission mission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_and_answer_detail);
        ListenerManager.getInstance().registerListtener("qa",this);
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

        question = (Mission_question)intent.getSerializableExtra("question");

        s_answer_content = question.getanswer().getContent();
        s_question_content = question.getContent();
        s_question_date = question.getCreatedAt();
        s_question_ID = question.getObjectId();
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

    @Override
    public void upData() {
        BmobQuery<Mission_answer> query = new BmobQuery<Mission_answer>();
        query.getObject(question.getanswer().getObjectId(), new QueryListener<Mission_answer>() {

            @Override
            public void done(Mission_answer object, BmobException e) {
                if(e==null){
                    s_answer_content = object.getContent();
                    answer_content.setText(s_answer_content);
//                    Toast.makeText(getApplicationContext(), "刷新确认",
//                            Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }
}
