package com.example.administrator.ourapp.MyLocationUtils;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.NotificationCompatSideChannelService;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.ourapp.MainActivity;

import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Administrator on 2016/12/14.
 */

public class MyLocation implements BDLocationListener{
    private LocationClient mLocationClient = null;
    private String locationInfo=null;//位置的语意信息
    private BmobGeoPoint myPoint=null;
    private Dialog dialog=null;//等待对话框
    //开始定位
    public void startLocation(Context context)
    {

        mLocationClient = new LocationClient(context);     //声明LocationClient类
        mLocationClient.registerLocationListener( MyLocation.this );    //注册监听函数
        setOption();
        dialog= MainActivity.createLoadingDialog(context,"定位中");
        dialog.show();
        mLocationClient.start();

    }

    //设置选项
    private void setOption(){
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
//        sb.append("\n维度: ");
//        sb.append(location.getLatitude());
//        sb.append("\n经度 : ");
//        sb.append(location.getLongitude());
        if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
           // sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            myPoint=new BmobGeoPoint(location.getLongitude(),location.getLatitude());
            //运营商信息
//            sb.append("\noperationers : ");
//            sb.append(location.getOperators());
//            sb.append("\ndescribe : ");
          //  sb.append("网络定位成功");
        }
        else if(location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
         //   sb.append("\naddr : ");
            sb.append(location.getAddrStr());
            myPoint=new BmobGeoPoint(location.getLongitude(),location.getLatitude());
          //  sb.append("\ndescribe : ");
           // sb.append("gps定位成功");
        }
        else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
         //   sb.append("\ndescribe : ");
            sb.append("离线定位成功，但无法获取地址");
            myPoint=new BmobGeoPoint(location.getLongitude(),location.getLatitude());
        }
        else if (location.getLocType() == BDLocation.TypeServerError) {
         //   sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
         //   sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
         //   sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败");
        }
      //  sb.append("\nlocationdescribe : ");
        sb.append("\n"+location.getLocationDescribe());// 位置语义化信息
        locationInfo=sb.toString();
        dialog.dismiss();


    }
        //获取自己的位置
    public BmobGeoPoint getMyPoint() {
        return myPoint;
    }

       //获取自己位置的语义信息
    public String getLocationInfo() {
        return locationInfo;
    }




}
