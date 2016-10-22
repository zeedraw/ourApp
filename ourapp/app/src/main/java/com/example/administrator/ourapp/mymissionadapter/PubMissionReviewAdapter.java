package com.example.administrator.ourapp.mymissionadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;

import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/10/21.
 */

public class PubMissionReviewAdapter extends ArrayAdapter<Mission> {

    private int res;                        //item布局
    private List<Mission> mlist;
    public PubMissionReviewAdapter(Context context, int resource, List<Mission> objects) {
        super(context, resource, objects);
        res=resource;
        mlist=objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Mission mission = getItem(position); //数据项
        final ListView listView = (ListView) parent;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(res, null);
            viewHolder = new ViewHolder();
            viewHolder.mission_title = (TextView) convertView.findViewById(R.id.mission_title);
            viewHolder.mission_tag=(TextView)convertView.findViewById(R.id.mission_tag);
            viewHolder.organization = (TextView) convertView.findViewById(R.id.organization);
            viewHolder.mission_abs = (TextView) convertView.findViewById(R.id.mission_abs);
            viewHolder.mission_time = (TextView) convertView.findViewById(R.id.mission_time);
            viewHolder.location_abs = (TextView) convertView.findViewById(R.id.location_abs);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
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
        viewHolder.mission_time.setText(mission.getStart_time() + "到" + mission.getEnd_time());
        List<String> list=mission.getLocation_abs();
        String locationAbs=list.get(0)+"-"+list.get(1)+"-"+list.get(2);
        viewHolder.location_abs.setText(locationAbs);

        return convertView;
    }

    class ViewHolder{
        TextView organization;
        TextView mission_title;
        TextView mission_tag;
        TextView mission_abs;
        TextView mission_time;
        TextView location_abs;

    }
    public Mission getMission(int p)
    {
        return mlist.get(p);
    }
}
