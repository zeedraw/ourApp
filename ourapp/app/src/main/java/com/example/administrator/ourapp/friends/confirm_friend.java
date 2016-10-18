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
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/25.
 */
public class confirm_friend extends Activity {
    //确认好友的界面 从消息的点击事件转到 功能为加载相关信息 判断是拒绝添加好友还是同意添加好友
    // 若为同意添加好友 点击确认 实现单方面的添加好友
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private TextView information_widget;
    private TextView agree_to_add_friend;
    private String message_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firend_application);
        initwidget();
    }//onCreate

    private void initwidget() {

        return_bt = (TextView) findViewById(R.id.lbt);
//        commit_bt = (TextView) findViewById(R.id.rbt);
        agree_to_add_friend = (TextView) findViewById(R.id.agree_to_add_friend);
        info_title = (TextView) findViewById(R.id.mission_title);

        return_bt.setText("返回");
        info_title.setText("好友确认");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm_friend.this.finish();
            }
        });

        Intent intent = getIntent();
        message_ID = intent.getStringExtra("message_ID");

        BmobQuery<Message> query = new BmobQuery<Message>();
        query.getObject(message_ID, new QueryListener<Message>() {

            @Override
            public void done(Message object, BmobException e) {
                if(e==null){
                    final Integer message_type = object.getType();
                    final String sender_ID = object.getSender().getObjectId();
                    final String sender_name = object.getSender().getUsername();
                    final String agree_or_refuse = message_type == 1 ? "同意" : "拒绝";
                    final String tip = message_type == 1 ? "点击确认正式添加"+sender_name+"为好友" : "";

                    information_widget.setText("用户" + sender_name + agree_or_refuse + "添加您为好友" + tip);
                    //给确认按钮添加点击事件， 若是拒绝信息则直接返回上一个页面， 若是同意信息则添加好友后再返回上一个页面
                    agree_to_add_friend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(message_type == 2){
                                confirm_friend.this.finish();
                            }//if type == 2
                            else{


                                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                                MyUser new_friend = new MyUser();
                                new_friend.setObjectId(sender_ID);
                                BmobRelation relation = new BmobRelation();
                                relation.add(new_friend);
                                user.setFriends(relation);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","多对多关联添加成功");
//                                            AlertDialog.Builder builder=new AlertDialog.Builder(confirm_friend.this);
//                                            builder.setMessage("添加好友成功").setCancelable(false).
//                                                    setPositiveButton("确定", null).create().show();
                                            confirm_friend.this.finish();
                                        }else{
                                            Log.i("bmob","添加好友失败："+e.getMessage()+","+e.getErrorCode());
                                        }//else
                                    }//done

                                });

                            }//else type == 1
                        }//onClick
                    });
                }else{
                    Log.i("bmob","获取消息失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }//initwidget




}
