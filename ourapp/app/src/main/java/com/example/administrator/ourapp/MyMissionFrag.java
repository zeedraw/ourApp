package com.example.administrator.ourapp;

import android.icu.text.Normalizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.ourapp.pulltorefresh.PullToRefreshBase;

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
 * Created by Administrator on 2016/9/28.
 */
public class MyMissionFrag extends ProgressFragment {
    private View mContentView;
    private String lastTime;
    private List<Mission> mlist;
    protected ListView mlistview;
    private boolean mIsStart = true;
    private RefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContentView=inflater.inflate(R.layout.missionrefresh,container,false);
        mlistview=(ListView)mContentView.findViewById(R.id.mission_listview);
        refreshLayout=(RefreshLayout) mContentView.findViewById(R.id.mission_refreshlayout);
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        return inflater.inflate(R.layout.fragment_progress,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setContentShown(false);

    }

    protected void initMission(final MissionAdapterCallBack callBack)
    {
        final BmobQuery<Mission> query=new BmobQuery<Mission>();
        addCondition(query);
        Log.i("z","初始化添加条件成功");
//        for (Map.Entry<String,String> entry : condition.entrySet())
//        {
//            query.addWhereEqualTo(entry.getKey(),entry.getValue());
//        }
//        query.order("-createdAt");
//        query.include(include);
//        query.setLimit(limit);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
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
//                        mAdapter = new MissionAdapter(getContext(), R.layout.mission_abstract, mlist);
//                        mlistview.setAdapter(mAdapter);
                        callBack.setAdapter(mlistview,mlist);
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
                queryData(callBack);

            }
        });

        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mIsStart=false;
                queryData(callBack);
            }
        });


    }

    private void queryData(final MissionAdapterCallBack callBack)
    {
        BmobQuery<Mission> query=new BmobQuery<Mission>();
//        query.order("-createdAt");
//        for (Map.Entry<String,String> entry : condition.entrySet())
//        {
//            query.addWhereEqualTo(entry.getKey(),entry.getValue());
//        }
//        query.include(include);
        addCondition(query);
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

                    }
                    else {
                        Log.i("up","list.size<0");

                    }
                    callBack.notifyData();
                }

                refreshLayout.setRefreshing(false);

                refreshLayout.setLoading(false);
            }
        });
    }


    protected void addCondition(BmobQuery query)
    {

    }

    public interface MissionAdapterCallBack
    {
        public void setAdapter(ListView listView,List<Mission> list);
        public void notifyData();
    }
}
