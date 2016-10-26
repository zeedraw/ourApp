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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/10/25.
 */

public class ForgetPassword extends AppCompatActivity {
    private TextView title,rt_bt,complete;
    private EditText pw,pw_confirm;
    private EditText phoneNum,code;
    private TimeButton send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);

        title = (TextView) findViewById(R.id.mission_title);
        title.setText("忘记秘密");
        rt_bt = (TextView) findViewById(R.id.lbt);
        rt_bt.setText("返回");
        rt_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        phoneNum = (EditText) findViewById(R.id.phonenum_ed);
        code = (EditText) findViewById(R.id.code_ed);

        pw=(EditText)findViewById(R.id.pw_ed);
        pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pw_confirm=(EditText)findViewById(R.id.pwconfirm_ed);
        pw_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());

        send = (TimeButton) findViewById(R.id.sendcode_bt);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhonenum()) {
                    send.onCreat();//TimeButton的计时时间
                    BmobSMS.requestSMSCode(phoneNum.getText().toString(), "注册验证", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer smsId, BmobException e) {
                            if (e == null) {//验证码发送成功
                                Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
                            }

                        }
                    });
                }

                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ForgetPassword.this);
                    builder.setMessage("请输入正确的手机号");
                    builder.create().show();
                }
            }
        });

        complete = (TextView) findViewById(R.id.complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = MainActivity.createLoadingDialog(ForgetPassword.this);
                dialog.show();
                if (phoneNum.getText().toString().length() == 0
                        || code.getText().toString().length() == 0
                        ||pw.getText().toString().length()==0
                        ||pw_confirm.getText().toString().length()==0) {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
                    builder.setMessage("信息不完整");
                    builder.create().show();
                } else if (isPassWord())
                {
                    if (isConfirm())
                    {
                        BmobUser.resetPasswordBySMSCode(code.getText().toString(),pw.getText().toString(), new UpdateListener() {

                            @Override
                            public void done(BmobException ex) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ForgetPassword.this);
                                if(ex==null){
                                    dialog.dismiss();
                                    Log.i("smile", "密码重置成功");
                                    builder.setMessage("重置密码成功");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });

                                }else{
                                    dialog.dismiss();
                                    Log.i("smile", "重置失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                                    builder.setMessage("重置密码失败"+ex.getMessage());

                                }
                                builder.create().show();

                            }
                        });
                    }
                    else
                    {
                        dialog.dismiss();
                        AlertDialog.Builder builder=new AlertDialog.Builder(ForgetPassword.this);
                        builder.setMessage("重置密码和确认密码不同");
                        builder.create().show();
                    }


                }
                else
                {
                    dialog.dismiss();
                    AlertDialog.Builder builder=new AlertDialog.Builder(ForgetPassword.this);
                    builder.setMessage("密码不能小于6位");
                    builder.create().show();
                }
            }
        });
    }

    private boolean isPhonenum() {
        String str = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phoneNum.getText().toString());

        if(!m.matches())
        {

            return false;
        }
        else {
            return true;
        }
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
