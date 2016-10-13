package com.neusoft.my12603;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.neusoft.my12603.order.NoOrderActivity;
import com.neusoft.my12603.order.OrderActivity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 明星 on 2016/9/9.
 */
public class OrderFragment extends android.support.v4.app.Fragment {

    private TextView mFor_pay;
    private TextView mTv_order;
    private ListView lv_order;
    private List<Map<String, Object>> data;
    private MyAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_fragmet, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //fragment中 onActivitycreated 就获取到了依赖的Activity，得到了创建
        //fragment与activity之间通信，先获取当前的activity
        mFor_pay = (TextView) getActivity().findViewById(R.id.tv_for_pay);
        mTv_order = (TextView) getActivity().findViewById(R.id.tv_order);
        lv_order = (ListView) getActivity().findViewById(R.id.lv_order);
        mFor_pay.setOnClickListener(new OrderHandler());
        mTv_order.setOnClickListener(new OrderHandler());
        data=new ArrayList<Map<String,Object>>();
        Map<String, Object> row1= new HashMap<String, Object>();
        row1.put("order_id", "订单编号：20143Yg");
        row1.put("order_stae", "未支付");
        row1.put("train", "G108");
        row1.put("time", "2014-8-18");
        row1.put("trainFrom", "北京-上海 2人");
        row1.put("menoy", "¥1000.5元");
        row1.put("orderFlg",R.drawable.forward_25);
        data.add(row1);
        adapter = new MyAdapter();
        lv_order.setAdapter(adapter);
        final Intent intent=new Intent();
        lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("未支付".equals(data.get(position).get("order_stae").toString())){
                    intent.setClass(getActivity(),NoOrderActivity.class);
                    startActivity(intent);
                }else if ("已支付".equals(data.get(position).get("order_stae").toString())){
                    intent.setClass(getActivity(),OrderActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    private class MyAdapter extends BaseAdapter {

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
            //listview的优化 ViewHolder
            ViewHolder holder=null;
            if (convertView==null){
                holder=new ViewHolder();
                //convertView用复用item的方法来达到优化listview的目的
                convertView=LayoutInflater.from(getActivity()).inflate(R.layout.order_item,null);
                 holder.tvOrderId= (TextView) convertView.findViewById(R.id.tvOrderId);
                 holder.tvOrderStatus= (TextView) convertView.findViewById(R.id.tvOrderStatus);
                 holder.tvOrderTrainNo= (TextView) convertView.findViewById(R.id.tvOrderTrainNo);
                 holder.tvOrderDateFrom= (TextView) convertView.findViewById(R.id.tvOrderDateFrom);
                 holder.tvOrderStationFrom= (TextView) convertView.findViewById(R.id.tvOrderStationFrom);
                 holder. tvOrderPrice= (TextView) convertView.findViewById(R.id.tvOrderPrice);
                holder.imgOrderFlg= (ImageView) convertView.findViewById(R.id.imgOrderFlg);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
                //赋值
            holder.tvOrderId.setText(data.get(position).get("order_id").toString());
            holder.tvOrderStatus.setText(data.get(position).get("order_stae").toString());
            if ("未支付".equals(holder.tvOrderStatus.getText().toString())){
                holder.tvOrderStatus.setTextColor(getResources().getColor(R.color.colorRed));

            }else  if ("已支付".equals(holder.tvOrderStatus.getText().toString())){
                holder.tvOrderStatus.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if ("已取消".equals(holder.tvOrderStatus.getText().toString())){
                holder.tvOrderStatus.setTextColor(getResources().getColor(R.color.colorGray));
            }
            holder.tvOrderTrainNo.setText(data.get(position)
                    .get("train").toString());
            holder.tvOrderDateFrom.setText(data.get(position)
                    .get("time").toString());
            holder.tvOrderStationFrom.setText(data.get(position)
                    .get("trainFrom").toString());
            holder.tvOrderPrice.setText(data.get(position).get("menoy")
                    .toString());
            Integer resId= (Integer) data.get(position).get("orderFlg");
            if (resId!=null){
                holder.imgOrderFlg.setImageDrawable(getResources().getDrawable(resId));
            }else {
                holder.imgOrderFlg.setImageDrawable(null);
            }
            return convertView;
        }
        //创建ViewHolder类，优化listview,把每个子view都放在holder中，当第一次创建convertView是，把子view找出来，
        //用convertView的setTag将viewHolder设置到Tag中。
        private class ViewHolder {
            TextView tvOrderId;
            TextView tvOrderStatus;
            TextView tvOrderTrainNo;
            TextView tvOrderDateFrom;
            TextView tvOrderStationFrom;
            TextView tvOrderPrice;
            ImageView imgOrderFlg;

        }
    }

    class OrderHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            data.clear();
           switch (v.getId()){
            case  R.id.tv_for_pay:
                mFor_pay.setBackgroundResource(R.drawable.cab_background_top_mainbar);
                mTv_order.setBackgroundResource(0);
                Map<String, Object> row1 = new HashMap<String, Object>();
                row1.put("order_id", "订单编号：20143Yg");
                row1.put("order_stae", "未支付");
                row1.put("train", "G108");
                row1.put("time", "2014-8-18");
                row1.put("trainFrom", "北京-上海 2人");
                row1.put("menoy", "¥1000.5元");
                row1.put("orderFlg",R.drawable.forward_25);
                data.add(row1);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_order:
                mTv_order.setBackgroundResource(R.drawable.cab_background_top_mainbar);
                mFor_pay.setBackgroundResource(0);
                Map<String, Object> row2= new HashMap<String, Object>();
                row2.put("order_id", "订单编号：20143Yg");
                row2.put("order_stae", "未支付");
                row2.put("train", "G108");
                row2.put("time", "2014-8-18");
                row2.put("trainFrom", "北京-上海 2人");
                row2.put("menoy", "¥1000.5元");
                row2.put("orderFlg",R.drawable.forward_25);
                data.add(row2);
                Map<String, Object> row3= new HashMap<String, Object>();
                row3.put("order_id", "订单编号：20143Yg");
                row3.put("order_stae", "已支付");
                row3.put("train", "G108");
                row3.put("time", "2014-8-18");
                row3.put("trainFrom", "北京-上海 2人");
                row3.put("menoy", "¥1000.5元");
                row3.put("orderFlg",R.drawable.forward_25);
                data.add(row3);
                Map<String, Object> row4= new HashMap<String, Object>();
                row4.put("order_id", "订单编号：20143Yg");
                row4.put("order_stae", "已取消");
                row4.put("train", "G108");
                row4.put("time", "2014-8-18");
                row4.put("trainFrom", "北京-上海 2人");
                row4.put("menoy", "¥1000.5元");
                row4.put("orderFlg",null);
                data.add(row4);
                adapter.notifyDataSetChanged();
          }

        }
    }

}
