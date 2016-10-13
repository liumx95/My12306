package com.neusoft.my12603.ticket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.neusoft.my12603.MainActivity;
import com.neusoft.my12603.R;

/**
 * Created by star on 2016/9/19.
 */
public class TicketPaySuccess extends Activity{
    Button btnTicketPaySuccessStep5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);

        btnTicketPaySuccessStep5 = (Button) findViewById(R.id.btnTicketPaySuccessStep5);

        btnTicketPaySuccessStep5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(TicketPaySuccess.this,
                        MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ticket_pay_success_step5, menu);
        return true;
    }

}
