package com.example.administrator.ourapp;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.http.bean.Init;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MineFrag extends Fragment implements IListener{

    private TextView name_tv,location_tv;
    private ImageView user_image_iv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("MineFrag",this);
        View view=inflater.inflate(R.layout.main_person, container, false);
        name_tv=(TextView)view.findViewById(R.id.mine_name_tv);
        location_tv=(TextView)view.findViewById(R.id.mine_location_tv);
        user_image_iv=(ImageView)view.findViewById(R.id.mine_userimage_iv);
        initInfo();
        return view;
    }

    @Override
    public void upData() {
        initInfo();

    }

    private void initInfo()
    {
        if (BmobUser.getCurrentUser(MyUser.class)!=null){
        Bitmap bitmap=null;
        try
        {
            bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getContext())+"/user_image.png");
            user_image_iv.setImageBitmap(bitmap);
            Log.i("z","获取更新iv--"+MainActivity.getDiskFileDir(getContext())+"/user_image.png");
        } catch (Exception e)
        {
            // TODO: handle exception
        }}
        else
        {
            user_image_iv.setImageDrawable(getResources().getDrawable(R.drawable.portrait));
        }
        if (BmobUser.getCurrentUser(MyUser.class)==null)
        {
            name_tv.setText("未知用户");
            location_tv.setText("所在地未知");

        }
        else
        {
            name_tv.setText((String)BmobUser.getObjectByKey("name"));
            Log.i("naosumi",(String)BmobUser.getObjectByKey("name"));
            if(BmobUser.getObjectByKey("location")!=null)
            {
                location_tv.setText((String)BmobUser.getObjectByKey("location"));
            }

        }
    }
}
