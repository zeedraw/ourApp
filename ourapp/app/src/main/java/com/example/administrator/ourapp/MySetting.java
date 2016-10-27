package com.example.administrator.ourapp;

import android.app.Dialog;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ourapp.authenticate.real_name_authenticate;

import java.io.File;

import cn.bmob.v3.BmobUser;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MySetting extends SwipeBackActivity {
    private TextView rt_button;
    private TextView title;
    private LinearLayout advice_feedback;
    private TextView loginout_bt;
//    private TextView modify_password;
    private TextView cacheShow;
    private LinearLayout modify_password;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initWidget();

    }

    private void initWidget()
    {
        rt_button=(TextView) findViewById(R.id.lbt);
        title=(TextView)findViewById(R.id.mission_title);
        advice_feedback=(LinearLayout)findViewById(R.id.advice_feedback);
        loginout_bt=(TextView)findViewById(R.id.loginout_bt);
//        modify_password=(TextView)findViewById(R.id.modify_password);
        modify_password=(LinearLayout) findViewById(R.id.modify_password);
        cacheShow=(TextView)findViewById(R.id.show_cache);
        cacheShow.setText(getCache());

        rt_button.setText("返回");
        rt_button.setVisibility(View.VISIBLE);
        title.setText("设置");

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySetting.this.finish();
            }
        });

        loginout_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser.logOut();
                ListenerManager.getInstance().sendBroadCast(new String[]{"MineFrag"});
                finish();
            }
        });

        advice_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp=new ComponentName(MySetting.this,advice_feedback.class);
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
            }//onClick
        });

        modify_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComponentName comp=new ComponentName(MySetting.this,modify_password.class);
                Intent intent=new Intent();
                intent.setComponent(comp);
                startActivity(intent);
            }//onClick
        });

        if(BmobUser.getCurrentUser(MyUser.class)!=null)
        {
            loginout_bt.setVisibility(View.VISIBLE);
        }
        else
        {
            loginout_bt.setVisibility(View.INVISIBLE);
        }
    }

    public void cacheClick(View view)
    {
        Log.i("z","点击了删除缓存");
        Dialog dialog=MainActivity.createLoadingDialog(this);
        dialog.show();
        DataCleanManager.deleteFilesByDirectory(new File(MainActivity.getDiskCacheDir(getApplicationContext())));
        while (!getCache().equals("0.0Byte"))
        {
            Log.i("z","还未删除成功");
        }
        cacheShow.setText(getCache());
        dialog.dismiss();
    }

    private String getCache()
    {
        String cache=null;
        try {
            cache=DataCleanManager.getCacheSize(new File(MainActivity.getDiskCacheDir(getApplicationContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }
}
