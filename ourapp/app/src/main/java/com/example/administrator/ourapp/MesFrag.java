package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MesFrag extends Fragment implements IListener{
//    private ListView listView;
//    private BaseAdapter baseAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListenerManager.getInstance().registerListtener("MesFrag",this);
        View rootview=inflater.inflate(R.layout.fra1, container, false);


        return rootview;
    }

    @Override
    public void upData() {

    }
}
