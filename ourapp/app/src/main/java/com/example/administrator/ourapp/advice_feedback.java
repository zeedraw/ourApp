package com.example.administrator.ourapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.message.Message;
import com.example.administrator.ourapp.message.Message_tools;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/10/20.
 */
public class advice_feedback extends SwipeBackActivity {

    private EditText EditFeedback = null;   //反馈编辑文本框
    private EditText contact_number = null;   //联系电话文本框
    private TextView submit = null; //提交按钮
    private TextView return_bt, commit_bt;//标题上的左右按钮
    private TextView info_title;//标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advice_feedback);
        initWidget();
    }//onCreate

    private void initWidget() {
        EditFeedback = (EditText) findViewById(R.id.edit_feedback);
        contact_number = (EditText) findViewById(R.id.contact_number);
        submit = (TextView) findViewById(R.id.submit);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);

        return_bt.setText("返回");
        info_title.setText("意见反馈");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advice_feedback.this.finish();
            }
        });

        submit.setOnClickListener(listener);
    }//initWidget

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MyUser receiver = new MyUser();
            receiver.setObjectId("TJRU555B");
            int text_length = 0;
            text_length = EditFeedback.length();
            if (text_length == 0) {
                Toast.makeText(getBaseContext(), "反馈内容不能为空", Toast.LENGTH_SHORT).show();
            }//if
            else if (text_length > 400) {
                Toast.makeText(getBaseContext(), "反馈内容不能超过400字", Toast.LENGTH_SHORT).show();
            } else {
                String phone_number = "0";
                if (contact_number.length() > 0 && contact_number.length() < 11) {
                    Toast.makeText(getBaseContext(), "联系电话格式有误，请输入正确的手机号",
                            Toast.LENGTH_SHORT).show();
                }//if
                else if (contact_number.length() == 11) {
                    phone_number = contact_number.getText().toString();
                }//
                Message_tools ms = new Message_tools();


//                ms.send(BmobUser.getCurrentUser(MyUser.class),
//                        reciver, EditFeedback.getText().toString(),
//                        9, false, phone_number, getBaseContext());

                final Message message = new Message();
                message.setContent( EditFeedback.getText().toString());
                message.setType(9); //7代表有人提问的消息
                message.setReceiver(receiver);
                message.setSender(BmobUser.getCurrentUser(MyUser.class));
                message.setBe_viewed(false);
                message.setRemark(phone_number);
                final Dialog loading_dialog = MainActivity.createLoadingDialog(advice_feedback.this);
                loading_dialog.show();
                message.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {

                        if(e==null){
                            Log.i("bomb","发送信息成功：" + objectId);

                            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                            query.addWhereEqualTo("user" ,"TJRU555B");
                            query.setLimit(50);
                            query.findObjects(new FindListener<UserInfo>() {
                                @Override
                                public void done(List<UserInfo> object, BmobException e) {
                                    if(e==null){

                                        for (UserInfo user : object) {
                                            user.addUnread_message_num();
                                            user.update(user.getObjectId(), new UpdateListener() {

                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Log.i("bmob","未读消息数更新成功");
                                                        loading_dialog.dismiss();

                                                        AlertDialog.Builder builder = new AlertDialog.Builder(advice_feedback.this);
                                                        builder.setMessage("反馈成功，谢谢您对我们的支持！")
                                                                .setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                finish();
                                                            }
                                                        });
                                                        builder.create().show();

                                                    }else{
                                                        Log.i("bmob","未读消息数更新失败："+e.getMessage()+","+e.getErrorCode());
                                                        loading_dialog.dismiss();
                                                    }
                                                }
                                            });
                                        }
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                        loading_dialog.dismiss();
                                    }
                                }
                            });

                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            loading_dialog.dismiss();
                        }//else
                    }//done
                });

//                Toast.makeText(getBaseContext(), "反馈成功，谢谢您对我们的支持！",
//                        Toast.LENGTH_SHORT).show();
//                advice_feedback.this.finish();


            }//onClick

        }
    };
}//advice_feedback