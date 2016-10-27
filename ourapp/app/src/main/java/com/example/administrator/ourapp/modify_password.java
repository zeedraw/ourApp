package com.example.administrator.ourapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Longze on 2016/10/24.
 */
public class modify_password extends SwipeBackActivity {
    private EditText old_password;
    private EditText new_password;
    private EditText confirm_password;
    private TextView submit;
    private TextView return_bt;//标题上的左右按钮
    private TextView info_title;//标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_password);
        initWidget();
    }//onCreate

    private void initWidget() {
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        submit = (TextView) findViewById(R.id.submit);
        return_bt = (TextView) findViewById(R.id.lbt);
        info_title = (TextView) findViewById(R.id.mission_title);

        return_bt.setText("返回");
        info_title.setText("提问");
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modify_password.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_password();
            }
        });



    }//initWidget

    private void change_password() {
        if(old_password.length() == 0){
            Toast.makeText(modify_password.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }//if

        else if(new_password.length() == 0){
            Toast.makeText(modify_password.this, "请输入新的密码", Toast.LENGTH_SHORT).show();
            return;
        }//if

        else if(confirm_password.length() == 0){
            Toast.makeText(modify_password.this, "请确认新密码", Toast.LENGTH_SHORT).show();
            return;
        }//if

        else if(new_password.length() > 16 || new_password.length() < 6){
            Toast.makeText(modify_password.this, "密码需要在6-16位之间，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }//if

        else if(!new_password.getText().toString().equals(confirm_password.getText().toString())){
            Toast.makeText(modify_password.this, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }//if



//        else if(!old_password.getText().toString().equals(BmobUser.getCurrentUser(MyUser.class).getObjectByKey("password"))){
//            Toast.makeText(modify_password.this, "原密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
//            return;
//        }

        else{
            BmobUser.updateCurrentUserPassword(old_password.getText().toString(), new_password.getText().toString(), new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Toast.makeText(modify_password.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(modify_password.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }



    }//change_password
}//modify_password
