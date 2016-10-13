package com.neusoft.my12603;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
//import com.baidu.location.Poi;

import org.androidpn.client.ServiceManager;

public class SplashActivity extends Activity {

    private String name;
    private String pwd;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public static String location;
    public static BDLocation Bdlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏模式
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();

        //获取通知服务
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(org.androidpn.demoapp.R.drawable.notification);
        serviceManager.startService();

        //从SharePreference中 获取信息，注意 sp.getString("username","");获取内容，不能有空格
        SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
        name = sp.getString("username", "");
        pwd = sp.getString("password", "");
        Log.e("kkk", name);
            Log.i("DDD", "" + Thread.currentThread().getId());

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("ddd", "" + Thread.currentThread().getId());
                    if (TextUtils.isEmpty(name) && TextUtils.isEmpty(pwd)) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            },3000);
      }
    class MyLocationListener implements BDLocationListener{


        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            location = bdLocation.getCity().split("市")[0];
            Bdlocation=bdLocation;
            Log.e("kkk", location);
        }
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=2000*10;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        option.setIsNeedAddress(true);

    }
}

