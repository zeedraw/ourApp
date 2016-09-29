package com.example.administrator.ourapp.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longze on 2016/9/25.
 */
public class Message_Adapter extends ArrayAdapter<Message> {
    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private List<Message> query_list;
    public Message_Adapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(getContext());
        res=resource;
        query_list = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Message message = getItem(position); //数据项
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
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 7:
                MessageType = "新的提问";       //有人对任务进行提问 看到此消息的是发布者
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;
            case 8:
                MessageType = "新的回答";       //发布者对用户的问题进行了回答 看到此消息的是提问者
                MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);
                break;


        }//switch 判断message来给属性赋值

        viewHolder.message_content.setText(message.getContent());
        viewHolder.message_date.setText(message.getCreatedAt().substring(5,15));
        viewHolder.message_type.setText(MessageType);
        viewHolder.message_image.setImageDrawable(MessageImage);
        return convertView;
    }

    class ViewHolder{
        ImageView message_image;
        TextView message_type;
        TextView message_content;
        TextView message_date;

    }
}
