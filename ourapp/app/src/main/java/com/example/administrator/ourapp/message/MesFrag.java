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
import android.widget.Button;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;
import com.example.administrator.ourapp.IListener;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MissionInfo;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.friends.confirm_friend;
import com.example.administrator.ourapp.friends.friend_application;
import com.example.administrator.ourapp.my_task;
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
    private Vector<String> message_mark = new Vector<String>(); //消息的备注（记录任务或者问答的ID）

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

//                            MyUser current_user = new MyUser();
//                            current_user.setIs_new_message(true);
//                            current_user.update(BmobUser.getCurrentUser(MyUser.class).getObjectId()
//                                    , new UpdateListener() {
//
//                                        @Override
//                                        public void done(BmobException e) {
//                                            if(e==null){
//                                                Log.i("bmob","更新成功");
//                                            }else{
//                                                Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
//                                            }
//                                        }
//                                    });

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

                    case 3: //跳转到任务详情界面（通过任务申请）
                        BmobQuery<Mission> query3 = new BmobQuery<Mission>();
                        query3.include("pub_user[userimage]");
                        query3.getObject(message_mark.get(i), new QueryListener<Mission>() {

                            @Override
                            public void done(Mission object, BmobException e) {
                                if(e==null){
                                    Intent intent=new Intent(getContext(), MissionInfo.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("mission",object);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });
                        break;

                    case 4: //有用户报名 跳转到我的任务界面
                        Intent intent4 = new Intent(getActivity(), my_task.class);
//                        intent4.putExtra("message_ID", message_ID.get(i));
                        startActivity(intent4);
                        break;

                    case 5: //跳转到任务详情界面（任务开始）
                        BmobQuery<Mission> query5 = new BmobQuery<Mission>();
                        query5.include("pub_user[userimage]");
                        query5.getObject(message_mark.get(i), new QueryListener<Mission>() {

                            @Override
                            public void done(Mission object, BmobException e) {
                                if(e==null){
                                    Intent intent=new Intent(getContext(), MissionInfo.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("mission",object);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                        break;

                    case 7: //跳转到问答详情界面(任务发布者的）（有新的提问）
                        BmobQuery<Mission_question> query7 = new BmobQuery<Mission_question>();
                        query7.include("answer[content]");
                        query7.getObject(message_list.get(i).getRemark(), new QueryListener<Mission_question>() {

                            @Override
                            public void done(Mission_question object, BmobException e) {
                                if(e==null){
                                    Intent intent7 = new Intent(getContext(), question_and_answer_detail_publisher.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("question",object);
                                    intent7.putExtras(bundle);
                                    startActivity(intent7);
                                }else{
                                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }

                        });
                        break;
                    case 8: //跳转到问答详情界面(提问者）（有新的提问）
                        BmobQuery<Mission_question> query8 = new BmobQuery<Mission_question>();
                        query8.include("answer[content]");
                        query8.getObject(message_list.get(i).getRemark(), new QueryListener<Mission_question>() {

                            @Override
                            public void done(Mission_question object, BmobException e) {
                                if(e==null){
                                    Intent intent8 = new Intent(getContext(), question_and_answer_detail.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putSerializable("question",object);
                                    intent8.putExtras(bundle);
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
                            message_mark.add(message.getRemark());
                            Log.i("z","消息加载成功");
                        }//for

                        message_list = new ArrayList<Message>(object);
                        message_adapter = new Message_Adapter(getContext(),
                                R.layout.message_listview_item, message_list);
                        listView.setAdapter(message_adapter);
                        message_adapter.setMode(Attributes.Mode.Single);

                        MyUser current_user = new MyUser();
                        current_user.setIs_new_message(false);
                        current_user.update(BmobUser.getCurrentUser(MyUser.class).getObjectId()
                                , new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","更新成功");
                                        }else{
                                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });


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
