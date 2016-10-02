package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.ourapp.mymissionadapter.MissionAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Frag_trans extends MyMissionFrag {
        private MissionAdapter mAdapter;
        private MissionAdapterCallBack callBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callBack=new MissionAdapterCallBack() {
            @Override
            public void setAdapter(ListView listView, List<Mission> list) {
                mAdapter = new MissionAdapter(getContext(), R.layout.mission_abstract, list);
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
        query.include("pub_user[name].userimage");
    }
}
