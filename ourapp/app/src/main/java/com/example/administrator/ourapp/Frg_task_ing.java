package com.example.administrator.ourapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.ourapp.mymissionadapter.PubMissionIngAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dell-pc on 2016/8/21.
 */
public class Frg_task_ing extends MyMissionFrag {
    private PubMissionIngAdapter mAdapter;
    private MissionAdapterCallBack callBack;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("暂无任务，点我重新加载");
        callBack=new MissionAdapterCallBack() {
            @Override
            public void setAdapter(ListView listView, List<Mission> list) {
                mAdapter = new PubMissionIngAdapter(getContext(), R.layout.pubmission_ing_abstract, list);
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
        query.addWhereEqualTo("state",new Integer(3));
        query.order("-createdAt");
        query.setLimit(7);
        query.include("pub_user[orgDescription]");
    }

    public void handleResult(Intent data)
     {
         final int position=data.getIntExtra("position",-1);
            Log.i("z","成功接收返回结果"+position);
            if (position!=-1)
            {
                Mission missionPointer=new Mission();
                        missionPointer.setObjectId(mlist.get(position).getObjectId());
                        missionPointer.setState(new Integer(4));
                        missionPointer.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null)
                                {
                                    Log.i("z","完成任务"+position);
                                    mlist.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                }

                            }
                        });
            }

    }
}
