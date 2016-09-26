package com.example.administrator.ourapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class Frag_do extends Fragment {
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private View rootView;
    private SlidingTabLayout slidingTabLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment1());
        fragments.add(new Fragment1());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.my_do_mission,container,false);
        viewPager=(ViewPager)rootView.findViewById(R.id.mydo_vpager);
        slidingTabLayout=(SlidingTabLayout)rootView.findViewById(R.id.mydo_tab);

        Mission_Do_FragAdapter adapter = new Mission_Do_FragAdapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        slidingTabLayout.setCustomTabView(R.layout.tab,0);
        slidingTabLayout.setViewPager(viewPager);
        return rootView;
    }
}
