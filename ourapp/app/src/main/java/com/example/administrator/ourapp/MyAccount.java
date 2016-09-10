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

/**
 * Created by Administrator on 2016/8/21.
 */
public class MyAccount extends AppCompatActivity implements IListener{
    private TextView rt_button,edit_button;
    private TextView title;
    private ImageView user_image_iv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomation);
        ListenerManager.getInstance().registerListtener("MyAccount",this);
        initWidget();


    }

    private void initWidget()
    {
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

    @Override
    public void upData() {
        Bitmap bitmap=null;
        try
        {
                bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getApplicationContext())+"/user_image.png");
                user_image_iv.setImageBitmap(bitmap);
            Log.i("z","获取更新iv--"+MainActivity.getDiskFileDir(getApplicationContext())+"/user_image.png");
        } catch (Exception e)
        {
            // TODO: handle exception
        }
    }
}


