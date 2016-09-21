package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/19.
 * 填写任务提问界面
 */
public class ask_question extends Activity {
    private EditText EditQuestion = null;   //问题编辑文本框
    private TextView submit = null; //提交按钮
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private Mission mission;
    private Mission_question question = new Mission_question();
    private Mission_answer answer = new Mission_answer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_question);
        initWidget();
    }//onCreate

    private void initWidget() {
        EditQuestion = (EditText) findViewById(R.id.edit_question);
        submit = (TextView) findViewById(R.id.submit);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.title);

        return_bt.setText("返回");
        info_title.setText("提问");

        //获取当前任务
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        mission = (Mission) bundle.getSerializable("mission");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ask_question.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer.setContent("暂无回答");
                answer.setMyUser(BmobUser.getCurrentUser(MyUser.class));
                answer.setMission(question.getMission());
                answer.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            Log.i("bmob","上传回答成功");

                            //更新question的answer
                            question.setMyUser(BmobUser.getCurrentUser(MyUser.class));
                            question.setContent(EditQuestion.getText().toString());
                            question.setMission(mission);
                            question.setAnswer(answer);
                            question.save(new SaveListener<String>() {

                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){
                                        Log.i("bmob","上传数据成功");

                                        ask_question.this.finish();
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });

                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });


            }
        });

    }//initWidget
}
