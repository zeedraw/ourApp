package com.example.administrator.ourapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/13.
 */

public class WishPopupWindow extends PopupWindow {
    private View rootView;
    private ImageView icon;
    private TextView contact,name,info,time,distance;


    public  WishPopupWindow(Context context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView=inflater.inflate(R.layout.wish_popwindow,null);
        //设置PopupWindow的View
        this.setContentView(rootView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb00000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //rootView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = rootView.getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

        initWidget();
        contact.setOnClickListener(itemsOnClick);
        icon.setOnClickListener(itemsOnClick);
    }

    private void initWidget()
    {
        icon=(ImageView)rootView.findViewById(R.id.person_image);
        name=(TextView)rootView.findViewById(R.id.name);
        contact=(TextView) rootView.findViewById(R.id.contact);
        info=(TextView)rootView.findViewById(R.id.wish_msg);
        time=(TextView)rootView.findViewById(R.id.wish_time);
        distance=(TextView)rootView.findViewById(R.id.wish_distance);
    }

    public void setIcon(Drawable drawable)
    {
        this.icon.setImageDrawable(drawable);
    }

    public void setName(String name)
    {
        this.name.setText(name);
    }

    public void setInfo(String info)
    {
        this.info.setText(info);
    }

    public void setTime(String time)
    {
        this.time.setText(time);
    }

    public void setDistance(String distance)
    {
        this.distance.setText(distance);
    }
}
