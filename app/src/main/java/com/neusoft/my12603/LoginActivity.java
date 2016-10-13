package com.neusoft.my12603;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.XMLFormatter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import utils.CONSTANT;
import utils.Md5Utils;
import utils.MyXmlHandler;
import utils.NetUtils;
import utils.ToastUtils;
public class LoginActivity extends Activity {

    private EditText mEt_name;
    private EditText mEt_pwd;
    private Button mBtn_login;
    private CheckBox mCb;
    private TextView mTv_pwd;
    private SharedPreferences sp;
    private SharedPreferences.Editor mEditor;
    ProgressDialog pDialog=null;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (pDialog!=null){
                pDialog.dismiss();
            }
            switch (msg.what) {
                case 1:
                    String jsessionid = (String) msg.obj;
                    int result=msg.arg1;

                    if (!(result==1)) {

                        mEt_pwd.selectAll();
                        mEt_pwd.setError("失败");
                        mEt_pwd.requestFocus();
                    } else  {
                        SharedPreferences pref = getSharedPreferences("user",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putString("Cookie", jsessionid);

                        if (mCb.isChecked()) {
                            editor.putString("username", mEt_name.getText()
                                    .toString());
                            editor.putString("password",
                                    Md5Utils.MD5(mEt_pwd.getText().toString()));
                        } else {
                            editor.remove("username");
                            editor.remove("password");
                        }

                        editor.commit();


                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);

                        LoginActivity.this.finish();
                    }

                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "没有该用户",
                            Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        mEt_name = (EditText) findViewById(R.id.et_name);
        mEt_pwd = (EditText) findViewById(R.id.et_pwd);
        mBtn_login = (Button) findViewById(R.id.btn_login);
        mCb = (CheckBox) findViewById(R.id.cb);
        mTv_pwd = (TextView) findViewById(R.id.tv_pwd);

    }
    private void initData() {
        sp = getSharedPreferences("user",MODE_PRIVATE);
        mEditor = sp.edit();
        mTv_pwd.setText(Html.fromHtml("<a href=\"http://www.baidu.com\">忘记密码</a>"));
        mTv_pwd.setMovementMethod(LinkMovementMethod.getInstance());
        mBtn_login.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(mEt_name.getText().toString())&&
                            !TextUtils.isEmpty(mEt_pwd.getText().toString())) {
                        if (!NetUtils.check(LoginActivity.this)){
                            ToastUtils.maktText(LoginActivity.this,"无法连接网络");
                            return;
                        }
                      pDialog=ProgressDialog.show(LoginActivity.this,null,"正在连接",false,true);
                        new Thread(){
                            public void run() {
                                Message msg = handler.obtainMessage();
                                HttpPost post = new HttpPost(CONSTANT.HOST + "/Login");
                                //http请求头
                                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                                params.add(new BasicNameValuePair("username", mEt_name.getText().toString()));
                                params.add(new BasicNameValuePair("password",Md5Utils.MD5(mEt_pwd.getText().toString())));
                                UrlEncodedFormEntity entity;
                                try {
                                    entity = new UrlEncodedFormEntity(params, "UTF-8");
                                    post.setEntity(entity);

                                    DefaultHttpClient client = new DefaultHttpClient();
                                    client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
                                            CONSTANT.REQUEST_TIMEOUT);
                                    client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                                            CONSTANT.SO_TIMEOUT);
                                    HttpResponse response = client.execute(post);
                                    Log.d("My12306", "result:"
                                            + 5);

                                    if (response.getStatusLine().getStatusCode() == 200) {

                                         StringBuilder builder=new StringBuilder();
                                         HttpEntity httpEntity=response.getEntity();  //得到一个http实体
                                         InputStream inputStream=httpEntity.getContent();
                                        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                                        String  str;
                                        while ((str=reader.readLine())!=null){
                                            builder.append(str);

                                        }
                                        Log.e("TAG",builder.toString());
                                        SAXParserFactory factory = SAXParserFactory.newInstance();
                                        XMLReader xmlReader = factory.newSAXParser().getXMLReader();
                                        MyXmlHandler handler = new MyXmlHandler();
                                        xmlReader.setContentHandler(handler);
                                        try{
                                        xmlReader.parse(new InputSource(new StringReader(builder.toString())));
                                        }catch (Exception e){
                                          e.printStackTrace();
                                        }

                                        String value = "";
                                        List<Cookie> cookies = client
                                                .getCookieStore().getCookies();
                                        for (Cookie cookie : cookies) {
                                            if ("JSESSIONID".equals(cookie
                                                    .getName())) {
                                                value = cookie.getValue();
                                                Log.d("My12306", "JSESSIONID:"
                                                        + value);
                                                break;
                                            }
                                        }
                                        msg.what = 1;
                                        msg.arg1 =Integer.parseInt( MyXmlHandler.s.toString());
                                        Log.e("kkk",MyXmlHandler.s.toString());
                                        msg.obj = "JSESSIONID=" + value;
                                    } else {
                                        msg.what = 2;
                                    }

                                    client.getConnectionManager().shutdown();
                                    handler.sendMessage(msg);


                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (ClientProtocolException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SAXException e) {
                                    e.printStackTrace();
                                } catch (ParserConfigurationException e) {
                                    e.printStackTrace();
                                }

                            }
                        }.start();


                    }else {

                        Toast.makeText(getApplicationContext(),"输入为空",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

