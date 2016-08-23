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
public class MesFrag extends Fragment{
//    private ListView listView;
//    private BaseAdapter baseAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fra1, container, false);
//        listView=(ListView)rootview.findViewById(R.id.mes_listview);
//        baseAdapter=new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return 14;
//            }
//
//            @Override
//            public Object getItem(int i) {
//                return null;
//            }
//
//            @Override
//            public long getItemId(int i) {
//                return 0;
//            }
//
//            @Override
//            public View getView(int i, View view, ViewGroup viewGroup) {
//                view=LayoutInflater.from(getContext()).inflate(R.layout.friend_lv_item,null);
//                return view;
//            }
//        };
//        listView.setAdapter(baseAdapter);

        return rootview;
    }
}
