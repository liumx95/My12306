package com.neusoft.my12603;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 明星 on 2016/9/9.
 */
public class MainActivity extends FragmentActivity {

    ViewPager pager = null;
    long startTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 导航条action bar  点击action bar
        final ActionBar bar = getActionBar();

        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // 获取ViewPager
        pager = (ViewPager) findViewById(R.id.pager);
        // 绑定
        pager.setAdapter(new TabFragmentPagerAdapter(
                getSupportFragmentManager()));
        // 事件处理
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                // 设置选中
                bar.setSelectedNavigationItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        // 添加
        bar.addTab(bar.newTab().setText("车票")
                .setTabListener(new MyTabListener()));
        bar.addTab(bar.newTab().setText("订单")
                .setTabListener(new MyTabListener()));
        bar.addTab(bar.newTab().setText("我的")
                .setTabListener(new MyTabListener()));

    }

    // 适配器类
    class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            Fragment f = null;
            switch (arg0) {
                case 0:
                    f = new TicketFragment();
                    break;
                case 1:
                    f = new OrderFragment();
                    break;
                case 2:
                    f = new MyFragment();
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 3;
        }
    }

    class MyTabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
            // 切换ViewPager
            pager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - startTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT)
                        .show();
                startTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        //菜单事件处理
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_map:
                //跳转到添加新用户
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),BaiduMapActivity.class);
                startActivity(intent);
                break;
        }


        return super.onMenuItemSelected(featureId, item);
    }
}

