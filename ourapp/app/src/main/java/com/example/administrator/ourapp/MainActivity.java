package com.example.administrator.ourapp;


import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
    CheckedTextView tv_main,tv_mes,tv_mine,tv_fre;//下方的3个tab
    TextView title;//上方标题
    FragmentManager mfragManager;
    FragmentTransaction mTransaction;
    TextView r_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //第一：默认初始化Bmob
        Bmob.initialize(this, "f7ff174553704fa24b1a4f83dea2e4aa");
        setContentView(R.layout.maininterface);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
        initWidget();

        MyUser manager=new MyUser();
        manager.setPassword("zx2724628");
        manager.setUsername("zeedraw");
        manager.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e==null)
                {
                    Toast.makeText(getApplicationContext(),"管理员创建成功",Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                }
            }

        });
        MyUser.logOut();




    }
    private  void initWidget(){
        //下部tabs中的3个组件
        tv_main=(CheckedTextView)findViewById(R.id.main);
        tv_main.setOnClickListener(this);
        tv_mes=(CheckedTextView)findViewById(R.id.mes);
        tv_mes.setOnClickListener(this);
        tv_mine=(CheckedTextView)findViewById(R.id.mine);
        tv_mine.setOnClickListener(this);
        tv_main.setChecked(true);
        tv_fre=(CheckedTextView)findViewById(R.id.fre);
        tv_fre.setOnClickListener(this);


        //设置activity的标题
        title=(TextView)findViewById(R.id.title);
        title.setText(tv_main.getText());

        //默认首页
         mfragManager=getSupportFragmentManager();
         mTransaction=mfragManager.beginTransaction();
        mTransaction.add(R.id.frag_container,new MineFrag(),"mine")
                .add(R.id.frag_container,new MesFrag(),"mes")
                .add(R.id.frag_container,new FreFrag(),"fre")
                .add(R.id.frag_container,new MainFrag(),"main")
                .commit();




        r_button=(TextView)findViewById(R.id.rbt);

    }



    @Override
    public void onClick(View view) {//下部tab的监听事件

        if(view==tv_main)
        {
            tv_main.setChecked(true);
            tv_mes.setChecked(false);
            tv_mine.setChecked(false);
            tv_fre.setChecked(false);
            title.setText(tv_main.getText());
            r_button.setVisibility(View.INVISIBLE);
            mTransaction=mfragManager.beginTransaction();
            mTransaction.show(mfragManager.findFragmentByTag("main"))
                    .hide(mfragManager.findFragmentByTag("mes"))
                    .hide(mfragManager.findFragmentByTag("mine"))
                    .hide(mfragManager.findFragmentByTag("fre")).commit();

        }

        else if (view==tv_mes)
        {
            tv_main.setChecked(false);
            tv_mes.setChecked(true);
            tv_mine.setChecked(false);
            tv_fre.setChecked(false);
            title.setText(tv_mes.getText());


            mTransaction=mfragManager.beginTransaction();
            mTransaction.hide(mfragManager.findFragmentByTag("main"))
                    .show(mfragManager.findFragmentByTag("mes"))
                    .hide(mfragManager.findFragmentByTag("mine"))
                    .hide(mfragManager.findFragmentByTag("fre")).commit();

        }
        else if (view==tv_mine)
        {
            tv_main.setChecked(false);
            tv_mes.setChecked(false);
            tv_mine.setChecked(true);
            tv_fre.setChecked(false);
            title.setText(tv_mine.getText());
            r_button.setVisibility(View.INVISIBLE);
            mTransaction=mfragManager.beginTransaction();
            mTransaction.hide(mfragManager.findFragmentByTag("main"))
                    .hide(mfragManager.findFragmentByTag("mes"))
                    .show(mfragManager.findFragmentByTag("mine"))
                    .hide(mfragManager.findFragmentByTag("fre")).commit();

            r_button.setText("登出");
            r_button.setVisibility(View.VISIBLE);
            r_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(false)
                            .setTitle("提醒")
                            .setMessage("确定登出？");
                    setPositive(builder);
                    setNegative(builder)
                            .create()
                            .show();
                }
            });
        }

        else if(view==tv_fre)
        {
            tv_fre.setChecked(true);
            tv_main.setChecked(false);
            tv_mine.setChecked(false);
            tv_mes.setChecked(false);
            title.setText(tv_fre.getText());
            r_button.setText("添加");
            r_button.setVisibility(View.VISIBLE);

            mTransaction=mfragManager.beginTransaction();
            mTransaction.hide(mfragManager.findFragmentByTag("main"))
                    .hide(mfragManager.findFragmentByTag("mes"))
                    .hide(mfragManager.findFragmentByTag("mine"))
                    .show(mfragManager.findFragmentByTag("fre")).commit();

        }


    }



    //以下四个方法对应MineFrag中的点击事件
    public void Myaccount_click(View view)
{
    ComponentName comp=new ComponentName(MainActivity.this,MyAccount.class);
    Intent intent=new Intent();
    intent.setComponent(comp);
    startActivity(intent);
}

    public void Mysetting_click(View view)
    {
        ComponentName comp=new ComponentName(MainActivity.this,MySetting.class);
        Intent intent=new Intent();
        intent.setComponent(comp);
        startActivity(intent);

    }

    public  void Mymssion_click(View view)
    {
        MyUser user= BmobUser.getCurrentUser(MyUser.class);
        if (user!=null) {
            ComponentName comp = new ComponentName(MainActivity.this, my_task.class);
            Intent intent = new Intent();
            intent.setComponent(comp);
            startActivity(intent);
        }

        else {
            Intent intent=new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }
    }

    public void Myteam_click(View view)
    {

    }

    private AlertDialog.Builder setPositive(AlertDialog.Builder builder)
    {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BmobUser.logOut();

            }
        });
    }

    private AlertDialog.Builder setNegative(AlertDialog.Builder builder)
    {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }


}



