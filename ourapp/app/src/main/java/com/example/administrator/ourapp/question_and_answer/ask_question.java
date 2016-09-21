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

/**
 * Created by Longze on 2016/9/19.
 * 填写任务提问界面
 */
public class ask_question extends Activity {
    private EditText EditQuestion = null;   //问题编辑文本框
    private TextView submit = null; //提交按钮
    TextView return_bt,commit_bt;//标题上的左右按钮
    TextView info_title;//标题

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

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ask_question.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mission_question question = new Mission_question();
                question.setMyUser(BmobUser.getCurrentUser(MyUser.class));
                question.setContent(EditQuestion.getText().toString());
                question.setAnswer(null);

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
            }
        });

    }//initWidget
}
