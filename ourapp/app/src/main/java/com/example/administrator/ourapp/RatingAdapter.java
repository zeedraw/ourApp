package com.example.administrator.ourapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.ourapp.imageloader.AsyncImageLoader;
import com.example.administrator.ourapp.imageloader.TagInfo;
import com.example.administrator.ourapp.mymissionadapter.UserItemAdapter;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */

public class RatingAdapter extends ArrayAdapter<MyUser> {

    HashMap<String, Drawable> imgCache;     // 图片缓存
    AsyncImageLoader loader;                // 异步加载图片类
    // HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    private int res;                        //item布局
    private Float scoreList[];//储存评分
    private String commentList[];//储存评语
    private List<MyUser> mlist;
    private ViewHolder viewHolder=null;
    public RatingAdapter(Context context, int resource, List<MyUser> objects) {
        super(context, resource, objects);
        imgCache=new HashMap<String,Drawable>();
        loader=new AsyncImageLoader(getContext());
        scoreList=new Float[objects.size()];
        for (int i=0;i<scoreList.length;i++)
        {
            scoreList[i]=new Float(5);
        }
        commentList=new String[objects.size()];
        res=resource;
        mlist=objects;
    }



    @NonNull
    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        final MyUser user=getItem(position);
        final ListView listView=(ListView)parent;
        if (convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(res,null);
            viewHolder=new ViewHolder();
            viewHolder.user_image=(ImageView)convertView.findViewById(R.id.user_image);
            viewHolder.user_name=(TextView)convertView.findViewById(R.id.user_name);
            viewHolder.studentDescription=(TextView)convertView.findViewById(R.id.student_des);
            viewHolder.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingbar);
            viewHolder.ratingBar.setTag(position);
            viewHolder.scores=(TextView)convertView.findViewById(R.id.scores);
            viewHolder.scores.setTag("s"+position);
            viewHolder.comment=(TextView)convertView.findViewById(R.id.comment);
            viewHolder.comment.setTag(position);
            viewHolder.itemClick=(RelativeLayout)convertView.findViewById(R.id.userInfo_click);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder) convertView.getTag();
            viewHolder.ratingBar.setTag(position);
            viewHolder.comment.setTag(position);
            viewHolder.scores.setTag("s"+position);
        }

        String imgurl = user.getUserimage().getUrl(); // 得到该项所代表的url地址
        Drawable drawable = imgCache.get(imgurl);  // 先去缓存中找

        TagInfo tag = new TagInfo();
        tag.setPosition(position);  //保存当前位置
        tag.setUrl(imgurl);         // 保存当前项所要加载的url
        viewHolder.user_image.setTag(position);

        if (null != drawable) {                         // 找到了直接设置为图像
            viewHolder.user_image.setImageDrawable(drawable);
        } else {                                      // 没找到则开启异步线程
            drawable = loader.loadDrawableByTag(tag, new AsyncImageLoader.ImageCallBack() {

                @Override
                public void obtainImage(TagInfo ret_info) {

                    imgCache.put(ret_info.getUrl(), ret_info.getDrawable());    // 首先把获取的图片放入到缓存中

                    // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                    ImageView person_iv = (ImageView) listView.findViewWithTag(ret_info.getPosition());
                    Log.i("z", "person_iv: " + person_iv + " position: " + ret_info.getPosition());
                    if (null != person_iv)
                        person_iv.setImageDrawable(ret_info.getDrawable());
                }
            });

            if (drawable != null) {
                viewHolder.user_image.setImageDrawable(drawable);
            }
        }
        viewHolder.user_name.setText(user.getName());
        viewHolder.studentDescription.setText(user.getStuDescription());

        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                TextView tv=(TextView) listView.findViewWithTag("s"+ratingBar.getTag());
                tv.setText(v+"分");
                scoreList[(int)ratingBar.getTag()]=v;
                Log.i("z","scoreList"+ratingBar.getTag()+"="+v);
            }
        });

        class MyTextWatcher implements TextWatcher {
            public MyTextWatcher(ViewHolder holder) {
                mHolder = holder;
            }

            private ViewHolder mHolder;

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s != null && !"".equals(s.toString())) {
//                    int position = (Integer) mHolder.value.getTag();
//                    mData.get(position).put("list_item_inputvalue",
//                            s.toString());// 当EditText数据发生改变的时候存到data变量中
//                }
//            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable!=null){
                    int position=(int)mHolder.comment.getTag();
                    commentList[position]=editable.toString().trim();
                    Log.i("z","position:"+position+";addstring"+commentList[position]);
                }
            }
        }
        viewHolder.comment.addTextChangedListener(new MyTextWatcher(viewHolder));

        viewHolder.itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyAccount.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView user_image;
        TextView user_name;
        TextView studentDescription;
        RatingBar ratingBar;
        TextView scores;
        TextView comment;
        RelativeLayout itemClick;

    }
    public MyUser getUser(int p)
    {
        return mlist.get(p);
    }

    public Float[] getScoreList(){return scoreList;}

    public String[] getCommentList(){return commentList;}



}
