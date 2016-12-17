package com.example.administrator.ourapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.administrator.ourapp.wish.Wish;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2016/12/7.
 */

public class LocationTest extends AppCompatActivity implements BDLocationListener {
    private TextView locationDisplay;
    private Button start,stop,add;
    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationtest);
        locationDisplay=(TextView)findViewById(R.id.location_dis);
        start=(Button)findViewById(R.id.start_location);
        stop=(Button)findViewById(R.id.stop_location);
        add=(Button)findViewById(R.id.add_location);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( LocationTest.this );    //注册监听函数
        initLocation();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 mLocationClient.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationClient.stop();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LocationTest.this, "开始添加心愿", Toast.LENGTH_SHORT);
                Wish wish=new Wish();
                wish.setWish_user(BmobUser.getCurrentUser(MyUser.class));
                wish.setContent("我的小小心愿1");
                wish.setTime("12-16 11:39");
                BmobGeoPoint point=new BmobGeoPoint(116.357507,40.004887);
                wish.setLocation(point);
                wish.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null) {
                            Toast.makeText(LocationTest.this, "添加心愿1成功", Toast.LENGTH_SHORT);
                            Log.i("naosumi","1ok");
                        }
                        else
                        {
                            Toast.makeText(LocationTest.this,"添加心愿1失败",Toast.LENGTH_SHORT);
                            Log.i("naosumi","1no   "+e.getMessage());
                        }
                    }
                });

                Wish wish1=new Wish();
                wish1.setWish_user(BmobUser.getCurrentUser(MyUser.class));
                wish1.setContent("我的小小心愿2");
                wish1.setTime("12-16 11:42");
                BmobGeoPoint point1=new BmobGeoPoint(116.357512,40.004879);
                wish.setLocation(point1);
                wish.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e==null) {
                            Toast.makeText(LocationTest.this, "添加心愿2成功", Toast.LENGTH_SHORT);
                            Log.i("naosumi","2ok");
                        }
                        else
                        {
                            Toast.makeText(LocationTest.this,"添加心愿2失败",Toast.LENGTH_SHORT);
                            Log.i("naosumi","2no  "+e.getMessage());
                        }
                        }
                });
            }
        });


    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }



        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive MyLocation
            StringBuffer sb = new StringBuffer(256);
            sb.append("当前定位时间: ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\n维度: ");
            sb.append(location.getLatitude());
            sb.append("\n经度 : ");
            sb.append(location.getLongitude());
            sb.append("\n定位精度 : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\n获取速度 : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\n获取gps锁定用的卫星数: ");
                sb.append(location.getSatelliteNumber());
                sb.append("\n海拔 : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            locationDisplay.setText(sb.toString());
        }

}
