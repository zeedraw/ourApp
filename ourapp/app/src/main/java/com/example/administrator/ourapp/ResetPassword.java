package com.example.administrator.ourapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/10/25.
 */

public class ResetPassword extends AppCompatActivity {
    private TextView title,rt_bt,complete;
    private EditText pw,pw_confirm;
    private String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);
        final Dialog dialog=MainActivity.createLoadingDialog(this);
        dialog.show();
      //  final String username=getIntent().getStringExtra("username");

           final String username="18813095229";

        Log.i("z","username="+username);
        BmobQuery<MyUser> query=new BmobQuery<>();
        query.addWhereEqualTo("username",username);
        query.addQueryKeys("password,objectId");
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null)
                {
                    Log.i("z","获取到用户");
                    MyUser user=list.get(0);
                    userId=user.getObjectId();
                    dialog.dismiss();
                }
            }
        });
        title=(TextView)findViewById(R.id.mission_title);
        title.setText("重置密码");
        rt_bt=(TextView)findViewById(R.id.lbt);
        rt_bt.setText("返回");
        rt_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pw=(EditText)findViewById(R.id.pw_ed);
        pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pw_confirm=(EditText)findViewById(R.id.pwconfirm_ed);
        pw_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());

        complete=(TextView)findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog loading=MainActivity.createLoadingDialog(ResetPassword.this);
                loading.show();
                if (pw.getText().toString().length()==0||pw_confirm.getText().toString().length()==0)
                {
                    loading.dismiss();
                    AlertDialog.Builder builder=new AlertDialog.Builder(ResetPassword.this);
                    builder.setMessage("信息填写不完整");
                    builder.create().show();
                }
                else if (isPassWord())
                {
                    if (isConfirm())
                    {
                        MyUser user=new MyUser();
                        user.setPassword(pw.getText().toString().trim());
                        user.update(userId,new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPassword.this);
                                if (e==null)
                                {   loading.dismiss();
                                    builder.setMessage("重置密码成功");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                           finish();
                                        }
                                    });

                                }
                                else
                                {
                                    loading.dismiss();
                                    builder.setMessage("重置密码失败"+e.getMessage());
                                }
                                builder.create().show();

                            }
                        });

                    }

                    else
                    {
                        loading.dismiss();
                        AlertDialog.Builder builder=new AlertDialog.Builder(ResetPassword.this);
                        builder.setMessage("重置密码和确认密码不一致");
                        builder.create().show();
                    }
                }

                else
                {   loading.dismiss();
                    AlertDialog.Builder builder=new AlertDialog.Builder(ResetPassword.this);
                    builder.setMessage("密码不能小于6位");
                    builder.create().show();
                }
            }
        });
    }

    private boolean isPassWord()
    {
        if (pw.getText().toString().length()<6)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isConfirm()
    {

        if(!(pw.getText().toString().equals(pw_confirm.getText().toString())))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
