package com.example.administrator.ourapp.question_and_answer;

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

import com.example.administrator.ourapp.Login;
import com.example.administrator.ourapp.Mission;
import com.example.administrator.ourapp.MyUser;
import com.example.administrator.ourapp.ProgressFragment;
import com.example.administrator.ourapp.R;
import com.example.administrator.ourapp.RefreshLayout;
import com.example.administrator.ourapp.question_and_answer.Mission_question;
import com.example.administrator.ourapp.question_and_answer.QA_adapter;
import com.example.administrator.ourapp.question_and_answer.question_and_answer;
import com.example.administrator.ourapp.question_and_answer.question_and_answer_detail;
import com.example.administrator.ourapp.question_and_answer.question_and_answer_detail_publisher;

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

/**
 * Created by Administrator on 2016/10/9.
 */

public class QAFrag extends ProgressFragment {
    private ListView listView;
    private List<Mission_question> qa_list;
    private Mission mission;
    private Vector<String> question_date = new Vector<String>();       //问题的发布日期
    private Vector<String> question_content = new Vector<String>();    //问题的内容
    private Vector<String> answer_content = new Vector<String>(); //问题的回答
    private Vector<String> question_ID = new Vector<String>();//问题的ID
    private Vector<String> user_ID = new Vector<String>();//问题的提问者的ID
    private QA_adapter qa_adapter;

    private View mContentView;
    private String lastTime;
    private boolean mIsStart = true;
    private RefreshLayout refreshLayout;

    public QAFrag(Mission mission) {
        this.mission = mission;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContentView=inflater.inflate(R.layout.missionrefresh,container,false);
        listView=(ListView)mContentView.findViewById(R.id.mission_listview);
        listView.setDividerHeight(5);
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
        setEmptyText("还没有提问，快来创建第一个问题吧");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(BmobUser.getCurrentUser(MyUser.class) == null){
                    Intent intent=new Intent(getContext(),Login.class);
                    startActivity(intent);
                    return;
                }//if 未登录

                if(mission.getPub_user().getObjectId().equals(BmobUser.getCurrentUser(MyUser.class).getObjectId())){
                    //若是任务发布者则跳转到编辑回答界面
                    // 在Intent中传递数据
                    Intent intent = new Intent(getContext(), question_and_answer_detail_publisher.class);
//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("mission",mission);
//                    intent.putExtras(bundle);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("question",qa_list.get(i));
                    intent.putExtras(bundle);
                    // 启动Intent
                    startActivity(intent);
                }//if
                //若不是任务发布者则跳转到提问界面
                else{
                    Intent intent = new Intent(getContext(), question_and_answer_detail.class);
                    // 在Intent中传递数据
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("question",qa_list.get(i));
                    intent.putExtras(bundle);
                    // 启动Intent
                    startActivity(intent);
                }//else


            }
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
        final BmobQuery<Mission_question> query=new BmobQuery<Mission_question>();
        query.include("answer[content],User");
        query.addWhereEqualTo("mission",mission.getObjectId());
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
        query.findObjects(new FindListener<Mission_question>() {
            @Override
            public void done(List<Mission_question> list, BmobException e) {
                if (e==null)
                {
                    Log.i("z","--查询数据成功");
                    if (list.size()!=0) {
                        Log.i("z","数据不为空");

                        lastTime = list.get(list.size()-1).getCreatedAt();
                        qa_list = new ArrayList<Mission_question>(list);

                        qa_adapter = new QA_adapter(getContext(), R.layout.question_and_answer_item, qa_list);
                        listView.setAdapter(qa_adapter);

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
        BmobQuery<Mission_question> query=new BmobQuery<Mission_question>();
//        query.order("-createdAt");
//        for (Map.Entry<String,String> entry : condition.entrySet())
//        {
//            query.addWhereEqualTo(entry.getKey(),entry.getValue());
//        }
//        query.include(include);
        query.include("answer[content],User[objectId|userimage]");
        query.addWhereEqualTo("mission",mission.getObjectId());
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
        query.findObjects(new FindListener<Mission_question>() {
            @Override
            public void done(List<Mission_question> list, BmobException e) {
                if (e==null)
                {
                    if (list.size()>0)
                    {
                        Log.i("up","list.size>0");
                        if (mIsStart)
                        {
                            qa_list.clear();
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }
                        for (Mission_question question:list)
                        {
                            qa_list.add(question);
                            lastTime = list.get(list.size()-1).getCreatedAt();
                        }

                    }
                    else {
                        Log.i("up","list.size<0");

                    }
                    qa_adapter.notifyDataSetChanged();
                }

                refreshLayout.setRefreshing(false);

                refreshLayout.setLoading(false);
            }
        });
    }

    public void upDate()
    {
//        Log.i("z","send mes to qa");
        setContentShown(false);
        initMission();
    }


}
