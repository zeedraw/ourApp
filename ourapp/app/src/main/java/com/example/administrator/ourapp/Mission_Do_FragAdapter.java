package com.example.administrator.ourapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Mission_Do_FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String mTabTitle[] = new String[]{"申请中","进行中","已完成"};

    public Mission_Do_FragAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments=fragments;
    }

    @Override
    public Fragment getItem(int arg0) {

        return mFragments.get(arg0);
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
