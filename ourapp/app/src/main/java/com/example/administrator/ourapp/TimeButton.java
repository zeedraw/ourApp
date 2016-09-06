package com.example.administrator.ourapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/9/6.
 */
public class TimeButton extends Button {
    private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "秒后重新获取";
    private String textbefore = "点击获取验证码";
    private Timer t;
    private TimerTask tt;
    private long time;
    private boolean isphone;

    public TimeButton(Context context) {
        super(context);
        TimeButton.this.setText(textbefore);

    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TimeButton.this.setText(textbefore);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textbefore);
                clearTimer();
            }
        }
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                Log.i("yung", time / 1000 + "");
                han.sendEmptyMessage(0x01);
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }


    public void onCreat() {

            initTimer();
            this.setText(time / 1000 + textafter);
            this.setEnabled(false);
            t.schedule(tt, 0, 1000);

        }


    public void onDestroy(){
        clearTimer();
        Log.e("yung", "onDestroy");
    }

    /** * 设置计时时候显示的文本 */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /** * 设置点击之前的文本 */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     *
     * @param lenght
     *            时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }

    public void setIsphone(boolean b)
    {
        this.isphone=b;

    }



}
