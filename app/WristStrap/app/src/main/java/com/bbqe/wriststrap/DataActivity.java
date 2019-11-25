package com.bbqe.wriststrap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DataActivity extends AppCompatActivity {

    private TextView heart;
    private TextView step;
    private TextView temperature;
    private Handler mHandler;
    private Thread thread;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        mContext = this;
        heart = findViewById(R.id.heart);
        step = findViewById(R.id.step);
        temperature = findViewById(R.id.temperature);
        TextView currentMachine = findViewById(R.id.current_machine);
        currentMachine.setText(getIntent().getStringExtra("name"));
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.show(mContext);
            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                refreshData();
            }
        };


    }

    @SuppressLint("SetTextI18n")
    private void refreshData() {
        heart.setText(MyDeviceData.getInstance().getHeart() + "");
        step.setText(MyDeviceData.getInstance().getStep() + "");
        temperature.setText(MyDeviceData.getInstance().getTemperature() + "");
    }


    public static void show(Context context, String name) {
        Intent intent = new Intent(context, DataActivity.class);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    mHandler.sendEmptyMessage(1);
                    try {
                        sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
    }
}
