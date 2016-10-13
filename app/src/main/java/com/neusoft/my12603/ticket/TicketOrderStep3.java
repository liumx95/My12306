package com.neusoft.my12603.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.neusoft.my12603.MainActivity;
import com.neusoft.my12603.R;

/**
 * Created by star on 2016/9/19.
 */
public class TicketOrderStep3 extends Activity{

    TextView tvTicketOrderSuccessStep4Back;
    TextView tvTicketOrderSuccessStep4Pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderstep3);
        initView();
        initData();
        initControl();
    }

    private void initView() {
        tvTicketOrderSuccessStep4Back = (TextView) findViewById(R.id.tvTicketOrderSuccessStep4Back);
        tvTicketOrderSuccessStep4Pay = (TextView) findViewById(R.id.tvTicketOrderSuccessStep4Pay);
    }

    private void initData() {
    }

    private void initControl() {
        tvTicketOrderSuccessStep4Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(TicketOrderStep3.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        tvTicketOrderSuccessStep4Pay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 跳转到下一步
                Intent intent = new Intent();
                intent.setClass(TicketOrderStep3.this, TicketPaySuccess.class);
                startActivity(intent);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClass(TicketOrderStep3.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_order_success_step4, menu);
        return true;
    }
}
