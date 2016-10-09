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

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/10/6.
 */

public class DoComFrag extends MyMissionFrag {
    private CheckPeopleMissionAdapter mAdapter;
    private MissionAdapterCallBack callBack;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                mAdapter = new CheckPeopleMissionAdapter(getContext(), R.layout.missionitem2, list);
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

        query.addWhereContains("get_user", BmobUser.getCurrentUser(MyUser.class).getObjectId());
        query.addWhereEqualTo("state",new Integer(4));
        query.order("-createdAt");
        query.setLimit(7);
        query.include("pub_user[name|orgDescription].userimage");
    }
}

