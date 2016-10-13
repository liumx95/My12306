package com.neusoft.my12603;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.neusoft.my12603.stationlist.StationListActivity;
import com.neusoft.my12603.ticket.TicketStep1Activity;

import java.util.Calendar;

/**
 * Created by 明星 on 2016/9/9.
 */
public class TicketFragment extends android.support.v4.app.Fragment{

    private TextView tvTicketStationFrom;
    private TextView tvTicketStationTo;
    private TextView tvTicketHistory1;
    private TextView tvTicketHistory2;
    private ImageView imgTicketExchange;
    private TextView tvTicketDateFrom;
    private Button btnTicketQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.ticker_fargmet,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

    }
    public void initView() {
        tvTicketStationFrom = (TextView) getActivity().findViewById(R.id.tvTicketStationFrom);
        tvTicketStationTo = (TextView) getActivity().findViewById(R.id.tvTicketStationTo);
        imgTicketExchange= (ImageView) getActivity().findViewById(R.id.imgTicketExchange);
        tvTicketDateFrom = (TextView) getActivity().findViewById(R.id.tvTicketDateFrom);
        btnTicketQuery = (Button) getActivity().findViewById(R.id.btnTicketQuery);
        tvTicketHistory1 = (TextView) getActivity().findViewById(R.id.tvTicketHistory1);
        tvTicketHistory2 = (TextView) getActivity().findViewById(R.id.tvTicketHistory2);
        tvTicketStationFrom.setText(SplashActivity.location);
        tvTicketStationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),StationListActivity.class);
                startActivityForResult(intent,100);
            }
        });
        tvTicketStationTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),StationListActivity.class);
                startActivityForResult(intent,101);
            }
        });
        imgTicketExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stationFrom= tvTicketStationFrom.getText().toString();
                final String stationTo= tvTicketStationTo.getText().toString();
                final TranslateAnimation animationFrom=new TranslateAnimation(0,150,0,0);
                animationFrom.setInterpolator(new AccelerateInterpolator());
                animationFrom.setDuration(300);
                animationFrom.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvTicketStationTo.setText(stationFrom);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                });
                TranslateAnimation animationTo=new TranslateAnimation(0,-150,0,0);
                animationTo.setInterpolator(new AccelerateInterpolator());
                animationTo.setDuration(300);
                animationTo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvTicketStationFrom.setText(stationTo);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }


                });
                //TextView移动
                tvTicketStationFrom.startAnimation(animationFrom);
                tvTicketStationTo.startAnimation(animationTo);

            }
        });

        tvTicketDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldDataFrom= tvTicketDateFrom.getText().toString();
                int oldYear=Integer.parseInt(oldDataFrom.split(" ")[0].split("-")[0]);
                int oldMonthOfYear=Integer.parseInt(oldDataFrom.split(" ")[0].split("-")[1])-1;
                int oldDayOfMouth=Integer.parseInt(oldDataFrom.split(" ")[0].split("-")[2]);
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    //重写该函数
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar c2=Calendar.getInstance();
                        c2.set(year,monthOfYear,dayOfMonth);
                        String weekDay= DateUtils.formatDateTime(getActivity(),c2.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_WEEKDAY|DateUtils.FORMAT_ABBREV_ALL);
                        tvTicketDateFrom.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" "+weekDay);

                    }
                },oldYear,oldMonthOfYear,oldDayOfMouth).show();
            }
        });
        btnTicketQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录查询历史
                HistoryHelper helper=new HistoryHelper(getActivity(),"history.db",null,1);
                SQLiteDatabase db=helper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("rec",tvTicketStationFrom.getText().toString()+
                "-"+tvTicketStationTo.getText().toString());
                db.insert("history",null,values);
                db.close();
                helper.close();
                Intent intent=new Intent();
                intent.setClass(getActivity(),TicketStep1Activity.class);
                //传值
                intent.putExtra("fromStationName",tvTicketStationFrom.getText().toString());
                intent.putExtra("toStationName",tvTicketStationTo.getText().toString());
                intent.putExtra("startTrainDate", tvTicketDateFrom.getText().toString());
                startActivity(intent);
            }
        });
        tvTicketHistory1.setOnClickListener(new HistoryListener());
        tvTicketHistory2.setOnClickListener(new HistoryListener());
    }
   class HistoryHelper extends SQLiteOpenHelper{

       public HistoryHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
           super(context, name, factory, version);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {
           db.execSQL("create table history(_id integer primary key,rec text)   ");

       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       }
   }

    @Override
    public void onResume() {
        super.onResume();
        //查询历史记录
        HistoryHelper helper=new HistoryHelper(getActivity(),"history.db",null,1);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.query("history",null,null,null,null,null,"_id desc","2");
        if (cursor.moveToNext()){
            tvTicketHistory1.setText(cursor.getString(1));
        }
        if (cursor.moveToNext()){
            tvTicketHistory2.setText(cursor.getString(1));
        }
        cursor.close();
        db.close();
        helper.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name=data.getStringExtra("name");
        if (!TextUtils.isEmpty(name)){
            switch (requestCode){
                case 100:
                    tvTicketStationFrom.setText(name);
                    break;
                case 101:
                    tvTicketStationTo.setText(name);
                    break;
            }
        }
    }

    private class HistoryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String s="";
            switch (v.getId()){
                case R.id.tvTicketHistory1:
                    s=tvTicketHistory1.getText().toString();
                    break;
                case R.id.tvTicketHistory2:
                    s=tvTicketHistory2.getText().toString();
                    break;
            }
            if (!TextUtils.isEmpty(s)){
                tvTicketStationFrom.setText(s.split("-")[0]);
                tvTicketStationTo.setText(s.split("-")[1]);

            }
        }
    }
}
