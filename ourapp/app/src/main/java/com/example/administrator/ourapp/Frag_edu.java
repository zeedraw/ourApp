package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.example.administrator.ourapp.mymissionadapter.MissionAdapter;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;

/**
 * Created by Administrator on 2016/9/28.
 */
public class Frag_edu extends MyMissionFrag {
    private MissionAdapter mAdapter;
    private MissionAdapterCallBack callBack;
    private String cityLimit="全部市";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MissionAdapter missionAdapter = null;
                if (adapterView.getAdapter() instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) adapterView.getAdapter();
                    missionAdapter = (MissionAdapter) listAdapter.getWrappedAdapter();
                } else {
                    missionAdapter = (MissionAdapter) adapterView.getAdapter();
                }
                Mission mission = missionAdapter.getMission(i);
                Intent intent = new Intent(getActivity(), MissionInfo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mission", mission);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        callBack = new MissionAdapterCallBack() {
            @Override
            public void setAdapter(ListView listView, List<Mission> list) {
                mAdapter = new MissionAdapter(getContext(), R.layout.mission_item, list);
                listView.setAdapter(mAdapter);

            }

            @Override
            public void notifyData() {
                mAdapter.notifyDataSetChanged();
            }
        };
        Log.i("z", "创建回调类成功");

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

        query.addWhereEqualTo("tag","教育");
        query.addWhereEqualTo("state",new Integer(2));
        query.order("-createdAt");
        query.setLimit(5);
        query.include("pub_user[name|orgDescription].userimage");
        if (!cityLimit.equals("全部市"))
        {
            Log.i("z","显示特定城市"+cityLimit);
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


}
