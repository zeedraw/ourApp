package com.example.administrator.ourapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class MainFragAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String mTabTitle[] = new String[]{"教育","活动","交通","景点"};
    public MainFragAdapter(FragmentManager fm, List<Fragment> fragments) {
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
