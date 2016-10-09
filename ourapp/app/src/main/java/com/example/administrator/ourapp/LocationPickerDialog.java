package com.example.administrator.ourapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.administrator.ourapp.location.CityModel;
import com.example.administrator.ourapp.location.DistrictModel;
import com.example.administrator.ourapp.location.ProvinceModel;
import com.example.administrator.ourapp.location.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.example.administrator.ourapp.wheel.OnWheelChangedListener;
import com.example.administrator.ourapp.wheel.WheelView;
import com.example.administrator.ourapp.wheel.adapters.ArrayWheelAdapter;

/**
 * Created by Administrator on 2016/9/11.
 */
public class LocationPickerDialog implements OnWheelChangedListener {
    private LinearLayout rootView;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;//wheelview组件
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context mcontext;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener mlistener;
    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();


    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;

    /**
     * 当前市的名称
     */
    private String mCurrentCityName;

    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName ;


    public LocationPickerDialog(Context context, DialogInterface.OnClickListener listener)
    {

        mcontext=context;
        mlistener=listener;
        preferences=mcontext.getSharedPreferences("ourApp",mcontext.MODE_PRIVATE);
        editor=preferences.edit();
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        rootView=(LinearLayout)inflater.inflate(R.layout.locationwheel,null);

        builder=new AlertDialog.Builder(mcontext);
        builder.setTitle("请选择省市区");
        builder.setView(rootView).setCancelable(false).setPositiveButton("确定", mlistener).setNegativeButton("取消",null);

            setUpViews();
            setUpListener();
            setUpData();

    }

    public void show()
    {
        builder.create().show();
    }

        //初始化wheelview组件
    private void setUpViews() {
        mViewProvince = (WheelView)rootView.findViewById(R.id.id_province);
        mViewCity = (WheelView)rootView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView)rootView.findViewById(R.id.id_district);

    }

        //为wheelview组件设置监听器
    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);

    }
    //为wheelview组件初始化数据
    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(mcontext, mProvinceDatas));
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
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mcontext, areas));
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
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(mcontext, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
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

    private void initProvinceDatas()
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = mcontext.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public  String getLocation()
    {
        return mCurrentProviceName+"-"+mCurrentCityName +"-"+mCurrentDistrictName;
    }
}
