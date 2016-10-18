package com.example.administrator.ourapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/15.
 */

public class MissionHistoryAdapter extends ArrayAdapter<Mission> {
    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private List<Mission> mlist;
    private List<String> comments;
    public MissionHistoryAdapter(Context context, int resource, List<Mission> objects,List<String> comments) {
        super(context, resource, objects);
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(getContext());
        res=resource;
        mlist=objects;
        this.comments=comments;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Mission mission = mlist.get(position); //数据项
        final ListView listView = (ListView)parent;
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(res,null);
            viewHolder=new ViewHolder();
            viewHolder.title=(TextView)convertView.findViewById(R.id.mission_title);
            viewHolder.missionTag=(TextView)convertView.findViewById(R.id.tag);
            viewHolder.abs=(TextView)convertView.findViewById(R.id.short_description);
            viewHolder.comTime=(TextView)convertView.findViewById(R.id.time);
            viewHolder.comment=(TextView)convertView.findViewById(R.id.comment);
            viewHolder.avater=(ImageView)convertView.findViewById(R.id.person_image);
            convertView.setTag(viewHolder);
        }

        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        String imgurl=mission.getPub_user().getUserimage().getUrl(); // 得到该项所代表的url地址
        if (imgurl!=null) {
            Drawable drawable = imgCache.get(imgurl);  // 先去缓存中找

            TagInfo tag = new TagInfo();
            tag.setPosition(position);  //保存当前位置
            tag.setUrl(imgurl);         // 保存当前项所要加载的url
            viewHolder.avater.setTag(position);

            if (null != drawable) {                         // 找到了直接设置为图像
                viewHolder.avater.setImageDrawable(drawable);
            } else {                                      // 没找到则开启异步线程
                drawable = loader.loadDrawableByTag(tag, new AsyncImageLoader.ImageCallBack() {

                    @Override
                    public void obtainImage(TagInfo ret_info) {

                        imgCache.put(ret_info.getUrl(), ret_info.getDrawable());    // 首先把获取的图片放入到缓存中

                        // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                        ImageView person_iv = (ImageView)listView.findViewWithTag(position);
                        Log.i("z", "person_iv: " + person_iv + " position: " + ret_info.getPosition());
                        if (null != person_iv)
                            person_iv.setImageDrawable(ret_info.getDrawable());
                    }
                });

            }
        }

        else {
            viewHolder.avater.setImageDrawable(getContext().getResources().getDrawable(R.drawable.defaulticon));
        }

        viewHolder.title.setText(mission.getName());
        viewHolder.missionTag.setText(mission.getTag());
        viewHolder.comTime.setText("完成于"+" "+mission.getEnd_time());
        viewHolder.abs.setText(mission.getIntro());
        viewHolder.comment.setText(comments.get(position));


        return convertView;
    }

    class ViewHolder
    {
        TextView title;
        TextView missionTag;
        TextView abs;
        TextView comTime;
        TextView comment;
        ImageView avater;
    }
}
