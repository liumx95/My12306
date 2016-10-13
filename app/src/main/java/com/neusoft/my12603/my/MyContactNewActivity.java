package com.neusoft.my12603.my;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.neusoft.my12603.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.ToastUtils;

/**
 * Created by 明星 on 2016/9/10.
 */
public class MyContactNewActivity extends Activity{

    private TextView tvMyContactNew1;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_newactivity);
        initView();
    }



    private void initView() {
       tvMyContactNew1 = (TextView) findViewById(R.id.tvMyContactNew1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_contact_new,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_finduser_contact:
//                pDialog = ProgressDialog.show(MyContactNewActivity.this,null,"正在加载...",false,true);
                ToastUtils.maktText(getApplicationContext(),"正在加载联系人");
                ContentResolver cr=getContentResolver();
                Cursor c=cr.query(ContactsContract.Contacts.CONTENT_URI,
                        new String[]{"_id","display_name"},null,null,null);
                List<String> contacts=new ArrayList<String>();
                while (c.moveToNext()){
                 String _id=c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                 String display_name=c.getString(c.getColumnIndex("display_name"));
                    //查找电话
                    Cursor c2=cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                            +"=?",new String[]{_id+""},null);
                    String number=null;
                    while (c2.moveToNext()){
                        number=c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                      }
                    c2.close();
                   Log.e("hehe","name="+display_name);
                   Log.e("hehe","num="+number);
                    contacts.add(display_name + "(" + number+ ")");
                }
                c.close();
//                if (pDialog!=null){
//                    pDialog.dismiss();
//                }

                if (contacts.size() == 0) {
                    new AlertDialog.Builder(MyContactNewActivity.this)
                            .setTitle("请选择").setMessage("通讯录为空")
                            .setNegativeButton("取消", null).show();
                } else {

                    final String[] items = new String[contacts.size()];
                    contacts.toArray(items);

                    // AlertDialog
                    new AlertDialog.Builder(MyContactNewActivity.this)
                            .setTitle("请选择")
                            .setItems(items, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    tvMyContactNew1.setText(items[which]);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("取消", null).show();
                }

                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
