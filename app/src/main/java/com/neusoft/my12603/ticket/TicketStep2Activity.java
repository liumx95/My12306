package com.neusoft.my12603.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
public class TicketStep2Activity extends Activity{

    private ListView lvTickerDetailStep2;
    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details_step2);
        initView();
    }

    private void initView() {
        lvTickerDetailStep2 = (ListView) findViewById(R.id.lvTicketDetailsStep2);
        data = new ArrayList<Map<String, Object>>();
        Map<String, Object> row1 = new HashMap<String, Object>();
        row1.put("seatName", "高级软座");
        row1.put("seatNum", "200张");
        row1.put("seatPrice", "￥188.5");
        data.add(row1);

        Map<String, Object> row2 = new HashMap<String, Object>();
        row2.put("seatName", "硬座");
        row2.put("seatNum", "88张");
        row2.put("seatPrice", "￥148.5");
        data.add(row2);

        Map<String, Object> row3 = new HashMap<String, Object>();
        row3.put("seatName", "无座");
        row3.put("seatNum", "100张");
        row3.put("seatPrice", "￥148.5");
        data.add(row3);
        lvTickerDetailStep2.setAdapter(new TicketDetailsStep2Adapter());

    }

    private class TicketDetailsStep2Adapter extends BaseAdapter {
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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null;
            if (convertView==null){
                holder=new ViewHolder();
                //创建converView
                convertView= LayoutInflater.from(TicketStep2Activity.this).inflate(R.layout.item_ticket_result_step2,null);
                holder.tvTicketName= (TextView) convertView.findViewById(R.id.tvTicketDetailsStep2SeatName);
                holder.tvTicketNum= (TextView) convertView.findViewById(R.id.tvTicketDetailsStep2SeatNum);
                holder.tvTicketPrice= (TextView) convertView.findViewById(R.id.tvTicketDetailsStep2SeatPrice);
                holder.btnTicketOrder= (Button) convertView.findViewById(R.id.btnTicketDetailsStep2Order);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
                //赋值
            holder.tvTicketName.setText(data.get(position).get("seatName").toString());
            holder.tvTicketNum.setText(data.get(position).get("seatNum").toString());
            holder.tvTicketPrice.setText(data.get(position).get("seatPrice").toString());
            holder.btnTicketOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setClass(TicketStep2Activity.this,TicketStep3Activity.class);
                    startActivity(intent);

                }
            });


            return convertView;
        }
    }

     class ViewHolder {
         TextView tvTicketName;
         TextView tvTicketNum;
         TextView tvTicketPrice;
         Button btnTicketOrder;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ticket_details_step2,menu);
        return true;
    }
}
