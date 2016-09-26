package com.example.administrator.ourapp.message;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

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
        loader=new AsyncImageLoader();
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

//        加载网络图片功能
//        String imgurl= message.getUserimage().getUrl(); // 得到该项所代表的url地址
//        Drawable drawable = imgCache.get(imgurl);  // 先去缓存中找
//
//        TagInfo tag = new TagInfo();
//        tag.setPosition(position);  //保存当前位置
//        tag.setUrl(imgurl);         // 保存当前项所要加载的url
//        viewHolder.message_image.setTag(position);
//
//        if(null!=drawable){                         // 找到了直接设置为图像
//            viewHolder.message_image.setImageDrawable(drawable);
//        }else {                                      // 没找到则开启异步线程
//            drawable = loader.loadDrawableByTag(tag, new AsyncImageLoader.ImageCallBack() {
//
//                @Override
//                public void obtainImage(TagInfo ret_info) {
//
//                    imgCache.put(ret_info.getUrl(), ret_info.getDrawable());    // 首先把获取的图片放入到缓存中
//
//                    // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
//                    ImageView person_iv = (ImageView)listView.findViewWithTag(position);
//                    Log.i("z", "person_iv: " +person_iv  + " position: " + ret_info.getPosition());
//                    if (null != person_iv)
//                        person_iv.setImageDrawable(ret_info.getDrawable());
//                }
//            });
//
//            if (drawable!=null) {
//                viewHolder.message_image.setImageDrawable(drawable);
//            }
//        }
        String MessageType = "未知消息";
        Drawable MessageImage = ContextCompat.getDrawable(getContext(), R.drawable.friend_request);

        switch(message.getType()){
            case 0:
                MessageType = "好友请求";
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
