package com.example.administrator.ourapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/9/3.
 */
public class Login extends AppCompatActivity {
    private EditText username,password;
    private Button login,signup;
    private TextView rt,title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username=(EditText)findViewById(R.id.login_username);
        password=(EditText)findViewById(R.id.login_password);
        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        title=(TextView)findViewById(R.id.title);
        title.setText("登录");
        rt=(TextView) findViewById(R.id.lbt);
        rt.setText("返回");
        rt.setVisibility(View.VISIBLE);
        rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            MyUser user = new MyUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.login(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if(e==null)
                        {
                            finish();
                        }
                        else
                        {
                            AlertDialog.Builder builder=new AlertDialog.Builder(Login.this)
                                    .setTitle("登录失败")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("确定",new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                            builder.create().show();
                        }
                    }
                });


           }
      });


    }
}
