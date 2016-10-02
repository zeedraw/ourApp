package com.example.administrator.ourapp.message;

import android.util.Log;

import com.example.administrator.ourapp.MyUser;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Longze on 2016/9/28.
 */
public class SendMessage {

    public void send(MyUser sender, final MyUser receiver, final String content,
                     Integer type, boolean be_viewed, String remark){
        final Message message = new Message();
        message.setContent(content);
        message.setType(type); //7代表有人提问的消息
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setBe_viewed(be_viewed);
        message.setRemark(remark);
        message.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.i("bomb","发送信息成功：" + objectId);

//                    BmobPushManager bmobPush = new BmobPushManager();
//                    BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
//                    query.addWhereEqualTo("uid", receiver.getObjectId());
//                    bmobPush.setQuery(query);
//                    bmobPush.pushMessage("新消息");

                    BmobPushManager bmobPush = new BmobPushManager();
                    BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                    query.addWhereEqualTo("deviceType", "android");
                    bmobPush.setQuery(query);
                    bmobPush.pushMessage("消息内容");
                    Log.i("bomb","已发送推送");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }//else
            }//done
        });
    }//send

}
