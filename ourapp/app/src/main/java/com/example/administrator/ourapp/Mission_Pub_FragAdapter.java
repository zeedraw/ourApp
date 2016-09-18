package com.example.administrator.ourapp;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class Mission_Pub_FragAdapter extends FragmentPagerAdapter {

    private List<android.support.v4.app.Fragment> mFragments;
    private String mTabTitle[] = new String[]{"审核中","申请中","进行中","已完成"};

    public Mission_Pub_FragAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return  mTabTitle[position];
    }
}
