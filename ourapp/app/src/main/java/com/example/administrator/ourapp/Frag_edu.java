package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
 * Created by Administrator on 2016/8/19.
 */
public class Frag_edu extends ProgressFragment {
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    private PullToRefreshListView mPullListView;
    private View mContentView;
    private List<Mission> mlist;
    private MissionAdapter mAdapter;
    private ListView mlistview;
    private int limit=7;//每页的长度
    private int curPage=0;//当前页
    private String lastTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        mPullListView = new PullToRefreshListView(getContext());
        mPullListView.setPullLoadEnabled(false);
        mPullListView.setScrollLoadEnabled(true);


        // mContentView=inflater.inflate(R.layout.frag_edu,container,false);
        //mlistview=(ListView)mContentView.findViewById(R.id.listview_edu);
        return inflater.inflate(R.layout.fragment_progress,container,false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        setContentView(mPullListView);
        setEmptyText("no data");
        setContentShown(false);
            BmobQuery<Mission> query=new BmobQuery<Mission>();
            query.addWhereEqualTo("tag","教育");
            query.order("-createdAt");
            query.include("pub_user[name].userimage");
            query.setLimit(limit);
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            query.findObjects(new FindListener<Mission>() {
                @Override
                public void done(List<Mission> list, BmobException e) {
                    if (e==null)
                    {
                        Log.i("z","查询数据成功");
                        if (list.size()!=0) {
                            curPage++;
                            lastTime = list.get(list.size()-1).getCreatedAt();
                            mlist = new ArrayList<Mission>(list);
                            mlistview = mPullListView.getRefreshableView();
                            mAdapter = new MissionAdapter(getContext(), R.layout.mission_abstract, mlist);
                            mlistview.setAdapter(mAdapter);
                            setContentShown(true);
                        }
                        else
                        {
                            setContentEmpty(true);
                        }
                    }
                    else
                    {
                        Log.i("z","查询数据失败");
                    }
                }
            });

        mPullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                    mIsStart=true;
                        queryData();


            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                    mIsStart=false;
                        queryData();
            }
        });

    }

    private void queryData()
    {
        BmobQuery<Mission> query=new BmobQuery<Mission>();
        query.order("-createdAt");
        query.addWhereEqualTo("tag","教育");
        query.include("pub_user[name].userimage");
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
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(curPage * limit+1);
        }
        query.setLimit(limit);
        query.findObjects(new FindListener<Mission>() {
            @Override
            public void done(List<Mission> list, BmobException e) {
                if (e==null)
                {
                    if (list.size()>0)
                    {
                        if (mIsStart)
                        {
                            curPage=0;
                            mlist.clear();
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }
                        for (Mission mission:list)
                        {
                            mlist.add(mission);
                        }
                        curPage++;
                        mPullListView.setHasMoreData(true);
                    }
                    else {
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
//    private class MyAsyncTask extends AsyncTask<Void,Void,Void>
//    {
//        @Override
//        protected Void doInBackground(Void... voids) {
//            BmobQuery<Mission> query=new BmobQuery<Mission>();
//            query.addWhereEqualTo("tag","教育");
//            query.include("pub_user[name|userimage]");
//            query.setLimit(7);
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
//            query.findObjects(new FindListener<Mission>() {
//                @Override
//                public void done(List<Mission> list, BmobException e) {
//                    if (e==null)
//                    {
//                        Log.i("z","查询数据成功");
//                        mlist=new ArrayList<Mission>(list);
//                    }
//                    else
//                    {
//                        Log.i("z","查询数据失败");
//                    }
//                }
//            });
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            mAdapter=new MissionAdapter(getContext(),R.id.mission_abs,mlist);
//            mlistview.setAdapter(mAdapter);
//            setContentShown(true);
//            super.onPostExecute(aVoid);
//
//        }
//    }
}
