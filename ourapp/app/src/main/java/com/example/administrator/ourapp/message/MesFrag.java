package com.example.administrator.ourapp.message;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.ourapp.IListener;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.friends.confirm_friend;
import com.example.administrator.ourapp.friends.friend_application;
import com.example.administrator.ourapp.question_and_answer.Mission_question;
import com.example.administrator.ourapp.question_and_answer.question_and_answer;
import com.example.administrator.ourapp.question_and_answer.question_and_answer_detail;
import com.example.administrator.ourapp.question_and_answer.question_and_answer_detail_publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MesFrag extends Fragment implements IListener {
    private ListView listView;
    private Message_Adapter message_adapter;
    private List<Message> message_list;

    private Vector<String> message_content = new Vector<String>();       //消息的内容
    private Vector<Integer> message_type = new Vector<Integer>();    //消息类型
    private Vector<String> message_sender_ID = new Vector<String>();//消息发送者的objectId
    private Vector<String> message_date = new Vector<String>();//消息发送的时间
    private Vector<String> message_ID = new Vector<String>();

//    private Handler handler = new Handler();
//    private Runnable runnable = new Runnable() {
//        public void run() {
//            this.update();
//            handler.postDelayed(this, 1000 * 2);// 间隔2秒
//        }
//        void update() {
//            //刷新msg的内容
//            Load_Message(BmobUser.getCurrentUser(MyUser.class).getObjectId());
//        }
//    };

    //TODO 添加了自动刷新[为不断地从服务器请求数据 若时间允许 改成通过Bmob的通知系统获取然后刷新]
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("MesFrag",this);
        View rootview=inflater.inflate(R.layout.fre_frag, container, false);
        listView=(ListView)rootview.findViewById(R.id.mes_listview);
        String User_ID = BmobUser.getCurrentUser(MyUser.class).getObjectId();
        Load_Message(User_ID);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                message_list.get(i).setBe_viewed(true);
                message_list.get(i).update(message_list.get(i).getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });


                switch(message_type.get(i)){

                    case 0: //跳转到好友申请详情界面
                        Intent intent0 = new Intent(getActivity(), friend_application.class);
                        intent0.putExtra("message_ID", message_ID.get(i));
                        startActivity(intent0);
                        break;

                    case 1: //跳转到确认好友页面（同意添加好友）
//                        Intent intent1 = new Intent(getActivity(), confirm_friend.class);
//                        intent1.putExtra("message_ID", message_ID.get(i));
//                        startActivity(intent1);
//                        break;
                    case 2: //跳转到确认好友界面（拒绝添加好友）
                        Intent intent2 = new Intent(getActivity(), confirm_friend.class);
                        intent2.putExtra("message_ID", message_ID.get(i));
                        startActivity(intent2);
                        break;
                    case 7: //跳转到问答详情界面(任务发布者的）（有新的提问）

                        BmobQuery<Mission_question> query7 = new BmobQuery<Mission_question>();
                        query7.getObject(message_list.get(i).getRemark(), new QueryListener<Mission_question>() {

                            @Override
                            public void done(Mission_question object, BmobException e) {
                                if(e==null){
                                    Intent intent7 = new Intent(getActivity(), question_and_answer_detail_publisher.class);
                                    intent7.putExtra("question_content", object.getContent());
                                    intent7.putExtra("answer_content",object.getanswer().getContent());
                                    intent7.putExtra("question_ID", object.getObjectId());
                                    intent7.putExtra("question_date", object.getCreatedAt());
                                    startActivity(intent7);
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });
                        break;
                    case 8: //跳转到问答详情界面(提问者）（有新的提问）
                        BmobQuery<Mission_question> query8 = new BmobQuery<Mission_question>();
                        query8.getObject(message_list.get(i).getRemark(), new QueryListener<Mission_question>() {

                            @Override
                            public void done(Mission_question object, BmobException e) {
                                if(e==null){
                                    Intent intent8 = new Intent(getActivity(), question_and_answer_detail_publisher.class);
                                    intent8.putExtra("question_content", object.getContent());
                                    intent8.putExtra("answer_content",object.getanswer().getContent());
                                    intent8.putExtra("question_ID", object.getObjectId());
                                    intent8.putExtra("question_date", object.getCreatedAt());
                                    startActivity(intent8);
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });
                        break;

                }//switch 通过switch判断 type分别进入不同的消息页面
               //TODO 通过switch判断 type分别进入不同的消息页面 进入后be_viewed置为true 并修改服务器的数据
            }//onItemClick
        });

//        handler.postDelayed(runnable, 1000 * 3);
        return rootview;
    }//onCreateView


    private void Load_Message(final String User_ID) {
        BmobQuery<Message> query = new BmobQuery<Message>();
//        query.include("answer[content],User[objectId|userimage]");
        query.addWhereEqualTo("receiver", User_ID);
        query.order("-createdAt");
        query.setLimit(100);//TODO 分页加载
        query.findObjects(new FindListener<Message>() {

            @Override
            public void done(List<Message> object, BmobException e) {
                if(e==null){
                    Log.i("z","查询数据成功");
                    if (object.size()!=0) {
                        for (Message message : object) {
                            message_date.add(message.getCreatedAt());
//                            Log.i("z","查询信息创建时间1");
                            message_content.add(message.getContent());
//                            Log.i("z","查询信息的内容2");
                            message_sender_ID.add(message.getSender().getObjectId());
//                            Log.i("z","查询消息发送者的ID3");
                            message_type.add(message.getType());
//                            Log.i("z","查询发布者的ID4");
                            message_ID.add(message.getObjectId());
                            Log.i("z","消息加载成功");
                        }//for

                        message_list = new ArrayList<Message>(object);
                        message_adapter = new Message_Adapter(getContext(), R.layout.message_item, message_list);
                        listView.setAdapter(message_adapter);
                    }
                    else
                    {
                        //Todo: 提示 还没有消息
                    }
                }//if
                else{
//                    Log.i("test","step 6");

                    Log.i("bmob","获取消息数据失败："+e.getMessage()+","+e.getErrorCode());
                }//else
            }//done
        });

    }//Load_Message

    @Override
    public void upData() {

    }

}
