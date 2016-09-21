package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.authenticate.real_name_authenticate;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/19.
 */
public class edit_answer extends Activity {

    private EditText EditAnswer = null;   //问题编辑文本框
    private TextView question_content = null;
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private Mission_question question = new Mission_question();
    private String question_ID = null; //对应问题的objectId
    private Mission_answer answer = new Mission_answer();
    private String answer_ID; //暂存此答案的objectId


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_answer);
        initWidget();
    }//onCreate

    private void initWidget() {
        EditAnswer = (EditText) findViewById(R.id.edit_question);
        return_bt = (TextView) findViewById(R.id.lbt);
        commit_bt = (TextView) findViewById(R.id.rbt);
        info_title = (TextView) findViewById(R.id.title);
        question_content = (TextView) findViewById(R.id.question_content);
        Intent intent = getIntent();

        return_bt.setText("返回");
        info_title.setText("编辑回答");
        commit_bt.setText("提交");
        question_content.setText(intent.getStringExtra("question_content"));
        EditAnswer.setText(intent.getStringExtra("answer_content"));
//        question = (Mission_question) intent.getSerializableExtra("question");
        question_ID = intent.getStringExtra("question_ID");

        //根据上一个activity传递的question的ID来获取question本身
        final BmobQuery<Mission_question> query = new BmobQuery<Mission_question>();
        query.include("answer");
        query.getObject(question_ID, new QueryListener<Mission_question>() {

            @Override
            public void done(Mission_question object, BmobException e) {
                if(e==null){
                    question = object;
                }else{
                    Log.i("bmob","获取对应问题失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_answer.this.finish();
            }
        });

        //单击提交按钮 上传EditText内容
        commit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer.setContent(EditAnswer.getText().toString());

                answer.update(question.getanswer().getObjectId(), new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","回答更新成功");
                            Toast.makeText(getApplicationContext(), "回答更新成功",
                                    Toast.LENGTH_SHORT).show();
                            edit_answer.this.finish();
//                            ComponentName comp=new ComponentName(edit_answer.this,question_and_answer.class);
//                            Intent intent=new Intent();
//                            intent.setComponent(comp);
//                            startActivity(intent);
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });

    }//initWidget
}
