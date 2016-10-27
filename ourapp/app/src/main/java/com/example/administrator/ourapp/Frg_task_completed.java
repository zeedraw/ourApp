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
import com.example.administrator.ourapp.mymissionadapter.PubMissionComAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * Created by dell-pc on 2016/8/21.
 */
public class Frg_task_completed extends MyMissionFrag {
    private PubMissionComAdapter mAdapter;
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
                mAdapter = new PubMissionComAdapter(getContext(), R.layout.pubmission_complete_abstract, list);
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
        MyUser user=new MyUser();
        user.setObjectId((String) BmobUser.getObjectByKey("objectId"));
        query.addWhereEqualTo("pub_user",user);
        query.addWhereEqualTo("state",new Integer(4));
        query.order("-createdAt");
        query.setLimit(7);
        query.include("pub_user[orgDescription|name].userimage");
    }
}
