package com.neusoft.my12603.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.neusoft.my12603.MyFragment;
import com.neusoft.my12603.R;

import org.apache.http.message.BasicHeader;

import utils.CONSTANT;
import utils.ToastUtils;

/**
 * Created by 明星 on 2016/9/10.
 */
public class MyPasswordActivity extends Activity{
    private EditText et_pwd;
    private EditText mEt_pwd;
    private EditText mEt_pwd_again;
    private Button mBtn_save_pwd;
    private String info;
    private String pwd;
    private String result;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Log.e("kkk", msg.obj.toString());
                    //就是他妈的字符串的事，是 "\"1\""
                    if ("\"1\"".equals(msg.obj.toString())){
                    ToastUtils.maktText(getApplicationContext(),"修改成功");
                }

                    break;
                case 2:
                    ToastUtils.maktText(getApplicationContext(),"无法获取数据");
                    break;


            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypassword);
        initView();
        initData();
    }

    private void initView() {
        mEt_pwd = (EditText) findViewById(R.id.et_pwd);
        mEt_pwd_again = (EditText) findViewById(R.id.et_pwd_again);
        mBtn_save_pwd = (Button) findViewById(R.id.btn_save_pwd);
    }

    private void initData() {

            mBtn_save_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd_again = mEt_pwd_again.getText().toString();
                    pwd = mEt_pwd.getText().toString();
                    if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(pwd_again)) {
                        ToastUtils.maktText(getApplicationContext(), "输入为空");
                    } else {
                        if (pwd.equals(MyAccountActivity.account.getPassword())) {
                            ToastUtils.maktText(getApplicationContext(), "与原密码相同，请重新输入");
                        } else {
                            if (pwd.equals(pwd_again)) {
                                //请求网络
                                httpUtils();
                                finish();
                            } else {
                                ToastUtils.maktText(getApplicationContext(), "两次输入不一致，请重新输入");
                            }
                        }
                    }
                }
            });
    }

     private void httpUtils() {
        new Thread(){
            public void run(){
                final Message msg=handler.obtainMessage();
                String url= CONSTANT.HOST+"otn/AccountPassword";
                RequestParams params=new RequestParams();
                SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
                String value=sp.getString("Cookie","");
                final BasicHeader header=new BasicHeader("Cookie",value);
                params.addHeader(header);
                params.addBodyParameter("newPassword",pwd);
                params.addBodyParameter("aciton","update");
                HttpUtils utils=new HttpUtils();
                utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<Object>() {

                    @Override
                    public void onSuccess(ResponseInfo<Object> responseInfo) {
                        result = responseInfo.result.toString();
                        Log.e("kkk", result +"");
                        msg.what=1;
                        msg.obj= result;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        msg.what=2;
                        handler.sendMessage(msg);

                    }
                });
            }
        }.start();

    }
}
