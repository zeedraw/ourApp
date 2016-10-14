package com.example.administrator.ourapp.mymissionadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.administrator.ourapp.R;

/**
 * Created by Administrator on 2016/10/11.
 */

public class TestAdpter extends BaseAdapter {
    private Context mContext;
    public TestAdpter(Context context) {
        mContext=context;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.ratinguser_item,null);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
}
