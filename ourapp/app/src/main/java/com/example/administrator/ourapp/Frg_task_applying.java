package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.ourapp.mymissionadapter.PubMissionApplyAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;

/**
 * Created by dell-pc on 2016/8/21.
 */
public class Frg_task_applying extends MyMissionFrag {
        private PubMissionApplyAdapter mAdapter;
        private MissionAdapterCallBack callBack;
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
                mAdapter = new PubMissionApplyAdapter(getContext(), R.layout.pubmission_apply_abstract, list);
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
        query.addWhereEqualTo("state",new Integer(2));
        query.order("-createdAt");
        query.setLimit(7);
        query.include("pub_user[orgDescription]");
    }
}
