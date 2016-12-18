package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.ourapp.mymissionadapter.MissionAdapter;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Frag_trans extends MyMissionFrag {
        private MissionAdapter mAdapter;
        private MissionAdapterCallBack callBack;
        private String cityLimit="全部市";
        private View headView;

    public static final Frag_trans newInstance(View view)
    {
        Frag_trans trans=new Frag_trans();
        trans.setHeaderView(view);

        return trans;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      //  mlistview.addHeaderView(headView);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),MissionInfo.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("mission",mlist.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        callBack=new MissionAdapterCallBack() {
            @Override
            public void setAdapter(ListView listView, List<Mission> list) {
                mAdapter = new MissionAdapter(getContext(),R.layout.mission_item, list);
                listView.setAdapter(mAdapter);

            }

            @Override
            public void notifyData() {
                mAdapter.notifyDataSetChanged();
            }
        };
        setmEmptyListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentShown(false);
                initMission(callBack);
            }
        });
        initMission(callBack);
    }
    @Override
    protected void addCondition(BmobQuery query) {
        query.addWhereEqualTo("tag","交通");
        query.addWhereEqualTo("state",new Integer(2));
        query.order("-createdAt");
        query.setLimit(7);
        query.include("pub_user[name|orgDescription].userimage");
        if (!cityLimit.equals("全部市"))
        {
            query.addWhereContainsAll("location_abs", Arrays.asList(new String[]{cityLimit}));
        }
    }

    public void setCityLimit(String str)
    {
        cityLimit=str;
    }

    public void initListView()
    {
        setContentShown(false);
        initMission(callBack);
    }

    public void setHeaderView(View view)
    {
        headView=view;
    }
}
