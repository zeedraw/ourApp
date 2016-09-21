package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ourapp.pulltorefresh.PullToRefreshBase;
import com.example.administrator.ourapp.pulltorefresh.PullToRefreshListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/9/20.
 */
public class MyMissionFrag extends ProgressFragment {
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    protected PullToRefreshListView mPullListView;
    private List<Mission> mlist;
    private MissionAdapter mAdapter;
    protected ListView mlistview;
    private String lastTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        mPullListView = new PullToRefreshListView(getContext());
        mPullListView.setPullLoadEnabled(false);
        mPullListView.setScrollLoadEnabled(true);

        return inflater.inflate(R.layout.fragment_progress,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        setContentView(mPullListView);
        setContentShown(false);
        mlistview = mPullListView.getRefreshableView();
//        initMission("tag","教育","pub_user[name].userimage",7);
//
//        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                mIsStart=true;
//                queryData("tag","教育","pub_user[name].userimage",7);
//
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                mIsStart=false;
//                queryData("tag","教育","pub_user[name].userimage",7);
//            }
//        });

    }

    protected void initMission(final String condition, final String mes, final String include, final int limit)
    {
        BmobQuery<Mission> query=new BmobQuery<Mission>();
        query.addWhereEqualTo(condition,mes);
        query.order("-createdAt");
        query.include(include);
        query.setLimit(limit);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Mission>() {
            @Override
            public void done(List<Mission> list, BmobException e) {
                if (e==null)
                {
                    Log.i("z","--查询数据成功");
                    if (list.size()!=0) {
                        Log.i("z","数据不为空");
                        lastTime = list.get(list.size()-1).getCreatedAt();
                        mlist = new ArrayList<Mission>(list);
                        mAdapter = new MissionAdapter(getContext(), R.layout.mission_abstract, mlist);
                        mlistview.setAdapter(mAdapter);

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
                    Log.i("z","查询数据失败");
                }
            }
        });

                mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart=true;
                queryData(condition,mes,include,limit);


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart=false;
                Log.i("up","开始load");
                queryData(condition,mes,include,limit);

            }
        });
    }

    private void queryData(String condition,String mes,String include,int limit)
    {
        BmobQuery<Mission> query=new BmobQuery<Mission>();
        query.order("-createdAt");
        query.addWhereEqualTo(condition,mes);
        query.include(include);
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
        query.setLimit(limit);
        query.findObjects(new FindListener<Mission>() {
            @Override
            public void done(List<Mission> list, BmobException e) {
                if (e==null)
                {
                    if (list.size()>0)
                    {
                        Log.i("up","list.size>0");
                        if (mIsStart)
                        {
                            mlist.clear();
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }
                        for (Mission mission:list)
                        {
                            mlist.add(mission);
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }

                        mPullListView.setHasMoreData(true);
                    }
                    else {
                        Log.i("up","list.size<0");
                        mPullListView.setHasMoreData(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    mPullListView.onPullDownRefreshComplete();
                    mPullListView.onPullUpRefreshComplete();
                    setLastUpdateTime();

                }
            }
        });
    }

    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }






}
