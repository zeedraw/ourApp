package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.SaveListener;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2016/9/3.
 */
public class Login extends SwipeBackActivity {
    private EditText username,password;
    private Button login,signup;
    private TextView forget;
   // private TextView rt,title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.login_username);
        password=(EditText)findViewById(R.id.login_password);
        forget=(TextView)findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,ForgetPassword.class);
                startActivity(intent);
            }
        });

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
//        title=(TextView)findViewById(R.id.mission_title);
//        title.setText("登录");
//        rt=(TextView) findViewById(R.id.lbt);
//        rt.setText("返回");
//        rt.setVisibility(View.VISIBLE);
//        rt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


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


//                            final BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
//                            query.addWhereEqualTo("installationId", BmobInstallation.getInstallationId(getBaseContext()));
//                            query.findObjects(new FindListener<MyBmobInstallation>() {
//
//                                @Override
//                                public void done(List<MyBmobInstallation> object, BmobException e) {
//
//                                    if (e == null) {
//                                        if (object.size() > 0) {
//                                            MyBmobInstallation mbi = object.get(0);
//                                            mbi.setUid(BmobUser.getCurrentUser().getObjectId());
//                                            mbi.update(new UpdateListener() {
//                                                @Override
//                                                public void done(BmobException e) {
//                                                    if(e == null){
//
//                                                        Log.i("bmob", "设备信息更新成功");
//                                                    }
//
//                                                    else{
//
//                                                        Log.i("bmob", "设备信息更新失败:"+e.getMessage()+","+e.getErrorCode());
//                                                    }
//                                                }
//                                            });
//                                        } else {
//                                        }//else
//                                    }//if e == null
//                                }
//                            });





                            Log.i("z","缓存用户成功");
                            BmobFile bf= BmobUser.getCurrentUser(MyUser.class).getUserimage();
                            File saveFile=new File(MainActivity.getDiskFileDir(getApplicationContext()) + "/user_image.png");
                            Log.i("z",MainActivity.getDiskFileDir(getApplicationContext()) + "/user_image.png");
                            bf.download(saveFile, new DownloadFileListener() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e==null){
                                        Log.i("z","下载用户头像成功");
                                    ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag","Main"});
                                    finish();
                                    }
                                    else
                                    {
                                        Log.i("z","下载用户头像失败"+e.getMessage());
                                        Toast.makeText(Login.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onProgress(Integer integer, long l) {

                                }

                                @Override
                                public void doneError(int code, String msg) {
                                    super.doneError(code, msg);
                                }
                            });

                            BmobFile backGround=BmobUser.getCurrentUser(MyUser.class).getBackground();
                            if (backGround!=null) {
                                File saveBack = new File(MainActivity.getDiskFileDir(getApplicationContext()) + "/background.png");
                                backGround.download(saveBack, new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Log.i("z", "缓存用户背景成功");
                                        } else {
                                            Log.i("z", "缓存用户背景失败");
                                            Toast.makeText(Login.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }

                                    @Override
                                    public void doneError(int code, String msg) {
                                        super.doneError(code, msg);
                                    }
                                });
                            }
                        }
                        else
                        {
                            AlertDialog.Builder builder=new AlertDialog.Builder(Login.this)
                                    .setTitle("登录失败")
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("确定",null);
                            builder.create().show();

                        }
                    }
                });


           }
      });


    }
}
