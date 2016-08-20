package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/19.
 */
public class Frag_edu extends Fragment {
    private BaseAdapter adapter;
    private ListView listView;
    private TextView textView;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        adapter=new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return 14;//测试个数
//            }
//
//            @Override
//            public Object getItem(int i) {
//                return null;
//            }
//
//            @Override
//            public long getItemId(int i) {
//                return i;
//            }
//
//            @Override
//            public View getView(int i, View view, ViewGroup viewGroup) {
//
//
//            }
    //};
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
      //  final View re=inflater.inflate(R.layout.mission_abstract,container,false);
        View rootView=inflater.inflate(R.layout.frag_edu,container,false);
        listView=(ListView)rootView.findViewById(R.id.listview_edu);
        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return 14;//测试个数
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                view=LayoutInflater.from(getContext()).inflate(R.layout.mission_abstract,null);
                return view;//要添加的组件
            }
        };

        listView.setAdapter(adapter);
        return rootView;

    }
}
