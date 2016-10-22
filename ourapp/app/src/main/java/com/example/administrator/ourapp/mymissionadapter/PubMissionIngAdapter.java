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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.CheckPeople;
import com.example.administrator.ourapp.ChoosePeople;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MissionInfo;
import com.example.administrator.ourapp.MyTask;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.RatingUser;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/9/26.
 */
public class PubMissionIngAdapter extends ArrayAdapter<Mission> {
    private int res;                        //item布局
    private List<Mission> mlist;
    private Context mContext;
    public PubMissionIngAdapter(Context context, int resource, List<Mission> objects) {
        super(context, resource, objects);
        res=resource;
        mlist=objects;
        mContext=context;
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
            viewHolder.mission_abs=(TextView)convertView.findViewById(R.id.mission_abs);
            viewHolder.organization=(TextView)convertView.findViewById(R.id.organization);
            viewHolder.mission_time=(TextView)convertView.findViewById(R.id.mission_time);
            viewHolder.location_abs=(TextView)convertView.findViewById(R.id.location_abs);
            viewHolder.check=(TextView) convertView.findViewById(R.id.checkpeople_tv);
            viewHolder.finish=(TextView)convertView.findViewById(R.id.finish_tv);
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

        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), CheckPeople.class);
                intent.putExtra("missionId",mission.getObjectId());
                Log.i("z",mission.getName()+".................");
                intent.putExtra("origin","publisher");
                getContext().startActivity(intent);
            }
        });

        viewHolder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
//                builder.setMessage("确认完成当前任务？");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Mission missionPointer=new Mission();
//                        missionPointer.setObjectId(mission.getObjectId());
//                        missionPointer.setState(new Integer(4));
//                        missionPointer.update(new UpdateListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if (e==null)
//                                {
//                                    Log.i("z","完成任务");
//                                    mlist.remove(mission);
//                                    PubMissionIngAdapter.this.notifyDataSetChanged();
//                                }
//
//                            }
//                        });
//                    }
//                });
//                builder.create().show();
                Intent intent=new Intent(getContext(), RatingUser.class);
                intent.putExtra("missionId",mission.getObjectId());
                intent.putExtra("position",position);
                ((MyTask)getContext()).startActivityForResult(intent,1000);
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
        TextView check,finish;



    }
}
