package com.example.administrator.ourapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.ourapp.user_information.MyAccount;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2016/12/12.
 */

public class NearbyWish extends AppCompatActivity{
    private MapView mMapView;
    private TextView title,return_bt;
    private WishPopupWindow wishPopupWindow;
    private BaiduMap mBaiduMap;
    private List<LatLng> latLngs=new ArrayList<LatLng>();
    private List<Wish> wishes=new ArrayList<Wish>();
    public LocationClient mLocationClient = null;
    private LatLng mylatlng;
    private int call=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化百度地图sdk
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.nearby_wish);
        initWidget();
        startLocation();
    }

    private void initWidget()
    {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        title=(TextView)findViewById(R.id.mission_title);
        title.setText("附近的心愿");
        return_bt=(TextView)findViewById(R.id.lbt);
        return_bt.setText("返回");
        return_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void createMarker(){
       //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.marker);
      //构建MarkerOption，用于在地图上添加Marker
        int i=0;
        for (LatLng latLng:latLngs) {
            MarkerOptions option = new MarkerOptions()
                    .position(latLng)
                    .icon(bitmap)
                    .animateType(MarkerOptions.MarkerAnimateType.drop)
                    .zIndex(9)
                    .draggable(false);
            //在地图上添加Marker，并显示
            Marker marker=(Marker)( mBaiduMap.addOverlay(option));
            marker.setTitle(""+i);
            i++;
        }


    }
    private void setMarkerListener()
    {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                call++;
                Log.i("naosumi",call+"");
                int i=Integer.parseInt(marker.getTitle());
                final Wish wish=wishes.get(i);
                new LoadImage().execute(wish.getWish_user().getUserimage().getUrl());
                    wishPopupWindow=new WishPopupWindow(NearbyWish.this, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()){
                                case R.id.person_image:
                                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                                    query.getObject(wish.getWish_user().getObjectId(), new QueryListener<MyUser>() {

                                        @Override
                                        public void done(MyUser object, BmobException e) {
                                            if(e==null){
                                                Intent intent=new Intent(NearbyWish.this,MyAccount.class);
                                                Bundle bundle=new Bundle();
                                                bundle.putSerializable("user", object);
                                                intent.putExtras(bundle);
                                                NearbyWish.this.startActivity(intent);
                                            }else{
                                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                Toast.makeText(NearbyWish.this, "失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    });
                                    break;
                                case R.id.contact:
                                    Intent intent=new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + wish.getWish_user().getMobilePhoneNumber()));
                                    Log.i("naosumi","tel:"+wish.getWish_user().getMobilePhoneNumber());
                                    //开启系统拨号器
                                    startActivity(intent);
                                    break;

                            }


                        }
                    });//new WishPopupWindow

                   wishPopupWindow.setName(wish.getWish_user().getName());
                   wishPopupWindow.setInfo(wish.getContent());
                   wishPopupWindow.setTime(wish.getTime());
                   LatLng latLng=new LatLng(wish.getLocation().getLatitude(),wish.getLocation().getLongitude());
                    double dis=DistanceUtil.getDistance(mylatlng, latLng);
                   wishPopupWindow.setDistance(new Double(dis).intValue()+"米");
                   wishPopupWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

                return false;
            }
        });
    }

    private void startLocation()
    {
        mLocationClient = new LocationClient(getApplicationContext());
        BDLocationListener myListener = new MyLocationListener();
        mLocationClient.registerLocationListener( myListener );
        setOption();
        mLocationClient.start();
    }

    private void createLocation(BDLocation location)
    {
        this.mylatlng=new LatLng(location.getLatitude(),location.getLongitude());
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.me3);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);
    }


    private void focusToMe(BDLocation location)
    {
       LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MapStatus mapStatus=new MapStatus.Builder()
                .target(latLng)
                .zoom(20)
                .build();
        MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.animateMapStatus(mapStatusUpdate,500);


    }

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

    private void findWish(BDLocation location)
    {
        BmobQuery<Wish> query=new BmobQuery<Wish>();
        query.include("wish_user[name|mobilePhoneNumber].userimage");
        query.addWhereWithinKilometers("location",new BmobGeoPoint(location.getLongitude(),location.getLatitude()),2);
        final Dialog dialog=MainActivity.createLoadingDialog(NearbyWish.this,"请稍后...");
        dialog.show();
        query.findObjects(new FindListener<Wish>() {
            @Override
            public void done(List<Wish> list, BmobException e) {
                dialog.dismiss();
                if (e==null) {
                    Log.i("naosumi","查询到"+list.size()+"条数据");
                    if (!list.isEmpty()) {
                        for (Wish wish : list) {
                            wishes.add(wish);
                            BmobGeoPoint point = wish.getLocation();
                            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                            latLngs.add(latLng);
                            createMarker();
                        }
                        setMarkerListener();
                    }
                    else
                    {
                        Toast.makeText(NearbyWish.this,"附近没有心愿",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(NearbyWish.this,"获取心愿失败",Toast.LENGTH_SHORT).show();
                    Log.i("naosumi",e.getMessage());
                }
            }
        });
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            boolean flag=false;

            if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                flag=true;
            }
            else if(location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                flag=true;
            }
            else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                flag=true;
            }
            else if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(NearbyWish.this,"服务端网络定位失败",Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(NearbyWish.this,"网络不同导致定位失败，请检查网络是否通畅",Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(NearbyWish.this,"无法获取有效定位依据导致定位失败",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(NearbyWish.this,"定位失败",Toast.LENGTH_SHORT).show();
            }

            if (flag)
            {
                mLocationClient.stop();
                createLocation(location);
                focusToMe(location);
                findWish(location);
            }


        }
    }

    //异步加载图片
    public class LoadImage extends AsyncTask<String,Void,Drawable>
    {
        @Override
        protected Drawable doInBackground(String... strs) {
            URL request;
            InputStream input;
            Drawable drawable = null;

            try {
                request =new URL(strs[0]);
                input=(InputStream)request.getContent();
                drawable = Drawable.createFromStream(input, "src");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return drawable;

        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            wishPopupWindow.setIcon(drawable);
        }
    }

}

