package com.example.administrator.ourapp.mymissionadapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.CheckPeople;
import com.example.administrator.ourapp.ChoosePeople;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;
import com.example.administrator.ourapp.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/22.
 */
public class PubMissionApplyAdapter extends ArrayAdapter<Mission>{
    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private List<Mission> mlist;
    public PubMissionApplyAdapter(Context context, int resource, List<Mission> objects) {
        super(context, resource, objects);
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(getContext());
        res=resource;
        mlist=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Mission mission=getItem(position); //数据项
        final ListView listView=(ListView)parent;
        ViewHolder viewHolder=null;
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(res,null);
            viewHolder=new ViewHolder();
            viewHolder.person_image=(ImageView)convertView.findViewById(R.id.person_image);
            viewHolder.mission_title=(TextView)convertView.findViewById(R.id.mission_title);
            viewHolder.organization=(TextView)convertView.findViewById(R.id.organization);
            viewHolder.mission_abs=(TextView)convertView.findViewById(R.id.mission_abs);
            viewHolder.mission_time=(TextView)convertView.findViewById(R.id.mission_time);
            viewHolder.location_abs=(TextView)convertView.findViewById(R.id.location_abs);
            viewHolder.check=(TextView) convertView.findViewById(R.id.checkpeople_tv);
            viewHolder.choose=(TextView) convertView.findViewById(R.id.choosepeople_tv);
            viewHolder.start=(TextView) convertView.findViewById(R.id.start_tv);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        if (mission.getPub_user().getObjectId().equals(BmobUser.getObjectByKey("objectId")))
        {
            Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getContext())+"/user_image.png");
            Drawable localdrawable=new BitmapDrawable(bitmap);
            viewHolder.person_image.setImageDrawable(localdrawable);
            Log.i("z","本地任务用户头像加载成功");
        }
        else {
            if (mission.getPub_user().getUserimage() != null) {
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
            } else {
                viewHolder.person_image.setImageDrawable(getContext().getResources().getDrawable(R.drawable.defaulticon));
            }
        }

        viewHolder.organization.setText(mission.getPub_user().getOrgDescription());
        viewHolder.mission_title.setText(mission.getName());
        viewHolder.mission_abs.setText(mission.getIntro());
        viewHolder.mission_time.setText(mission.getStart_time()+"到"+mission.getEnd_time());
        viewHolder.location_abs.setText(mission.getLocation_abs());

        viewHolder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ChoosePeople.class);
                intent.putExtra("missionId",mission.getObjectId());
                Log.i("z",mission.getName()+".................");
                getContext().startActivity(intent);
            }
        });
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), CheckPeople.class);
                intent.putExtra("missionId",mission.getObjectId());
                intent.putExtra("origin","publisher");
                getContext().startActivity(intent);
            }
        });
        viewHolder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setMessage("确认以当前志愿者开始");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            Mission missionPointer=new Mission();
                        missionPointer.setObjectId(mission.getObjectId());
                        missionPointer.setState(new Integer(3));
                        missionPointer.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                Log.i("z","开始任务成功");
                                //TODO  给相关人员推送消息[已推送被选中人员 还未同意未被选中人员]

                                BmobQuery<MyUser> query = new BmobQuery<MyUser>();

                                query.addWhereRelatedTo("get_user", new BmobPointer(mission));
                                query.findObjects(new FindListener<MyUser>() {

                                    @Override
                                    public void done(List<MyUser> object,BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","查询个数："+object.size());

                                            List<BmobObject> messages = new ArrayList<BmobObject>();
                                            for (int i = 0; i < object.size(); i++) {
                                                Message message = new Message();
                                                message.setSender(BmobUser.getCurrentUser(MyUser.class));
                                                message.setReceiver(object.get(i));
                                                message.setType(5); //5为申请的任务开始信息
                                                message.setBe_viewed(false);
                                                message.setContent("恭喜您，您参加的"+ mission.getName() +
                                                        "活动已经开始！");
                                                messages.add(message);
                                            }
//第二种方式：v3.5.0开始提供
                                            new BmobBatch().insertBatch(messages).doBatch(new QueryListListener<BatchResult>() {

                                                @Override
                                                public void done(List<BatchResult> o, BmobException e) {
                                                    if(e==null){
                                                        for(int i=0;i<o.size();i++){
                                                            BatchResult result = o.get(i);
                                                            BmobException ex =result.getError();
                                                            if(ex==null){
                                                                Log.i("给申请者发送成功消息", "第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                                                            }else{
                                                                Log.i("给申请者发送成功消息", "第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                                                            }
                                                        }
                                                    }else{
                                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                    }
                                                }
                                            });



                                        }else{
                                            Log.i("bmob","失败："+e.getMessage());
                                        }
                                    }

                                });






                                mlist.remove(mission);
                                PubMissionApplyAdapter.this.notifyDataSetChanged();
                            }
                        });

                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }
        });

        return convertView;
    }

    class ViewHolder{
        ImageView person_image;
        TextView organization;
        TextView mission_title;
        TextView mission_abs;
        TextView mission_time;
        TextView location_abs;
        TextView check,choose,start;

    }
    public Mission getMission(int p)
    {
        return mlist.get(p);
    }
}
