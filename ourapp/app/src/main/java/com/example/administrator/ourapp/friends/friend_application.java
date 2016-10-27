package com.example.administrator.ourapp.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.message.Message;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/25.
 * 好友申请页面
 */
public class friend_application extends Activity {
    // 从消息的点击事件跳转而来 功能为 根据信息加载相关页面 并根据拒绝或者同意按钮发送相关信息
    // 点击同意按钮时同时完成单方面添加好友的功能

    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private TextView apply_information_widget;
    private TextView agree_to_add_friend;
    private TextView refuse_to_add_friend;
    private String applicant_ID; //好友申请人的ID
    private String message_ID;
    private TextView fre_username;
    private TextView fre_introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firend_application);
        initwidget();
        set_message_be_viewed();
    }



    private void initwidget() {

        return_bt = (TextView) findViewById(R.id.lbt);
//        commit_bt = (TextView) findViewById(R.id.rbt);
        agree_to_add_friend = (TextView) findViewById(R.id.agree_to_add_friend);
        refuse_to_add_friend = (TextView) findViewById(R.id.refuse_to_add_friend);
        info_title = (TextView) findViewById(R.id.mission_title);
        apply_information_widget = (TextView) findViewById(R.id.apply_information);
        fre_username = (TextView) findViewById(R.id.fre_username);
        fre_introduction = (TextView) findViewById(R.id.fre_introduction);

        return_bt.setText("返回");
        info_title.setText("好友申请");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friend_application.this.finish();
            }
        });



        Intent intent = getIntent();
        message_ID = intent.getStringExtra("message_ID");

        BmobQuery<Message> query = new BmobQuery<Message>();
        query.getObject(message_ID, new QueryListener<Message>() {

            @Override
            public void done(final Message object, BmobException e) {
                if(e==null){

                    fre_username.setText(object.getSender().getUsername());
                    fre_introduction.setText(object.getSender().getIntroduction());
                    apply_information_widget.setText(object.getContent());




                    agree_to_add_friend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //点击同意后先单方面添加好友成功后在发送消息
                            MyUser user = BmobUser.getCurrentUser(MyUser.class);
                            MyUser new_friend = object.getSender();
                            BmobRelation relation = new BmobRelation();
                            relation.add(new_friend);
                            user.setFriends(relation);
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Log.i("bmob","多对多关联添加成功");
//                                        AlertDialog.Builder builder=new AlertDialog.Builder(friend_application.this);
//                                        builder.setMessage("添加好友成功").setCancelable(false).
//                                                setPositiveButton("确定", null).create().show();

                                        //发送同意添加好友的消息

                                        Message agree_friend_apply_App_message = new Message();
                                        agree_friend_apply_App_message.setSender(BmobUser.getCurrentUser(MyUser.class));
                                        agree_friend_apply_App_message.setContent(BmobUser.getCurrentUser(MyUser.class).getUsername()+
                                                "同意添加您为好友");
                                        agree_friend_apply_App_message.setType(1);    //1代表同意添加好友消息
                                        agree_friend_apply_App_message.setBe_viewed(false);
                                        agree_friend_apply_App_message.setReceiver(object.getSender());

                                        agree_friend_apply_App_message.save(new SaveListener<String>() {

                                            @Override
                                            public void done(String objectId, BmobException e) {
                                                if(e==null){
                                                    friend_application.this.finish();
                                                }else{
                                                    Log.i("bmob","消息发送失败："+e.getMessage()+","+e.getErrorCode());
                                                }
                                            }
                                        });

                                    }else{
                                        Log.i("bmob","好友添加失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }

                            });

                        }
                    });

                    refuse_to_add_friend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Message agree_friend_apply_App_message = new Message();
                            agree_friend_apply_App_message.setSender(BmobUser.getCurrentUser(MyUser.class));
                            agree_friend_apply_App_message.setContent(BmobUser.getCurrentUser(MyUser.class).getUsername()+
                                    "拒绝添加您为好友");
                            agree_friend_apply_App_message.setType(2);    //2代表拒绝添加好友消息
                            agree_friend_apply_App_message.setBe_viewed(false);
                            agree_friend_apply_App_message.setReceiver(object.getSender());

                            agree_friend_apply_App_message.save(new SaveListener<String>() {

                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){
                                        friend_application.this.finish();
                                    }else{
                                        Log.i("bmob","消息发送失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                            friend_application.this.finish();
                        }
                    });


                }else{
                    Log.i("bmob","消息获取失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

    }//initwidget

    private void set_message_be_viewed() {


    }//set_message_be_viewed
}
