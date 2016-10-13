package com.neusoft.my12603.my;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.neusoft.my12603.R;
import com.neusoft.my12603.bean.Account;

import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.CONSTANT;
import utils.DialogUtils;
import utils.ToastUtils;

/**
 * Created by 明星 on 2016/9/10.
 */
public class MyAccountActivity extends Activity{

     TextView tv_account;
     public static Account account;
     ListView myaccount;
     Button btn_account;
     SimpleAdapter adapter=null;
     ProgressDialog pDialog=null;
     List<Map<String,Object>> data=new ArrayList<Map<String, Object>>();
     Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (pDialog!=null){
                pDialog.dismiss();
            }
            switch (msg.what){
                case 1:
                    myaccount.setAdapter(adapter);

                    break;
                case 2:
                    ToastUtils.maktText(getApplicationContext(),"连接服务器失败");
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount);
        initView();
        initData();

  }

    private void initView() {
        myaccount = (ListView) findViewById(R.id.lvMyAccountEdit);
        btn_account = (Button) findViewById(R.id.btnMyAccountEditSave);
        btn_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求修改
                new Thread(){
                    public void run(){
                        String url = CONSTANT.HOST + "/otn/Account";
                        RequestParams params = new RequestParams();
                        SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                        String value = pref.getString("Cookie", "");
                        final BasicHeader header = new BasicHeader("Cookie", value);
                        params.addHeader(header);
                        Log.e("kkk", (String) data.get(4).get("key2"));
                        params.addBodyParameter("乘客类型", (String) data.get(4).get("key2"));
                        params.addBodyParameter("电话", (String) data.get(5).get("key2"));
                        params.addBodyParameter("action", "update");
                        HttpUtils httpUtils = new HttpUtils();
                        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<Object>() {
                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
                                String result= (String) responseInfo.result;
                                Gson gson=new GsonBuilder().create();
                                account = gson.fromJson(result,Account.class);
                                Log.e("kkk",account.toString());
                                ToastUtils.maktText(getApplicationContext(),"更新成功");
                                finish();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                ToastUtils.maktText(getApplicationContext(),"请求网络失败");

                            }
                        });
                    }
                }.start();




            }
        });
        myaccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (position){
                    case  4:
                        final String[] types = new String[] { "成人", "学生", "儿童",
                                "其他" };
                        String key2 = (String) (data.get(position).get("key2"));
                        int idx = 0;
                        for (int i = 0; i < types.length; i++) {
                            if (types[i].equals(key2)) {
                                idx = i;
                                break;
                            }
                        }
                        new AlertDialog.Builder(MyAccountActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("请选择乘客类型")
                                .setSingleChoiceItems(types, idx,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method stub
                                               data.get(position).put("key2",
                                                        types[which]);
                                                adapter.notifyDataSetChanged();

                                                dialog.dismiss();
                                            }
                                        }).setNegativeButton("取消", null).show();

                        break;
                    case 5:
                        final EditText edtPhone = new EditText(
                                MyAccountActivity.this);
                        edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                        edtPhone.setText((String) (data.get(position).get("key2")));
                        edtPhone.selectAll(); // 默认选中
                        new AlertDialog.Builder(MyAccountActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("请输入电话")
                                .setView(edtPhone)
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method stub
                                                // 验证
                                                String name = edtPhone.getText()
                                                        .toString();
                                                if (TextUtils.isEmpty(name)) {
                                                    // 设置对话框不能自动关闭
                                                    DialogUtils.setClosable(dialog,
                                                            false);

                                                    edtPhone.setError("请输入电话");
                                                    edtPhone.requestFocus();

                                                } else {
                                                    // 设置对话框自动关闭
                                                    DialogUtils.setClosable(dialog,
                                                            true);

                                                    data.get(position).put(
                                                            "key2",
                                                            edtPhone.getText()
                                                                    .toString());
                                                    // 更新ListView
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        })
                                .setNegativeButton("取消",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method stub
                                                // 设置对话框自动关闭
                                                DialogUtils.setClosable(dialog,
                                                        true);

                                            }
                                        }).show();
                        break;

                }
            }
        });

    }


      public  void initData() {
        //progressDialog是一种进度框，用来等待网络请求，不能放在子线程，需要放在UI线程中去操作。
        pDialog=ProgressDialog.show(MyAccountActivity.this,null,"正在加载...",false,true);
       new Thread(){
           public  void run(){
               final Message msg=handler.obtainMessage();
               String url = CONSTANT.HOST + "/otn/Account";
               RequestParams params = new RequestParams();
               SharedPreferences pref = getSharedPreferences("user", Context.MODE_PRIVATE);
               String value = pref.getString("Cookie", "");
               final BasicHeader header = new BasicHeader("Cookie", value);
               params.addHeader(header);
               params.addBodyParameter("action", "query");
               HttpUtils httpUtils = new HttpUtils();
               httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<Object>() {
                           @Override
                           public void onSuccess(ResponseInfo<Object> responseInfo) {
                               String result= (String) responseInfo.result;
                               Gson gson=new GsonBuilder().create();
                               account = gson.fromJson(result,Account.class);
                               Log.e("kkk",account.toString());
                               ToastUtils.maktText(getApplicationContext(),"密码:"+account.getPassword());
                               Map<String,Object> map1=new HashMap<String, Object>();
                               map1.put("key1","用户名");
                               map1.put("key2",account.getUsername());
                               map1.put("key3",null);
                               data.add(map1);

                               Map<String,Object> map2=new HashMap<String, Object>();
                               map2.put("key1","姓名");
                               map2.put("key2",account.getName());
                               map2.put("key3",null);
                               data.add(map2);

                               Map<String,Object> map3=new HashMap<String, Object>();
                               map3.put("key1","证件类型");
                               map3.put("key2",account.getIdType());
                               map3.put("key3",null);
                               data.add(map3);
                               Map<String,Object> map4=new HashMap<String, Object>();
                               map4.put("key1","证件ID");
                               map4.put("key2",account.getId());
                               map4.put("key3",null);
                               data.add(map4);

                               Map<String,Object> map5=new HashMap<String, Object>();
                               map5.put("key1","乘客类型");
                               map5.put("key2",account.getType());
                               map5.put("key3",R.drawable.forward_25);
                               data.add(map5);

                               Map<String,Object> map6=new HashMap<String, Object>();
                               map6.put("key1","电话");
                               map6.put("key2",account.getTel());
                               map6.put("key3",R.drawable.forward_25);
                               data.add(map6);

                               adapter=new SimpleAdapter(getApplicationContext(),data,
                                       R.layout.contact_item,new String[]{"key1","key2","key3"},
                                       new int[]{ R.id.tvMyContactEditKey,
                                       R.id.tvMyContactEditValue, R.id.imgMyContactEditFlg}
                                       );
                               msg.what=1;
                               handler.sendMessage(msg);
                           }
                           @Override
                           public void onFailure(HttpException e, String s) {
                               msg.what = 2;
                               handler.sendMessage(msg);
                           }
                       }
               );

           }
       }.start();
    }
}
