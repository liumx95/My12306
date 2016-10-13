package com.neusoft.my12603;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


/**
 * Created by star on 2016/10/11.
 */
public class BaiduMapActivity extends Activity{
    MapView mMapView = null;
    BDLocation location=SplashActivity.Bdlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.baidumap);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 开启定位图层
        BaiduMap mBaiduMap = mMapView.getMap();
        float maxZoomLevel=mBaiduMap.getMaxZoomLevel();
        float minZoomLevel=mBaiduMap.getMinZoomLevel();
        //设置地图中心
//        MapStatusUpdate mapStatusUpdate= MapStatusUpdateFactory.newLatLng(new LatLng(SplashActivity.Bdlocation.getLatitude(),
//                SplashActivity.Bdlocation.getLongitude()));
//        mBaiduMap.setMapStatus(mapStatusUpdate);
        //开启定位图层
        //3种模式 compass 定位圈在地图中心  Following  跟随状态  normal普通态，更新数据不对地图产生操作
        mBaiduMap.setMyLocationEnabled(true);
        MyLocationConfiguration.LocationMode mode=MyLocationConfiguration.LocationMode.COMPASS;
        boolean enableDirection=true;//设置允许显示的方向
        BitmapDescriptor customMarker=BitmapDescriptorFactory.fromResource(R.drawable.icon_geo); //自定义图标
        MyLocationConfiguration config=new MyLocationConfiguration(mode,enableDirection,customMarker);
        mBaiduMap.setMyLocationConfigeration(config);
        MyLocationData.Builder builder=new MyLocationData.Builder();
        builder.accuracy(location.getRadius());  //设置精度
        builder.direction(location.getDirection());  //设置方向
        builder.longitude(location.getLongitude()); //设置经度
        builder.latitude(location.getLatitude()); //设置纬度
        MyLocationData locationData=builder.build();
        mBaiduMap.setMyLocationData(locationData);

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
}
