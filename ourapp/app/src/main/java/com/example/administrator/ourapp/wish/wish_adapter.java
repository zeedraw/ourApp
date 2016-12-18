package com.example.administrator.ourapp.wish;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Longze on 2016/12/8.
 */
public class wish_adapter extends ArrayAdapter<Wish> {
    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private List<Wish> Wish_list;
    public wish_adapter(Context context, int resource, List<Wish> objects) {
        super(context, resource, objects);
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(getContext());
        res=resource;
        Wish_list=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Wish wish = getItem(position); //数据项
        final ListView listView = (ListView)parent;
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(res,null);
            viewHolder=new ViewHolder();
            viewHolder.user_image=(ImageView)convertView.findViewById(R.id.user_image);
            viewHolder.wish_title=(TextView)convertView.findViewById(R.id.wish_title);
            viewHolder.wish_content=(TextView)convertView.findViewById(R.id.wish_content);
            viewHolder.wish_date=(TextView)convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        String imgurl=wish.getWish_user().getUserimage().getUrl(); // 得到该项所代表的url地址
        Drawable drawable = imgCache.get(imgurl);  // 先去缓存中找

        TagInfo tag = new TagInfo();
        tag.setPosition(position);  //保存当前位置
        tag.setUrl(imgurl);         // 保存当前项所要加载的url
        viewHolder.user_image.setTag(position);

        if(null!=drawable){                         // 找到了直接设置为图像
            viewHolder.user_image.setImageDrawable(drawable);
        }else {                                      // 没找到则开启异步线程
            drawable = loader.loadDrawableByTag(tag, new AsyncImageLoader.ImageCallBack() {

                @Override
                public void obtainImage(TagInfo ret_info) {

                    imgCache.put(ret_info.getUrl(), ret_info.getDrawable());    // 首先把获取的图片放入到缓存中

                    // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                    ImageView person_iv = (ImageView)listView.findViewWithTag(position);
                    Log.i("z", "person_iv: " +person_iv  + " position: " + ret_info.getPosition());
                    if (null != person_iv)
                        person_iv.setImageDrawable(ret_info.getDrawable());
                }
            });

            if (drawable!=null) {
                viewHolder.user_image.setImageDrawable(drawable);
            }
        }

        viewHolder.wish_title.setText(wish.getTitle());
        viewHolder.wish_content.setText(wish.getContent());
        viewHolder.wish_date.setText(wish.getCreatedAt());

        viewHolder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MyAccount.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("user",wish.getWish_user());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });



        return convertView;
    }

    class ViewHolder{
        ImageView user_image;
        TextView wish_title;
        TextView wish_content;
        TextView wish_date;

    }

}
