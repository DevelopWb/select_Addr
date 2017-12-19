package com.choseaddrdemo.selectAddr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.choseaddrdemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Hello World!
     */
    private TextView mSelectAddr;
    private String cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registBroadcast();
    }

    private void initView() {
        mSelectAddr = (TextView) findViewById(R.id.select_addr);
        mSelectAddr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_addr:
                ChooserActivity.start(MainActivity.this, null);
                break;
        }
    }
    /**
     * 注册处理选择地址的广播
     */
    private void registBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ChooserActivity.ACTION);
        registerReceiver(echoRegionReceiver, intentFilter);
    }

    private BroadcastReceiver echoRegionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ChooserActivity.ACTION)) {
                String region = intent.getStringExtra(ChooserActivity.ART_ADDRESS);
                cityId = intent.getStringExtra(ChooserActivity.ART_CITYINFO);
                mSelectAddr.setText(region);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(echoRegionReceiver);
    }
}
