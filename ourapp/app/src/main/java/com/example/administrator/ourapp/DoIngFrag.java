package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.ourapp.mymissionadapter.CheckPeopleMissionAdapter;
import com.example.administrator.ourapp.mymissionadapter.MissionAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/10/6.
 */

public class DoIngFrag extends MyMissionFrag {

    private CheckPeopleMissionAdapter mAdapter;
    private MissionAdapterCallBack callBack;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("暂无任务，点我重新加载");

        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mission mission = mAdapter.getMission(i);
                Intent intent = new Intent(getActivity(), MissionInfo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mission", mission);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        callBack=new MissionAdapterCallBack() {
            @Override
            public void setAdapter(ListView listView, List<Mission> list) {
                mAdapter = new CheckPeopleMissionAdapter(getContext(), R.layout.domission_ing_abstract, list,"publisher");
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

        List<String> list=new ArrayList<String>();
        list.add(BmobUser.getCurrentUser(MyUser.class).getObjectId());
        query.addWhereContainsAll("get_user",list);
        query.addWhereEqualTo("state",new Integer(3));
        query.order("-createdAt");
        query.setLimit(7);
        query.include("pub_user[name|orgDescription].userimage");

    }
}
