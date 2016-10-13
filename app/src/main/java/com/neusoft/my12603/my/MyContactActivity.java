package com.neusoft.my12603.my;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.my12603.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.CONSTANT;
import utils.ToastUtils;

/**
 * Created by 明星 on 2016/9/10.
 */
public class  MyContactActivity extends Activity{

    private List<Map<String, Object>> mListcontact=new ArrayList<Map<String,Object>>();
    private ListView lv_contact;
    private ProgressDialog pDialog=null;
    private MyAdapter myAdapter=new MyAdapter();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (pDialog!=null){
                pDialog.dismiss();
            }

            switch (msg.what){
                case 1:
                    lv_contact.setAdapter(myAdapter);

                    break;
                case 2:
                    ToastUtils.maktText(getApplicationContext(),"请求数据失败");
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mycontact);
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        
        initView();
//

    }
    private void initData() throws IOException {


        //获得网络请求
        HttpEntity entity;
        String data = null;
        HttpClient client = new DefaultHttpClient();
        SharedPreferences pref=getSharedPreferences("user", Context.MODE_PRIVATE);
        String value=pref.getString("Cookie","");
        //发送请求头
        BasicHeader header=new BasicHeader("Cookie",value);
        HttpPost post = new HttpPost(CONSTANT.HOST+"otn/PassengerList");
        StringBuilder builder = new StringBuilder();
        post.setHeader(header);
        Message msg = handler.obtainMessage();
        //从网络中解析数据
        //是client客户端发送请求post
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == 200) {
            //然后请求获取实体response.getEntity，通过实体获得字节流再解析。
            entity = response.getEntity();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while ((data = reader.readLine()) != null) {
                builder.append(data);

            }
            try {

                JSONArray array = new JSONArray(builder.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Map<String, Object> row = new HashMap<String, Object>();
                    row.put("name", object.getString("name")+"("+object.getString("type")+")");
                    Log.e("kkk",object.getString("name")+":"+object.getString("type"));
                    row.put("idcard", object.getString("idType")+":"+object.getString("id"));
                    Log.e("kkk",object.getString("idType")+":"+object.getString("id"));
                    row.put("tel", "电话:"+object.getString("tel"));
                    Log.e("kkk","电话:"+object.getString("tel"));
                    mListcontact.add(row);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            msg.what = 1;

        } else {
            msg.what = 2;
        }

        handler.sendMessage(msg);
    }

    private void initView() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),MyContactEditActivity.class);
                intent.putExtra("row", (Serializable) mListcontact.get(position));
                startActivity(intent);
                }

        });
    }

    private class MyAdapter  extends BaseAdapter{
        @Override
        public int getCount() {
            return mListcontact.size();
        }

        @Override
        public Object getItem(int position) {
            return mListcontact.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(getApplicationContext(),R.layout.item_step3,null);
            TextView tv_name= (TextView) view.findViewById(R.id.tvTicketPassengerStep3Name);
            TextView tv_cid= (TextView) view.findViewById(R.id.tvTicketPassengerStep3IdCard);
            TextView tv_phone= (TextView) view.findViewById(R.id.tvTicketPassengerStep3Tel);
            tv_name.setText((CharSequence) mListcontact.get(position).get("name"));
            tv_cid.setText((CharSequence) mListcontact.get(position).get("idcard"));
            tv_phone.setText((CharSequence) mListcontact.get(position).get("tel"));
            return view;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mListcontact.clear();
        pDialog = ProgressDialog.show(MyContactActivity.this,null,"正在加载",false,true);
        new Thread() {
                @Override
                public void run() {
                    try {
                        initData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }.start();
        myAdapter.notifyDataSetChanged();
        lv_contact.setAdapter(myAdapter);

        //访问服务器，跟新数据

        Toast.makeText(MyContactActivity.this,"刷新",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_contact,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        //菜单事件处理
       switch (item.getItemId()){
           case android.R.id.home:
               finish();
               break;
           case R.id.mn_contact_add:
               //跳转到添加新用户
               Intent intent=new Intent();
               intent.setClass(getApplicationContext(),MyContactNewActivity.class);
               startActivity(intent);
               break;
       }
        return super.onMenuItemSelected(featureId, item);
    }
}
