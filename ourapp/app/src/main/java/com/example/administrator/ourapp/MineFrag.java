package com.example.administrator.ourapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MineFrag extends Fragment implements IListener{

    private TextView name_tv;
    private ImageView user_image_iv;
    private RatingBar ratingBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("MineFrag",this);
        View view=inflater.inflate(R.layout.mine_frag, container, false);
        name_tv=(TextView)view.findViewById(R.id.mine_name_tv);
//        location_tv=(TextView)view.findViewById(R.id.mine_location_tv);
        user_image_iv=(ImageView)view.findViewById(R.id.mine_userimage_iv);
        ratingBar=(RatingBar)view.findViewById(R.id.ratingbar);
        initInfo();
        return view;
    }

    @Override
    public void upData() {
        initInfo();

    }

    private void initInfo()
    {
        if (BmobUser.getCurrentUser(MyUser.class)!=null){
        Bitmap bitmap=null;
        try
        {
            bitmap = BitmapFactory.decodeFile(MainActivity.getDiskFileDir(getContext())+"/user_image.png");
            user_image_iv.setImageBitmap(bitmap);
            Log.i("z","获取更新iv--"+MainActivity.getDiskFileDir(getContext())+"/user_image.png");
        } catch (Exception e)
        {
            // TODO: handleResult exception
        }

            name_tv.setText((String)BmobUser.getObjectByKey("name"));
            Log.i("naosumi",(String)BmobUser.getObjectByKey("name"));
            setRating();
        }
//        else
//        {
//            user_image_iv.setImageDrawable(getResources().getDrawable(R.drawable.portrait));
//        }
        else
        {
            user_image_iv.setImageDrawable(getResources().getDrawable(R.drawable.defaulticon));
            name_tv.setText("未知用户");
            ratingBar.setRating(new Float(0));
//            location_tv.setText("所在地未知");

        }
//        else
//        {
//            name_tv.setText((String)BmobUser.getObjectByKey("name"));
//            Log.i("naosumi",(String)BmobUser.getObjectByKey("name"));
//            if(BmobUser.getObjectByKey("location")!=null)
//            {
//                location_tv.setText((String)BmobUser.getObjectByKey("location"));
//            }

//        }
    }

    private void setRating()
    {
        BmobQuery<UserInfo> query=new BmobQuery<UserInfo>();
        MyUser user=new MyUser();
        user.setObjectId(BmobUser.getCurrentUser().getObjectId());
        query.addWhereEqualTo("user",user);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e==null)
                {
                    ratingBar.setRating(list.get(0).getRating().floatValue());
                }
                else
                {
                    Log.i("z","查询评分失败");
                    Toast.makeText(getContext(), "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
