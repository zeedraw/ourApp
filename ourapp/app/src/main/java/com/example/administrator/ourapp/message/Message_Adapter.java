package com.example.administrator.ourapp.message;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.ArraySwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.UserInfo;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.jauker.widget.BadgeView;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/9/25.
 */
public class Message_Adapter extends ArraySwipeAdapter<Message> {
    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private Button delete_btn;
    private List<Message> query_list;
    public Message_Adapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(getContext());
        res=resource;
        query_list = objects;
        this.setMode(Attributes.Mode.Single);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Message message = (Message) getItem(position); //数据项
        final ListView listView = (ListView)parent;
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(res,null);
            viewHolder=new ViewHolder();
            viewHolder.message_image=(ImageView)convertView.findViewById(R.id.message_image);
            viewHolder.message_content=(TextView)convertView.findViewById(R.id.message_content);
            viewHolder.message_date=(TextView)convertView.findViewById(R.id.message_date);
            viewHolder.message_type=(TextView)convertView.findViewById(R.id.message_type);
            viewHolder.be_viewed=(ImageView) convertView.findViewById(R.id.be_viewed);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }


        String MessageType = "未知消息";
        Drawable MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);

        switch(message.getType()){
            case 0:
                MessageType = "好友请求";
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 1:
                MessageType = "同意添加好友";
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 2:
                MessageType = "拒绝添加好友";
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 3:
                MessageType = "通过任务申请";
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 4:
                MessageType = "任务申请";       //有人报名任务  看到此消息的是发布者
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 5:
                MessageType = "任务已开始";
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.ask);
                break;
            case 7:
                MessageType = "新的提问";       //有人对任务进行提问 看到此消息的是发布者
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.ask);
                break;
            case 8:
                MessageType = "新的回答";       //发布者对用户的问题进行了回答 看到此消息的是提问者
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;


        }//switch 判断message来给属性赋值

        viewHolder.message_content.setText(message.getContent());
        viewHolder.message_date.setText(message.getCreatedAt().substring(5,16));
        viewHolder.message_type.setText(MessageType);
        viewHolder.message_image.setImageDrawable(MessageImage);
        viewHolder.be_viewed.setVisibility(View.INVISIBLE);
//        viewHolder.message_content.setBackgroundColor(Color.WHITE);
//        convertView.setBackgroundColor(Color.RED);
        //判断小红点
//        BadgeView badgeView = new BadgeView(getContext());
//        badgeView.setTargetView(viewHolder.message_image);
//        badgeView.setBadgeMargin(0, 0, 0, 0); //左上右下
//        badgeView.setBadgeCount(1);
//        badgeView.setVisibility(badgeView.INVISIBLE);

        if(!message.getBe_viewed()){
            viewHolder.be_viewed.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.be_viewed.setVisibility(View.INVISIBLE);

        }


        delete_btn = (Button) convertView.findViewById(R.id.delete);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 添加删除信息的处理方法

                Message message = query_list.get(position);
                message.setObjectId(message.getObjectId());
                message.delete(new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","成功");


                            BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
                            query.addWhereEqualTo("user" , BmobUser.getCurrentUser(MyUser.class));
                            query.setLimit(50);
                            query.findObjects(new FindListener<UserInfo>() {
                                @Override
                                public void done(List<UserInfo> object, BmobException e) {
                                    if(e==null){

                                        for (UserInfo user : object) {
                                            if(!query_list.get(position).getBe_viewed())
                                                user.subtractUnread_message_num();
                                            if(user.getUnread_message_num() ==0){
                                                ((MainActivity)getContext()).change_signal();
                                            }//if ==0

                                            user.update(user.getObjectId(), new UpdateListener() {

                                                @Override
                                                public void done(BmobException e) {
                                                    if(e==null){
                                                        Log.i("bmob","未读消息数更新成功");
                                                    }else{
                                                        Log.i("bmob","未读消息数更新失败："+e.getMessage()+","+e.getErrorCode());
                                                    }
                                                }
                                            });
                                        }
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });

                            query_list.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(getContext(), "消息已删除", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

            }
        });


        return convertView;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }



    class ViewHolder{
        ImageView message_image;
        TextView message_type;
        TextView message_content;
        TextView message_date;
        ImageView be_viewed;

    }
}
