package com.example.administrator.ourapp.wish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ourapp.CheckMission;
import com.example.administrator.ourapp.IListener;
import com.example.administrator.ourapp.ListenerManager;
import com.example.administrator.ourapp.Login;
import com.example.administrator.ourapp.MainActivity;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MissionInfo;
import com.example.administrator.ourapp.MissionPub;
import com.example.administrator.ourapp.MyTask;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.ProgressFragment;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.RefreshLayout;
import com.example.administrator.ourapp.UserInfo;
import com.example.administrator.ourapp.agency_authentication_reply;
import com.example.administrator.ourapp.friends.confirm_friend;
import com.example.administrator.ourapp.friends.friend_application;
import com.example.administrator.ourapp.message.Message;
import com.example.administrator.ourapp.message.Message_Adapter;
import com.example.administrator.ourapp.mission_audit_reply;
import com.example.administrator.ourapp.question_and_answer.Mission_question;
import com.example.administrator.ourapp.question_and_answer.question_and_answer_detail;
import com.example.administrator.ourapp.question_and_answer.question_and_answer_detail_publisher;
import com.example.administrator.ourapp.real_name_authentication_reply;
import com.example.administrator.ourapp.reject_authentication_reason;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Longze on 2016/12/8.
 */
public class WishFrag extends ProgressFragment implements IListener {
    private ListView listView;
    private wish_adapter wish_adapter;
    private List<Wish> wishList;
    private Vector<String> wish_content = new Vector<String>();       //心愿的内容
    private Vector<String> wish_title = new Vector<String>();       //心愿的标题
    private Vector<Integer> wish_type = new Vector<Integer>();    //心愿类型
    private Vector<String> wisher_ID = new Vector<String>();//许愿者的objectId
    private Vector<String> wish_date = new Vector<String>();//心愿发送的时间
    private Vector<String> wish_ID = new Vector<String>();

    private View mContentView;
    private String lastTime;
    private boolean mIsStart = true;
    private RefreshLayout refreshLayout;

//    public WishFragh(Wish wish) {
//        this.wish = wish;
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("WishFrag",this);

        mContentView=inflater.inflate(R.layout.wish_listview,container,false);
        listView=(ListView)mContentView.findViewById(R.id.wish_listview);
        refreshLayout=(RefreshLayout) mContentView.findViewById(R.id.wish_refreshlayout);
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));



        return inflater.inflate(R.layout.fragment_progress,container,false);
    }//onCreateView



    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setContentShown(false);
        setEmptyText("暂无心愿，去看看别的吧");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO 点击后跳转到心愿详情
                if(BmobUser.getCurrentUser(MyUser.class) == null){
                    Intent intent=new Intent(getContext(),Login.class);
                    startActivity(intent);
                    return;
                }//if 未登录

//                if(wishList.get(i).getWish_user().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
//                    //若是心愿发布者则跳转到有完成按钮的页面
//                    // 在Intent中传递数据
//                    Intent intent = new Intent(getContext(), question_and_answer_detail_publisher.class);
////                    Bundle bundle=new Bundle();
////                    bundle.putSerializable("mission",mission);
////                    intent.putExtras(bundle);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("wish",wishList.get(i));
//                    intent.putExtras(bundle);
//                    // 启动Intent
//                    startActivity(intent);
//                }//if
                //若不是任务发布者则跳转到提问界面
//                else{
                    Intent intent = new Intent(getContext(), wish_detail.class);
                    // 在Intent中传递数据
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("wish",wishList.get(i));
                    intent.putExtras(bundle);
                    // 启动Intent
                    startActivity(intent);
//                }//else
            }//onItemClick
        });

        setmEmptyListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentShown(false);
                initMission();
            }
        });
        initMission();

    }



    protected void initMission()
    {
        final BmobQuery<Wish> query=new BmobQuery<Wish>();
        query.addWhereEqualTo("type", 1);
        query.addWhereEqualTo("audit_pass", true);
        query.addWhereEqualTo("is_finished", false);
        query.include("wish_user, organization, mission");
        query.order("-createdAt");
        query.setLimit(9);
        Log.i("z","初始化添加条件成功");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Wish>() {
            @Override
            public void done(List<Wish> list, BmobException e) {
                if (e==null)
                {
                    Log.i("z","--查询数据成功");
                    if (list.size()!=0) {
                        Log.i("z","数据不为空");

                        lastTime = list.get(list.size()-1).getCreatedAt();
                        wishList = new ArrayList<Wish>(list);
                        wish_adapter = new wish_adapter(getContext(),
                                R.layout.wish_item, wishList);
                        listView.setAdapter(wish_adapter);
                        setContentShown(true);
                        setContentEmpty(false);


                    }
                    else
                    {
                        Log.i("z","数据为空");
                        setContentShown(true);
                        setContentEmpty(true);
                    }
                }
                else
                {
                    setContentShown(true);
                    setContentEmpty(true);
                    Log.i("z","查询数据失败"+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsStart=true;
                queryData();
//                ((MainActivity)getContext()).checkNewMes();

            }
        });

        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mIsStart=false;
                queryData();
            }
        });
    }

    private void queryData()
    {
        BmobQuery<Wish> query=new BmobQuery<Wish>();
        query.addWhereEqualTo("type", 1);
        query.addWhereEqualTo("audit_pass", true);
        query.addWhereEqualTo("is_finished", false);
        query.include("wish_user, organization, mission");
//        query.addWhereEqualTo("receiver", BmobUser.getCurrentUser(MyUser.class).getObjectId());
        query.order("-createdAt");
        query.setLimit(9);
        Log.i("z","初始化添加条件成功");
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        if (mIsStart)//true 下拉刷新
        {
            query.setSkip(0);
        }
        else // 上拉加载
        {
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThan("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            //query.setSkip(1);
            Log.i("up","开始设置条件"+"skip"+1 +"lastTime"+lastTime);
        }
        query.findObjects(new FindListener<Wish>() {
            @Override
            public void done(List<Wish> list, BmobException e) {
                if (e==null)
                {
                    if (list.size()>0)
                    {
                        Log.i("up","list.size>0");
                        if (mIsStart)
                        {
                            wishList.clear();
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }
                        for (Wish wish:list)
                        {
                            wishList.add(wish);
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }

                    }
                    else {
                        Log.i("up","list.size<0");

                    }
                    wish_adapter.notifyDataSetChanged();
                }

                refreshLayout.setRefreshing(false);

                refreshLayout.setLoading(false);
            }
        });
    }

    @Override
    public void upData() {
        ((MainActivity)getContext()).checkNewMes();
    }

    public void hasNesMesRefresh()
    {

        setContentShown(false);
        initMission();
    }

}

