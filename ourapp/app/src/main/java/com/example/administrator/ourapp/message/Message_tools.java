package com.example.administrator.ourapp.message;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.UserInfo;

import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/28.
 */
public class Message_tools {
    boolean IsUnreadMessage = false;

    public void send(final MyUser sender, final MyUser receiver, final String content,
                     Integer type, boolean be_viewed, String remark, final Context context){

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
                final Dialog loading_dialog = MainActivity.createLoadingDialog(context);
                loading_dialog.show();
                if(e==null){
                    Log.i("bomb","发送信息成功：" + objectId);

                    BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                    query.addWhereEqualTo("user" ,receiver.getObjectId());
                    query.setLimit(50);
                    query.findObjects(new FindListener<UserInfo>() {
                        @Override
                        public void done(List<UserInfo> object, BmobException e) {
                            if(e==null){

                                for (UserInfo user : object) {
                                    user.addUnread_message_num();
                                    user.update(user.getObjectId(), new UpdateListener() {

                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                Log.i("bmob","未读消息数更新成功");
                                                loading_dialog.dismiss();
                                            }else{
                                                Log.i("bmob","未读消息数更新失败："+e.getMessage()+","+e.getErrorCode());
                                                loading_dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                loading_dialog.dismiss();
                            }
                        }
                    });

//                    BmobPushManager bmobPush = new BmobPushManager();
//                    BmobQuery<MyBmobInstallation> query = BmobInstallation.getQuery();
//                    query.addWhereEqualTo("uid", receiver.getObjectId());
//                    bmobPush.setQuery(query);
//                    bmobPush.pushMessage("新消息");

//                    BmobPushManager bmobPush = new BmobPushManager();
//                    BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
//                    query.addWhereEqualTo("deviceType", "android");
//                    bmobPush.setQuery(query);
//                    bmobPush.pushMessage("消息内容");
//                    Log.i("bomb","已发送推送");
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    loading_dialog.dismiss();
                }//else
            }//done
        });
    }//send

//    public boolean is_unread_message(String user_Id){
//        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
//        query.addWhereEqualTo("user", user_Id);
//        query.setLimit(50);
//        query.findObjects(new FindListener<UserInfo>() {
//            @Override
//            public void done(List<UserInfo> object, BmobException e) {
//                if(e==null){
//                    if(object.get(0).getUnread_message_num() == 0)
//                        IsUnreadMessage = false;
//                    else if (object.get(0).getUnread_message_num() > 0)
//                        IsUnreadMessage = true;
//                }else{
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                }
//            }
//        });
//
//        return IsUnreadMessage;
//    }//is_unread_message
}
