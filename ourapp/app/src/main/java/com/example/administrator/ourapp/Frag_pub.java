package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class Frag_pub extends Fragment {
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private View rootView;
    private SlidingTabLayout slidingTabLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<Fragment>();
        fragments.add(new Frg_task_reviewing());
        fragments.add(new Frg_task_applying());
        fragments.add(new Frg_task_ing());
        fragments.add(new Frg_task_completed());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.my_pub_mission,container,false);
        viewPager=(ViewPager)rootView.findViewById(R.id.mypub_vpager);
        slidingTabLayout=(SlidingTabLayout)rootView.findViewById(R.id.mypub_tab);

        Mission_Pub_FragAdapter adapter = new Mission_Pub_FragAdapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        slidingTabLayout.setCustomTabView(R.layout.tab,0);
        slidingTabLayout.setViewPager(viewPager);
        return rootView;
    }

    public Fragment getFragment(int i)
    {
        return fragments.get(i);
    }
}
