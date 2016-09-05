package com.example.administrator.ourapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.ourapp.location.LocationTest;

import wheel.widget.OnWheelChangedListener;
import wheel.widget.WheelView;
import wheel.widget.adapters.ArrayWheelAdapter;

/**
 * Created by Administrator on 2016/9/5.
 */
public class SignUp extends LocationTest implements OnWheelChangedListener,View.OnClickListener {
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    private Button mBtnConfirm,location_setting;
    private TextView location_tv;
    private AlertDialog.Builder builder;
    private LinearLayout root;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        location_tv=(TextView)findViewById(R.id.location_tv);
        location_setting=(Button)findViewById(R.id.location_setting);
        location_setting.setOnClickListener(this);

        preferences=getSharedPreferences("ourApp",MODE_PRIVATE);
        editor=preferences.edit();
    }




    private void setUpViews() {
        mViewProvince = (WheelView)root.findViewById(R.id.id_province);
        mViewCity = (WheelView)root.findViewById(R.id.id_city);
        mViewDistrict = (WheelView)root.findViewById(R.id.id_district);
        mBtnConfirm = (Button)root.findViewById(R.id.btn_confirm);

    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mBtnConfirm.setOnClickListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(SignUp.this, mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
        mViewProvince.setCurrentItem(preferences.getInt("province",0));
        mViewCity.setCurrentItem(preferences.getInt("city",0));
        mViewDistrict.setCurrentItem(preferences.getInt("district",0));
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
            editor.putInt("province",mViewProvince.getCurrentItem());
        } else if (wheel == mViewCity) {
            updateAreas();
            editor.putInt("city",mViewCity.getCurrentItem());
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            editor.putInt("district",mViewDistrict.getCurrentItem());
        }
        editor.commit();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[] { "" };
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
       int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[] { "" };
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View view) {
        if(view==mBtnConfirm)
        {
            location_tv.setText(
                    mCurrentProviceName+"-"+mCurrentCityName
                    +"-"+mCurrentDistrictName);

            builder.create().dismiss();
        }
        if (view==location_setting)
        {
            root=(LinearLayout)getLayoutInflater().inflate(R.layout.locationwheel,null);

            builder=new AlertDialog.Builder(SignUp.this);

            builder.setView(root).setCancelable(false).setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
            builder.create().show();

            setUpViews();
            setUpListener();
            setUpData();
        }
    }
}
