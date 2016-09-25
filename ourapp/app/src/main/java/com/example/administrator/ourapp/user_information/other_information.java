package com.example.administrator.ourapp.user_information;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/22.
 * 查看他人信息时，若不是自己的好友显示的界面
 */

//TODO 通过ID 显示详细信息
public class other_information extends Activity {

    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private Context context;
    private String other_ID;
    private boolean WHETHER_FRIEND = false; //判断显示的用户是否已经为用户的好友

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation);
        initwidget();
    }//onCreate

    private void initwidget() {

        context = getBaseContext();
        commit_bt = (TextView) findViewById(R.id.rbt);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.title);

        Intent intent =  getIntent();

        return_bt.setText("返回");
        info_title.setText("他人信息");

        other_ID = intent.getStringExtra("other_ID");
        WHETHER_FRIEND = intent.getBooleanExtra("IS_FRIEND", false);

//        MyUser other = getOther(other_ID);
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                other_information.this.finish();
            }
        });
        if(WHETHER_FRIEND){ //如果是自己的好友则显示 更多 按钮（内包含删除好友接口）
            commit_bt.setText("更多");
            commit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(other_information.this, detailed_information.class);
                    // 在Intent中传递数据
                    intent.putExtra("other_ID", other_ID);
                    // 启动Intent
                    startActivity(intent);
                }
            });
        }
        if(!WHETHER_FRIEND){//如果上一个界面传递的信息没有确定此用户是不是自己的好友，则通过查询后台来判断
            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            MyUser friends = new MyUser();
            friends.setObjectId(BmobUser.getCurrentUser(MyUser.class).getObjectId());
            query.addWhereRelatedTo("friends", new BmobPointer(friends));
            query.findObjects(new FindListener<MyUser>() {

                @Override
                public void done(List<MyUser> object, BmobException e) {
                    if(e==null){
                        if (object.size() != 0){ //如果该用户有好友
                            for (MyUser user : object) {
                                if( user.getObjectId().equals(other_ID)){
                                    WHETHER_FRIEND = true; //查询证明要显示信息的用户是当前用户的好友
                                    commit_bt.setText("更多");
                                    commit_bt.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(other_information.this, detailed_information.class);
                                            // 在Intent中传递数据
                                            intent.putExtra("other_ID", other_ID);
                                            // 启动Intent
                                            startActivity(intent);

//                                            MyUser user = BmobUser.getCurrentUser(MyUser.class);
//                                            MyUser new_friend = new MyUser();
//                                            new_friend.setObjectId(stranger_ID);
//                                            BmobRelation relation = new BmobRelation();
//                                            relation.add(new_friend);
//                                            user.setFriends(relation);
//                                            user.update(new UpdateListener() {
//                                                @Override
//                                                public void done(BmobException e) {
//                                                    if(e==null){
//                                                        Log.i("bmob","多对多关联添加成功");
//                                                    }else{
//                                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                                                    }
//                                                }
//
//                                            });


                                        }//onClick
                                    });
                                    break;
                                }//if
                            }//for
                        }//if object.size() != 0
                    }else{
                        Log.i("bmob","失败："+e.getMessage());
                    }
                }
            });
        }

        if(!WHETHER_FRIEND){   //若经过查询后台后确定该用户不是当前用户的好友则显示添加好友的入口
            commit_bt.setText("添加");
            commit_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                    MyUser new_friend = new MyUser();
                    new_friend.setObjectId(other_ID);
                    BmobRelation relation = new BmobRelation();
                    relation.add(new_friend);
                    user.setFriends(relation);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("bmob","多对多关联添加成功");
                                AlertDialog.Builder builder=new AlertDialog.Builder(other_information.this);
                                builder.setMessage("添加好友成功").setCancelable(false).
                                        setPositiveButton("确定", null).create().show();
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }

                    });


                    BmobRelation relation2 = new BmobRelation();
                    relation2.add(user);
                    new_friend.setFriends(relation2);
                    new_friend.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.i("bmob","多对多关联添加成功");
//                                AlertDialog.Builder builder=new AlertDialog.Builder(other_information.this);
//                                builder.setMessage("添加好友成功").setCancelable(false).
//                                        setPositiveButton("确定", null).create().show();
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }

                    });

                }//onClick
            });
        }






    }//initwidget

//    private MyUser getOther(String other_ID) {
//        MyUser other = null;
//        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
//        query.getObject("a203eba875", new QueryListener<MyUser>() {
//
//            @Override
//            public void done(MyUser object, BmobException e) {
//                if(e==null){
//                    other = object;
//                }else{
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                }
//            }
//
//        });
//        return other;
//    }//getOther
}
