package com.neusoft.my12603.ticket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.neusoft.my12603.R;
import com.neusoft.my12603.bean.Seat;
import com.neusoft.my12603.bean.Train;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.CONSTANT;
import utils.ToastUtils;

/**
 * Created by star on 2016/9/18.
 */
public class TicketStep1Activity extends Activity {
    ListView lvTicketResultStep1 = null;
    TextView tvTicketResultStep1Before = null;
    TextView tvTicketResultStep1After = null;
    TextView tvTicketResultStep1DateTitle = null;
    ProgressDialog pDialog=null;
    private TextView datetitle=null;
    private TextView formtitle;
    private TextView totitle;
    List<Map<String, Object>> data = null;
    private String startTrainDate,fromStationName,toStationName;
    private SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_result_step1);
        initView();
        initData();

    }


    private void initView() {
        datetitle = (TextView) findViewById(R.id.tvTicketResultStep1DateTitle);

        lvTicketResultStep1 = (ListView) findViewById(R.id.lvTicketResultStep1);
        tvTicketResultStep1Before = (TextView) findViewById(R.id.tvTicket_before);
        tvTicketResultStep1After = (TextView) findViewById(R.id.tvTicket_after);
        tvTicketResultStep1DateTitle = (TextView) findViewById(R.id.tvTicketResultStep1DateTitle);
        tvTicketResultStep1Before.setOnClickListener(new HandlerTicketResultStep1());
        tvTicketResultStep1After.setOnClickListener(new HandlerTicketResultStep1());
        formtitle = (TextView) findViewById(R.id.tvTicketResultStep1StationFromTitle);
        totitle = (TextView) findViewById(R.id.tvTicketResultStep1StationToTitle);

    }

    private void initData() {
        startTrainDate = getIntent().getStringExtra("startTrainDate");
        fromStationName = getIntent().getStringExtra("fromStationName");
        toStationName = getIntent().getStringExtra("toStationName");
        datetitle.setText(startTrainDate);
        formtitle.setText(fromStationName);
        totitle.setText(toStationName);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> row1 = new HashMap<String, Object>();
        row1.put("trainNo", "G108");
        row1.put("flg1", R.drawable.flg_shi);
        row1.put("flg2", R.drawable.flg_guo);
        row1.put("timeFrom", "07:00");
        row1.put("timeTo", "14:39(0日)");
        row1.put("seat1", "高级软卧:100");
        row1.put("seat2", "硬座:8");
        row1.put("seat3", "一等座:20");
        row1.put("seat4", "商务座:200");
        data.add(row1);

        Map<String, Object> row2 = new HashMap<String, Object>();
        row2.put("trainNo", "D1");
        row2.put("flg1", R.drawable.flg_shi);
        row2.put("flg2", R.drawable.flg_zhong);
        row2.put("timeFrom", "09:00");
        row2.put("timeTo", "12:39(0日)");
        row2.put("seat1", "高级软卧:100");
        row2.put("seat2", "硬座:8");
        row2.put("seat3", "一等座:20");
        row2.put("seat4", "商务座:200");
        data.add(row2);

        Map<String, Object> row3 = new HashMap<String, Object>();
        row3.put("trainNo", "K7777");
        row3.put("flg1", R.drawable.flg_guo);
        row3.put("flg2", R.drawable.flg_guo);
        row3.put("timeFrom", "15:00");
        row3.put("timeTo", "12:39(1日)");
        row3.put("seat1", "高级软卧:55");
        row3.put("seat2", "硬座:77");
        row3.put("seat3", "一等座:33");
        data.add(row3);
        adapter = new SimpleAdapter(this, data, R.layout.item_ticket_result_step1,
                new String[]{"trainNo",
                        "flg1", "flg2", "timeFrom", "timeTo", "seat1", "seat2",
                        "seat3", "seat4"}, new int[]{
                R.id.tvTicketResultStep1TrainNo,
                R.id.imgTicketResultStep1Flg1,
                R.id.imgTicketResultStep1Flg2,
                R.id.tvTicketResultStep1TimeFrom,
                R.id.tvTicketResultStep1TimeTo,
                R.id.tvTicketResultStep1Seat1,
                R.id.tvTicketResultStep1Seat2,
                R.id.tvTicketResultStep1Seat3,
                R.id.tvTicketResultStep1Seat4,});
        lvTicketResultStep1.setAdapter(adapter);
        lvTicketResultStep1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(TicketStep1Activity.this, TicketStep2Activity.class);
                startActivity(intent);

            }
        });
        //调用异步任务
//        new Step1Task().execute();

    }

    private class HandlerTicketResultStep1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Calendar c = Calendar.getInstance();
            //获取选中日期
            String oldDateFrom = tvTicketResultStep1DateTitle.getText().toString();
            int oldYear = Integer
                    .parseInt(oldDateFrom.split(" ")[0].split("-")[0]);
            int oldMonthOfYear = Integer.parseInt(oldDateFrom.split(" ")[0]
                    .split("-")[1]) - 1;
            int oldDayOfMonth = Integer.parseInt(oldDateFrom.split(" ")[0]
                    .split("-")[2]);
            c.set(oldYear, oldMonthOfYear, oldDayOfMonth);
            switch (v.getId()) {
                case R.id.tvTicket_before:
                    c.add(Calendar.DAY_OF_MONTH, -1);
                    break;
                case R.id.tvTicket_after:
                    c.add(Calendar.DAY_OF_MONTH, +1);
                    break;
            }
            //更新选中日期
            String weekDay = DateUtils.formatDateTime(TicketStep1Activity.this, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_WEEKDAY
                    | DateUtils.FORMAT_ABBREV_ALL);
            tvTicketResultStep1DateTitle.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
                    c.get(Calendar.DAY_OF_MONTH) + " " + weekDay);

            //调用异步任务
//            new Step1Task().execute();

        }
    }
       //使用第三个参数返回后台线程的结果
    class Step1Task extends AsyncTask<String,String,Object> {

           private Train[] trains;

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
//               pDialog = ProgressDialog.show(getApplicationContext(), null, "正在加载...", false, true);
           }

           @Override
           protected Object doInBackground(String... params) {
               //请求网络
               HttpPost post = new HttpPost(CONSTANT.HOST + "/otn/TrainList");
               //创建客户端
               DefaultHttpClient client = new DefaultHttpClient();
               String result = "";
               //jsessionid
               SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
               String value = pref.getString("Cookie", "");
               //发送请求头
               BasicHeader header = new BasicHeader("Cookie", value);
               post.setHeader(header);
               //请求参数
               Intent intent = getIntent();
               List<BasicNameValuePair> param = new ArrayList<BasicNameValuePair>();
               param.add(new BasicNameValuePair("fromStationName", intent.getStringExtra("fromStationName")));
               param.add(new BasicNameValuePair("toStationName", intent
                       .getStringExtra("toStationName")));
               param.add(new BasicNameValuePair("startTrainDate",
                       intent.getStringExtra("startTrainDate")));
               try {
                   UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param, "UTF-8");
                   post.setEntity(entity);
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               }
               //超时设置
               client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONSTANT.REQUEST_TIMEOUT);
               client.getParams().setParameter(
                       CoreConnectionPNames.SO_TIMEOUT, CONSTANT.SO_TIMEOUT);
               try {
                   HttpResponse response = client.execute(post);
                   //处理结果
                   if (response.getStatusLine().getStatusCode() == 200) {
                       String json = EntityUtils.toString(response.getEntity());
                       Gson gosn = new GsonBuilder().create();
                       Train[] trains = gosn.fromJson(json, Train[].class);
                       Log.e("kkk", trains.toString());
                       return trains;

                   } else {
                       result = "2";

                   }
                   client.getConnectionManager().shutdown();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               //处理结果
               return result;
           }

           @Override
           protected void onPostExecute(Object result) {
               super.onPostExecute(result);
//               data.clear();
               if (pDialog != null) {
                   pDialog.dismiss();
               }
               if (result instanceof Train[]) {
                   trains = (Train[]) result;
                   if (trains.length == 0) {
                       ToastUtils.maktText(getApplicationContext(), "没有查询到对应的车次");
                   } else {
                       for (Train train : trains) {
                           Map<String, Object> row1 = new HashMap<String, Object>();
                           row1.put("trainNO", train.getTrainNo());
                           if (train.getStartStationName().equals(
                                   train.getFromStationName())) {
                               row1.put("flg1", R.drawable.flg_shi);
                           } else {
                               row1.put("flg1", R.drawable.flg_guo);
                           }

                           if (train.getEndStationName().equals(
                                   train.getToStationName())) {
                               row1.put("flg2", R.drawable.flg_zhong);
                           } else {
                               row1.put("flg2", R.drawable.flg_guo);
                           }

                           row1.put("timeFrom", train.getStartTime());
                           row1.put(
                                   "timeTo",
                                   train.getArriveTime() + "("
                                           + train.getDayDifference() + "日)");

                           String[] seatKey = {"seat1", "seat2", "seat3", "seat4"};
                           Map<String, Seat> seats = train.getSeats();
                           int i = 0;
                           for (String key : seats.keySet()) {
                               Seat seat = seats.get(key);
                               row1.put(seatKey[i++], seat.getSeatName() + ":"
                                       + seat.getSeatNum());
                           }

                           data.add(row1);
                       }
                   }

                   adapter.notifyDataSetChanged();

               } else if (result instanceof String) {

                   Toast.makeText(getApplicationContext(), "服务器错误，请重试",
                           Toast.LENGTH_SHORT).show();

               }

           }
       }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticket_step1,menu);
        return true;
    }
}

