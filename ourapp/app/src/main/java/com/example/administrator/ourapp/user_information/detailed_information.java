package com.example.administrator.ourapp.user_information;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/24.
 * 用户的详细信息
 */
public class detailed_information extends Activity {
    private TextView delete_friend; //删除好友按钮
    private TextView return_bt,commit_bt;//标题上的左右按钮
    private TextView info_title;//标题
    private String other_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_detailed_information);
        initWidget();
    }//onCreate

    private void initWidget() {
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.title);
        delete_friend = (TextView) findViewById(R.id.delete_friend);
        Intent intent =  getIntent();

        other_ID = intent.getStringExtra("other_ID");
        return_bt.setText("返回");
        info_title.setText("详细信息");

        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailed_information.this.finish();
            }
        });

        delete_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MyUser user = new MyUser();
//                user.setObjectId(BmobUser.getCurrentUser(MyUser.class).getObjectId());
                MyUser user = BmobUser.getCurrentUser(MyUser.class);
                MyUser other = new MyUser();
                other.setObjectId(other_ID);
                BmobRelation relation = new BmobRelation();
                relation.remove(other);  //被删除好友的ID
                user.setFriends(relation);
                user.update(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            AlertDialog.Builder builder=new AlertDialog.Builder(detailed_information.this);
                            builder.setMessage("好友删除成功").setCancelable(false).setPositiveButton("确定", null).create().show();
                            Log.i("bmob","关联关系删除成功");
                        }else{
                            Log.i("bmob","失败："+e.getMessage());
                        }
                    }

                });
            }
        });
    }//initWidget
}
