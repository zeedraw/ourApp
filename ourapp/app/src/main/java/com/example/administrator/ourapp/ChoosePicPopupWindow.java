package com.example.administrator.ourapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.ourapp.R;

/**
 * Created by Administrator on 2016/10/3.
 */

public class ChoosePicPopupWindow extends PopupWindow {
    private TextView takePhoto_tv,pickPhoto_tv,cancel_tv;
    private View rootView;


    public ChoosePicPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView=inflater.inflate(R.layout.choosepicdialog,null);
        takePhoto_tv=(TextView)rootView.findViewById(R.id.take_photo);
        pickPhoto_tv=(TextView)rootView.findViewById(R.id.pick_photo);
        cancel_tv=(TextView)rootView.findViewById(R.id.cancel);

        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        takePhoto_tv.setOnClickListener(itemsOnClick);
        pickPhoto_tv.setOnClickListener(itemsOnClick);
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

    }


    }
