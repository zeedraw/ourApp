package com.example.administrator.ourapp;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/7.
 */
public class SignUp extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private EditText name_ed,pw_ed,pwconfirm_ed,phonenum_ed,code_ed;
    private TextView name_tv,pw_tv,pwconfirm_tv,phonenum_tv,code_tv,rt,title;
    private Button signup_bt;
    private TimeButton sendcode_bt;
    private RadioGroup sex_rg;
    private boolean isCode=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        initWidget();

    }
    private void initWidget()
    {
        name_ed=(EditText)findViewById(R.id.name_ed);
        name_ed.setOnFocusChangeListener(this);
        name_tv=(TextView)findViewById(R.id.name_tv);

        sex_rg=(RadioGroup)findViewById(R.id.sex_rg);

        pw_ed=(EditText)findViewById(R.id.pw_ed);
        pw_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pw_ed.setOnFocusChangeListener(this);
        pw_tv=(TextView)findViewById(R.id.pw_tv);

        pwconfirm_ed=(EditText)findViewById(R.id.pwconfirm_ed);
        pwconfirm_ed.setTransformationMethod(PasswordTransformationMethod.getInstance());
        pwconfirm_ed.setOnFocusChangeListener(this);
        pwconfirm_tv=(TextView)findViewById(R.id.pwconfirm_tv);

        phonenum_ed=(EditText)findViewById(R.id.phonenum_ed);
        sendcode_bt=(TimeButton)findViewById(R.id.sendcode_bt);
        sendcode_bt.setOnClickListener(this);
        phonenum_tv=(TextView)findViewById(R.id.phonenum_tv);

        code_ed=(EditText)findViewById(R.id.code_ed);
        code_tv=(TextView)findViewById(R.id.code_tv);

        signup_bt=(Button)findViewById(R.id.signup_bt);
        signup_bt.setOnClickListener(this);

        title=(TextView)findViewById(R.id.mission_title);
        title.setText("注册");

        rt=(TextView)findViewById(R.id.lbt);
        rt.setText("返回");
        rt.setOnClickListener(this);


    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(!b)
        {
            if (view==name_ed)
            {
                isName();
            }
            if (view==pw_ed)
            {
                isPassword();
            }
            if (view==pwconfirm_ed)
            {
                isConfirm();
            }

        }
    }

    @Override
    public void onClick(View view) {
        if(view==sendcode_bt)
        {
                if(isPhonenum())
            {
                sendcode_bt.onCreat();//TimeButton的计时时间
                BmobSMS.requestSMSCode(phonenum_ed.getText().toString(), "注册验证", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer smsId, BmobException e) {
                        if (e == null) {//验证码发送成功
                            Log.i("smile", "短信id：" + smsId);//用于查询本次短信发送详情
                        }

                    }
                });
            }
        }

        if (view==rt)
        {
            finish();
        }
        if (view==signup_bt)
        {
            if(!isCode) {
                isCode();
            }
            if (isName()&&isPassword()&&isConfirm()&&isPhonenum()&&isCode)
            {
                Resources r =getResources();
                MyUser newuser=new MyUser();

                BmobFile image=new BmobFile("defaultimage",null,"http://bmob-cdn-6218.b0.upaiyun.com/2016/10/19/c565a6fa4034de09806e2e5d441b2eac.png");
                newuser.setUserimage(image);
                newuser.setUsername(phonenum_ed.getText().toString());
                newuser.setPassword(pw_ed.getText().toString());
                newuser.setName(name_ed.getText().toString());
                newuser.setSex(sex_rg.getCheckedRadioButtonId()==R.id.male?true:false);
                newuser.setMobilePhoneNumber(phonenum_ed.getText().toString());
                newuser.setMobilePhoneNumberVerified(true);
                newuser.setIdentifiedStudent(false);
                newuser.setIdentifiedPublish(false);
                newuser.signUp(new SaveListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e==null)
                        {
                            UserInfo userInfo=new UserInfo();
                            userInfo.setUser(myUser);
                            userInfo.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(SignUp.this);
                                    builder.setCancelable(false);
                                    if (e==null)
                                    {
                                        builder.setMessage("注册成功");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag","Main"});
                                                finish();
                                            }
                                        });
                                    }
                                    else
                                    {
                                        builder.setMessage("注册失败"+e.getMessage());
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                    }
                                    builder.create().show();
                                }
                            });


                        }
                        else
                        {
                            Log.i("z","创建用户失败");
                        }

                    }
                });
            }

        }

    }

    private boolean isName()
    {
        if (name_ed.getText().toString().length()==0)
        {
            name_tv.setText("昵称不能为空");
            return false;
        }
        else
        {
            name_tv.setText("");
            return true;
        }
    }

    private boolean isPassword()
    {
        if (pw_ed.getText().toString().length()==0)
        {
            pw_tv.setText("密码不能为空");
            return false;
        }
        else
        {
            pw_tv.setText("");
            return true;
        }
    }

    private boolean isConfirm()
    {
        if (pwconfirm_ed.getText().toString().length()==0)
        {
            pwconfirm_tv.setText("该项不能为空");
            return false;
        }
        else if(!(pw_ed.getText().toString().equals(pwconfirm_ed.getText().toString())))
        {
            pwconfirm_tv.setText("与原密码不同");
            return false;
        }
        else
        {
            pwconfirm_tv.setText("");
            return true;
        }
    }

        private boolean isPhonenum() {
        String str = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(phonenum_ed.getText().toString());
        if (phonenum_ed.getText().toString().length()==0)
        {
            phonenum_tv.setText("手机号不能为空");
            return false;
        }
        else if(!m.matches())
        {
            phonenum_tv.setText("请输入正确的手机号");
            return false;
        }
        else {
            phonenum_tv.setText("");
            return true;
        }
    }

    private void isCode()
    {

                if (code_ed.getText().toString().length()==0)
        {
            code_tv.setText("验证码不能为空");
            isCode=false;
        }
        else {
            BmobSMS.verifySmsCode(phonenum_ed.getText().toString(), code_ed.getText().toString(), new UpdateListener() {

                @Override
                public void done(BmobException ex) {
                    if (ex == null) {//短信验证码已验证成功
                        Log.i("smile", "验证通过");
                        code_tv.setText("");
                        isCode=true;
                    }
                    else {
                        Log.i("smile", code_ed.getText().toString()+"验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                        code_tv.setText("验证失败");
                        isCode = false;
                    }
                }
            });
        }
    }
}
