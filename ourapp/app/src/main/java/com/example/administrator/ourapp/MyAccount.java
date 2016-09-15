package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/8/21.
 */
public class MyAccount extends AppCompatActivity implements IListener{
    private TextView rt_button,edit_button;
    private TextView title;
    private TextView name_tv,sex_tv,age_tv,location_tv,intro_tv;
    private ImageView user_image_iv;
    private MyUser current;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation);
        ListenerManager.getInstance().registerListtener("MyAccount",this);
        initWidget();
        initInfo();


    }

    private void initWidget()
    {
        name_tv=(TextView)findViewById(R.id.info_name_tv);
        sex_tv=(TextView)findViewById(R.id.info_sex_tv);
        age_tv=(TextView)findViewById(R.id.info_age_tv);
        location_tv=(TextView)findViewById(R.id.info_location_tv);
        intro_tv=(TextView)findViewById(R.id.info_intro_tv);

        rt_button=(TextView)findViewById(R.id.lbt);
        edit_button=(TextView)findViewById(R.id.rbt);
        title=(TextView)findViewById(R.id.title);

        rt_button.setText("返回");
        edit_button.setText("编辑");
        title.setText("个人信息");
        rt_button.setVisibility(View.VISIBLE);
        edit_button.setVisibility(View.VISIBLE);

        rt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyAccount.this,EditInfo.class);
                startActivity(intent);
            }
        });

        user_image_iv=(ImageView)findViewById(R.id.info_user_image_iv);


    }

    private void initInfo()
    {
        current= BmobUser.getCurrentUser(MyUser.class);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getApplicationContext()) + "/user_image.png");
                user_image_iv.setImageBitmap(bitmap);
                Log.i("z", "获取更新iv--" + MainActivity.getDiskFileDir(getApplicationContext()) + "/user_image.png"
                +"userid:"+current);
            } catch (Exception e) {
                // TODO: handle exception
            }


        name_tv.setText(current.getName());
        sex_tv.setText(current.getSex()?"男":"女");
        if (current.getBorndate()!=null&&current.getBorndate().length()!=0);
        { age_tv.setText(getAge()+"");}
        if (current.getLocation()!=null)
        {location_tv.setText(current.getLocation());}
        if (current.getIntroduction()!=null)
        {intro_tv.setText(current.getIntroduction());}


    }

    @Override
    public void upData() {
       initInfo();
    }

    private int getAge()
    {
        int age,y,m,d,my,mm,md;
        Calendar calendar=Calendar.getInstance();
        y=calendar.get(Calendar.YEAR);
        m=calendar.get(Calendar.MONTH)+1;
        d=calendar.get(Calendar.DATE);
        String bd=current.getBorndate();
        String[] sourceStrArray = bd.split("-");
        my=Integer.parseInt(sourceStrArray[0]);
        mm=Integer.parseInt(sourceStrArray[1]);
        md=Integer.parseInt(sourceStrArray[2]);
        if (m<mm)
        {
            age=y-my;
        }
        else if (m==mm)
        {
            if (d<md)
            {
                age=y-my;
            }
            else
            {
                age=y-my+1;
            }
        }
        else
        {
            age=y-my+1;
        }
            return age;
    }
}


