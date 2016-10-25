package com.example.administrator.ourapp.question_and_answer;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.message.Message_tools;



import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/9/19.
 */
public class edit_answer extends SwipeBackActivity {

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
        info_title = (TextView) findViewById(R.id.mission_title);
        question_content = (TextView) findViewById(R.id.question_content);
        Intent intent = getIntent();

        return_bt.setText("返回");
        info_title.setText("编辑回答");
        commit_bt.setText("提交");
        question_content.setText(intent.getStringExtra("question_content"));
        if(intent.getStringExtra("answer_content").equals("暂无回答")){
            EditAnswer.setText("");
        }//if
        else{
            EditAnswer.setText(intent.getStringExtra("answer_content"));
        }//else

        EditAnswer.setSelection(EditAnswer.length());
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
                    Toast.makeText(edit_answer.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                if(EditAnswer.length() < 4){
                    Toast.makeText(edit_answer.this, "回答的内容不得少于4个字符", Toast.LENGTH_SHORT).show();
                    return;
                }//回答的内容不少于4字符
                else if(EditAnswer.length() > 200){
                    Toast.makeText(edit_answer.this, "回答的内容超过字数限制", Toast.LENGTH_SHORT).show();
                    return;
                }
                if((EditAnswer.getText().toString().equals(question.getanswer().getContent()))){
                    Toast.makeText(edit_answer.this, "您必须更改回答的内容才能提交", Toast.LENGTH_SHORT).show();
                    return;
                }//若答案有变更才上传

                answer.setContent(EditAnswer.getText().toString());

                answer.update(question.getanswer().getObjectId(), new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        final Dialog loading_dialog = MainActivity.createLoadingDialog(edit_answer.this);
                        loading_dialog.show();
                        AlertDialog.Builder builder=new AlertDialog.Builder(edit_answer.this);
                        if(e==null){
                            Log.i("bmob","回答更新成功");
                            Message_tools sm = new Message_tools();
                            sm.send(BmobUser.getCurrentUser(MyUser.class), question.getUser(),
                                    "您的提问有新回答啦", 8 , false, question.getObjectId(), edit_answer.this);

                            //8代表提问有回答的消息

                            builder.setMessage("答案编辑成功").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loading_dialog.dismiss();
                                    ListenerManager.getInstance().sendBroadCast(new String[]{"qa"});
                                    finish();
                                }
                            }).create().show();
                        }else{
                            loading_dialog.dismiss();
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
//                            builder.setMessage("答案编辑失败").create().show();
                            Toast.makeText(edit_answer.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    });


            }
        });

    }//initWidget
}
