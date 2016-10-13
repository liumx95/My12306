package com.neusoft.my12603;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.neusoft.my12603.my.MyAccountActivity;
import com.neusoft.my12603.my.MyContactActivity;
import com.neusoft.my12603.my.MyPasswordActivity;

import org.apache.http.message.BasicHeader;

import utils.CONSTANT;
import utils.DialogUtils;
import utils.ToastUtils;

/**
 * Created by 明星 on 2016/9/9.
 */
public class MyFragment extends android.support.v4.app.Fragment {

    private Button mBtn_fargment;
    private ListView mLv_list;

    public MyFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.my_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtn_fargment = (Button) getActivity().findViewById(R.id.btn_fragment);
        mLv_list = (ListView) getActivity().findViewById(R.id.lv_list);
        String[] data=getResources().getStringArray(R.array.my_list_data);
        //适配器  三个参数，上下文，item布局文件,填充的数据
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,data);
        //绑定
        mLv_list.setAdapter(adapter);
        mLv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(getActivity(),MyContactActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(),MyAccountActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        final EditText et_pwd=new EditText(getActivity());
                        et_pwd.selectAll();
                        et_pwd.setHint("请输入原密码");
//                        intent.setClass(getActivity(),MyPasswordActivity.class);
                         new AlertDialog.Builder(getActivity())
                                 .setIcon(R.drawable.lock_25)
                                 .setTitle("输入密码")
                                 .setView(et_pwd)
                                 .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         DialogUtils.setClosable(dialog,true);
                                     }
                                 })
                                 .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                       String pwd= MyAccountActivity.account.getPassword();
                                       if (et_pwd.getText().toString().equals(pwd)){
                                           Intent intent=new Intent(getActivity(),MyPasswordActivity.class);
                                           startActivity(intent);

                                       }else {
                                           ToastUtils.maktText(getActivity(),"输入密码错误，请联系客服");
                                       }
                                     }
                                 }).show();

                        break;
                }

            }
        });
        MyButtonListener btn_listener=new MyButtonListener();
        mBtn_fargment.setOnClickListener(btn_listener);

    }


    private class MyButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(getActivity(),LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
