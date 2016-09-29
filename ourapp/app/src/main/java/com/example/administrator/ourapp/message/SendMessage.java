package com.example.administrator.ourapp.message;

import android.util.Log;

import com.example.administrator.ourapp.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Longze on 2016/9/28.
 */
public class SendMessage {

    public void send(MyUser sender, MyUser receiver, String content, Integer type, boolean be_viewed){
        Message message = new Message();
        message.setContent(content);
        message.setType(type); //7代表有人提问的消息
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setBe_viewed(be_viewed);
        message.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.i("bomb","发送信息成功：" + objectId);
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }//send

}
