package com.example.administrator.ourapp.mymissionadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyLocationUtils.MyLocation;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/12/18.
 */

public class ViewPaperMissionAdapter extends BaseAdapter {
    private static final int TYPE_LISTVIEW = 0;
    private static final int TYPE_VIEWPAGER = 1;
    private static final int TYPE_COUNT = 2;

    private View viewPager;
    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private List<Mission> mlist;
    private Context mContext;
    public ViewPaperMissionAdapter(Context context, int resource, List<Mission> objects) {
        super();
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(context);
        res=resource;
        mlist=objects;
        mContext=context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mlist.size()+1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position==0) {
            return viewPager;
        } else {
            return mlist.get(position-1);
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
       ViewHolder viewHolder=null;
        switch(getItemViewType(position)){
            case TYPE_LISTVIEW:
                Mission mission=(Mission)getItem(position); //数据项
                final ListView listView=(ListView)parent;
                if (convertView==null)
                {
                    LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=inflater.inflate(res,null);
                    viewHolder=new ViewHolder();
                    viewHolder.person_image=(ImageView)convertView.findViewById(R.id.person_image);
                    viewHolder.mission_title=(TextView)convertView.findViewById(R.id.mission_title);
                    viewHolder.organization=(TextView)convertView.findViewById(R.id.organization);
                    viewHolder.mission_abs=(TextView)convertView.findViewById(R.id.mission_abs);
                    viewHolder.mission_time=(TextView)convertView.findViewById(R.id.mission_time);
                    viewHolder.location_abs=(TextView)convertView.findViewById(R.id.location_abs);
                    convertView.setTag(viewHolder);
                }
                else
                {
                    viewHolder=(ViewHolder) convertView.getTag();
                }
                if (mission.getPub_user().getObjectId().equals(BmobUser.getObjectByKey("objectId")))
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(mContext)+"/user_image.png");
                    Drawable localdrawable=new BitmapDrawable(bitmap);
                    viewHolder.person_image.setImageDrawable(localdrawable);
                    Log.i("z","本地任务用户头像加载成功");
                }
                else {
                    //   if (mission.getPub_user().getUserimage() != null) {
                    String imgurl = mission.getPub_user().getUserimage().getUrl(); // 得到该项所代表的url地址
                    Drawable drawable = imgCache.get(imgurl);  // 先去缓存中找

                    TagInfo tag = new TagInfo();
                    tag.setPosition(position);  //保存当前位置
                    tag.setUrl(imgurl);         // 保存当前项所要加载的url
                    viewHolder.person_image.setTag(position);

                    if (null != drawable) {                         // 找到了直接设置为图像
                        viewHolder.person_image.setImageDrawable(drawable);
                    } else {                                      // 没找到则开启异步线程
                        drawable = loader.loadDrawableByTag(tag, new AsyncImageLoader.ImageCallBack() {

                            @Override
                            public void obtainImage(TagInfo ret_info) {

                                imgCache.put(ret_info.getUrl(), ret_info.getDrawable());    // 首先把获取的图片放入到缓存中

                                // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                                ImageView person_iv = (ImageView) listView.findViewWithTag(position);
                                Log.i("z", "person_iv: " + person_iv + " position: " + ret_info.getPosition());
                                if (null != person_iv)
                                    person_iv.setImageDrawable(ret_info.getDrawable());
                            }
                        });

                        if (drawable != null) {
                            viewHolder.person_image.setImageDrawable(drawable);
                        }
                    }
                    //       }
//        else {
//                viewHolder.person_image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.personimg));
//            }
                }
                viewHolder.organization.setText(mission.getPub_user().getOrgDescription());
                viewHolder.mission_title.setText(mission.getName());
                viewHolder.mission_abs.setText(mission.getIntro());
                viewHolder.mission_time.setText(mission.getStart_time()+"到"+mission.getEnd_time());
                List<String> list=mission.getLocation_abs();
                String locationAbs=list.get(0)+"-"+list.get(1)+"-"+list.get(2);
                viewHolder.location_abs.setText(locationAbs);


                break;
            case TYPE_VIEWPAGER:

                if(convertView==null){
                    convertView=viewPager;
                }
               break;
        }
        return convertView;

    }

    public void setViewPager(View view) {
        // TODO Auto-generated method stub
        viewPager=view;
    }
    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if(viewPager!=null){
            return position > 0 ? TYPE_LISTVIEW : TYPE_VIEWPAGER;
        }else{
            return TYPE_LISTVIEW;
        }

    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return TYPE_COUNT;
    }

    class ViewHolder{
        ImageView person_image;
        TextView organization;
        TextView mission_title;
        TextView mission_abs;
        TextView mission_time;
        TextView location_abs;

    }
    public Mission getMission(int p)
    {
        return mlist.get(p);
    }


}