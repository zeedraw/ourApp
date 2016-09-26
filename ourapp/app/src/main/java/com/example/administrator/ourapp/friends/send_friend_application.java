package com.example.administrator.ourapp.friends;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.message.Message;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Longze on 2016/9/25.
 */
public class send_friend_application extends Activity {
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private EditText apply_information;
    private String other_ID;

    /*
    从other_information的添加按钮跳转来 完成加载页面 以及 点击发送按钮时 创建相关的消息 界面为 friend_application

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_friend_application);
        initwidget();
    }//onCreate

    private void initwidget() {

        return_bt = (TextView) findViewById(R.id.lbt);
        commit_bt = (TextView) findViewById(R.id.rbt);
        info_title = (TextView) findViewById(R.id.title);
        apply_information = (EditText) findViewById(R.id.apply_information);

        Intent intent =  getIntent();
        other_ID = intent.getStringExtra("other_ID");

        return_bt.setText("返回");
        commit_bt.setText("发送");
        info_title.setText("身份验证");

        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.getObject(other_ID, new QueryListener<MyUser>() {

            @Override
            public void done(final MyUser object, BmobException e) {
                if(e==null){
                    //TODO 通过ID显示相关的用户信息
                    commit_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Message friend_apply_message = new Message();
                            friend_apply_message.setSender(BmobUser.getCurrentUser(MyUser.class));
                            friend_apply_message.setContent(apply_information.getText().toString());
                            friend_apply_message.setType(0);    //0代表好友申请的消息
                            friend_apply_message.setBe_viewed(false);
                            friend_apply_message.setReceiver(object);

                            friend_apply_message.save(new SaveListener<String>() {

                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){
                                        send_friend_application.this.finish();
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });

                        }
                    });
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_friend_application.this.finish();
            }
        });



    }//initwidget
}
