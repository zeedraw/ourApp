package com.example.administrator.ourapp.mymissionadapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ourapp.CheckPeople;
import com.example.administrator.ourapp.ChoosePeople;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MissionInfo;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;
import com.example.administrator.ourapp.message.Message_tools;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/22.
 */
public class PubMissionApplyAdapter extends ArrayAdapter<Mission>{
    private int res;                        //item布局
    private List<Mission> mlist;
    public PubMissionApplyAdapter(Context context, int resource, List<Mission> objects) {
        super(context, resource, objects);

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
            viewHolder.mission_tag=(TextView)convertView.findViewById(R.id.mission_tag);
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

        String tag=mission.getTag();
        if (tag.equals("活动"))
        {
            viewHolder.mission_tag.setText("活动");
            viewHolder.mission_tag.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.act_tag));

        }
        else if(tag.equals("教育"))
        {
            viewHolder.mission_tag.setText("教育");
            viewHolder.mission_tag.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.edu_tag));

        }
        else if(tag.equals("交通"))
        {
            viewHolder.mission_tag.setText("交通");
            viewHolder.mission_tag.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.trans_tag));

        }
        else if(tag.equals("社区"))
        {
            viewHolder.mission_tag.setText("社区");
            viewHolder.mission_tag.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.com_tag));

        }


        viewHolder.organization.setText(mission.getPub_user().getOrgDescription());
        viewHolder.mission_title.setText(mission.getName());
        viewHolder.mission_abs.setText(mission.getIntro());
        viewHolder.mission_time.setText(mission.getStart_time()+"到"+mission.getEnd_time());
        List<String> list=mission.getLocation_abs();
        String locationAbs=list.get(0)+"-"+list.get(1)+"-"+list.get(2);
        viewHolder.location_abs.setText(locationAbs);

        viewHolder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(getContext(), ChoosePeople.class);
//                intent.putExtra("missionId",mission.getObjectId());
//                Log.i("z",mission.getName()+".................");
//                getContext().startActivity(intent);

                Intent intent=new Intent(getContext(), ChoosePeople.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("mission",mission);
                intent.putExtras(bundle);
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

                                BmobQuery<MyUser> query = new BmobQuery<MyUser>();

                                query.addWhereRelatedTo("get_user", new BmobPointer(mission));
                                query.findObjects(new FindListener<MyUser>() {

                                    @Override
                                    public void done(List<MyUser> object,BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","查询个数："+object.size());
                                            Message_tools sm = new Message_tools();
                                            for(int i = 0; i < object.size(); ++i){
                                                sm.send(BmobUser.getCurrentUser(MyUser.class), object.get(i),
                                                        "恭喜您，您参加的"+ mission.getName() + "活动已经开始！",
                                                        5, false, mission.getObjectId(), getContext());
                                            }//for
                                        }else{
                                            Log.i("bmob","失败："+e.getMessage());
                                        }
                                    }

                                });

                                BmobQuery<MyUser> query1 = new BmobQuery<MyUser>();

                                query1.addWhereRelatedTo("cur_people", new BmobPointer(mission));
                                query1.findObjects(new FindListener<MyUser>() {

                                    @Override
                                    public void done(List<MyUser> object,BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","查询个数："+object.size());
                                            Message_tools sm = new Message_tools();
                                            for(int i = 0; i < object.size(); ++i){
                                                sm.send(BmobUser.getCurrentUser(MyUser.class), object.get(i),
                                                        "很遗憾，您并没有被"+ mission.getName() + "活动选为志愿者，去首页看看其他任务吧。",
                                                        6, false, mission.getObjectId(), getContext());
                                            }//for
                                        }else{
                                            Toast.makeText(getContext(), "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        TextView mission_tag;
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
