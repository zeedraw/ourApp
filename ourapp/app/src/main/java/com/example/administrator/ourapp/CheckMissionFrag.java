package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.ourapp.mymissionadapter.CheckPeopleMissionAdapter;
import com.example.administrator.ourapp.question_and_answer.Mission_question;
import com.example.administrator.ourapp.question_and_answer.QA_adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/10/15.
 */

public class CheckMissionFrag extends ProgressFragment {
    private ListView listView;
    private MissionHistoryAdapter mAdapter;
    List<Mission> missionlist;
    List<String> commentlist;

    private View mContentView;
    private String lastTime;
    private boolean mIsStart = true;
    private RefreshLayout refreshLayout;
    private int flag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView=inflater.inflate(R.layout.missionrefresh,container,false);
        listView=(ListView)mContentView.findViewById(R.id.mission_listview);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),MissionInfo.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("mission",missionlist.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setContentShown(false);

        setmEmptyListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentShown(false);
                initMission();
            }
        });
        initMission();

    }

    private void initMission()
    {
        BmobQuery<Mission> query=new BmobQuery<Mission>();
        query.include("pub_user.userimage");
        List<String> list=new ArrayList<String>();
        list.add(BmobUser.getCurrentUser(MyUser.class).getObjectId());
        query.addWhereContainsAll("get_user",list);
        query.addWhereEqualTo("state",new Integer(4));
        query.order("-createdAt");
        query.setLimit(7);
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
            public void done(final List<Mission> list1, BmobException e) {
                if (e==null)
                {
                    Log.i("z","--查询数据成功");
                    if (list1.size()!=0) {
                        Log.i("z","数据不为空");

                        lastTime = list1.get(list1.size()-1).getCreatedAt();
                        missionlist=new ArrayList<Mission>();
                        commentlist=new ArrayList<String>();
                         flag=list1.size();
                        Log.i("z","共有"+flag+"条评论i");
                        for (int i=0;i<list1.size();i++)
                        {
                            final int positon=i;
                            MyUser user=BmobUser.getCurrentUser(MyUser.class);
                            BmobQuery<Comment> commentBmobQuery=new BmobQuery<Comment>();
                            commentBmobQuery.addWhereEqualTo("user",user);
                            commentBmobQuery.addWhereEqualTo("mission",list1.get(i));
                            commentBmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                            commentBmobQuery.findObjects(new FindListener<Comment>() {
                                @Override
                                public void done(List<Comment> list2, BmobException e) {
                                    if (e==null)
                                    {
                                        Comment comment=list2.get(0);
                                        if (comment.getComment().length()!=0)
                                        {
                                            missionlist.add(list1.get(positon));
                                            commentlist.add(comment.getComment());
                                            Log.i("z","添加数据"+positon+"-"+comment.getComment());
                                        }
                                    }
                                    else
                                    {
                                        Log.i("z","查找对应comment失败");
                                        Toast.makeText(getContext(), "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    flag--;
                                    if (flag==0)
                                    {
                                        setAdapter(missionlist,commentlist);
                                    }


                                }
                            });

                        }//for

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
                    Toast.makeText(getContext(), "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        refreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                mIsStart=false;
                queryData();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void queryData()
    {
        BmobQuery<Mission> query=new BmobQuery<Mission>();
        query.include("pub_user.userimage");
        List<String> list=new ArrayList<String>();
        list.add(BmobUser.getCurrentUser(MyUser.class).getObjectId());
        query.addWhereContainsAll("cur_people",list);
        query.addWhereEqualTo("state",new Integer(4));
        query.order("-createdAt");
        query.setLimit(7);
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
            public void done(final List<Mission> list1, BmobException e) {
                if (e==null)
                {
                    if (list1.size()>0)
                    {
                        Log.i("up","list.size>0");
                        flag=list1.size();
                        Log.i("z","共有"+flag+"条评论i");
                        for (int i=0;i<list1.size();i++)
                        {
                            final int positon=i;
                            MyUser user=BmobUser.getCurrentUser(MyUser.class);
                            BmobQuery<Comment> commentBmobQuery=new BmobQuery<Comment>();
                            commentBmobQuery.addWhereEqualTo("user",user);
                            commentBmobQuery.addWhereEqualTo("mission",list1.get(i));
                            commentBmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                            commentBmobQuery.findObjects(new FindListener<Comment>() {
                                @Override
                                public void done(List<Comment> list2, BmobException e) {
                                    if (e==null)
                                    {
                                        Comment comment=list2.get(0);
                                        if (comment.getComment().length()!=0)
                                        {
                                            missionlist.add(list1.get(positon));
                                            commentlist.add(comment.getComment());
                                            Log.i("z","添加数据"+positon+"-"+comment.getComment());
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.i("z","查找对应comment失败");
                                    }
                                    flag--;
                                    if (flag==0)
                                    {
                                        mAdapter.notifyDataSetChanged();
                                        refreshLayout.setRefreshing(false);
                                        refreshLayout.setLoading(false);
                                    }


                                }
                            });

                        }//for

                        lastTime = list1.get(list1.size()-1).getCreatedAt();

                    }
                    else {
                        Log.i("up","list.size<0");
                        refreshLayout.setRefreshing(false);
                        refreshLayout.setLoading(false);

                    }

                }

            }
        });
    }

    private void setAdapter(List<Mission> missions,List<String> comments)
    {
        mAdapter = new MissionHistoryAdapter(getContext(), R.layout.history_mission_item,missions,comments);
        listView.setAdapter(mAdapter);
        setContentShown(true);
        setContentEmpty(false);
    }
}
