package com.neusoft.my12603.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.my12603.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by star on 2016/9/18.
 */
public class TicketStep3Activity extends Activity{

    private ListView lvStep3;
    private TextView tvStep3List;
    private TextView tvStep3Submit;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step3);
        initView();
        initData();
        initControl();
    }




    private void initView() {
        lvStep3 = (ListView) findViewById(R.id.lvTicketPassengerStep3);
        tvStep3List = (TextView) findViewById(R.id.tvTicketPassengerStep3PassengerList);
        tvStep3Submit = (TextView) findViewById(R.id.tvTicketPassengerStep3Submit);
    }
    private void initData() {
        data = new ArrayList<Map<String, Object>>();

        Map<String, Object> row1 = new HashMap<String, Object>();
        row1.put("name", "李四");
        row1.put("idCard", "身份证:10010019990101012X");
        row1.put("tel", "电话:13812345578");
        data.add(row1);

        Map<String, Object> row2 = new HashMap<String, Object>();
        row2.put("name", "订单");
        row2.put("idCard", "身份证:20010019990101012X");
        row2.put("tel", "电话:13912345578");
        data.add(row2);

        Map<String, Object> row3 = new HashMap<String, Object>();
        row3.put("name", "中午");
        row3.put("idCard", "身份证:30010019990101012X");
        row3.put("tel", "电话:13712345578");
        data.add(row3);

    }
    private void initControl() {
        lvStep3.setAdapter(new TickerStepAdapter());
        tvStep3List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(TicketStep3Activity.this,TicketListStep3.class);
                startActivity(intent);
            }
        });
        tvStep3Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(TicketStep3Activity.this,TicketOrderStep3.class);
                startActivity(intent);
            }
        });


    }

    private class TickerStepAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView==null){
                holder=new ViewHolder();
                convertView= LayoutInflater.from(TicketStep3Activity.this).inflate(R.layout.item_step3,null);
               holder.tvTicketName= (TextView) convertView.findViewById(R.id.tvTicketPassengerStep3Name);
               holder.tvTicketIdCard= (TextView) convertView.findViewById(R.id.tvTicketPassengerStep3IdCard);
               holder.tvTivketTel= (TextView) convertView.findViewById(R.id.tvTicketPassengerStep3Tel);
               holder.imgDel= (ImageView) convertView.findViewById(R.id.imgTicketPassengerStep3Del);
                convertView.setTag(holder);

            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            //赋值
            holder.tvTicketName.setText(data.get(position).get("name").toString());
            holder.tvTicketIdCard.setText(data.get(position).get("idCard").toString());
            holder.tvTivketTel.setText(data.get(position).get("tel").toString());
            holder.imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

         class ViewHolder {
             TextView tvTicketName;
             TextView tvTicketIdCard;
             TextView tvTivketTel;
             ImageView imgDel;


        }
    }


}
