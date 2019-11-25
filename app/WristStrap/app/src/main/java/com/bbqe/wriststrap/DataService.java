package com.bbqe.wriststrap;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import com.us.ble.central.BLEDevice;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataService extends Service {

    private BLEDevice mDevice;
    private static final String DATE_STYLE = "yy-MM-dd HH:mm:ss";
    private Handler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LYQ", "服务已开启");
        mDevice = MainActivity.device;
        new Thread() {
            @Override
            public void run() {

                while (true) {

                    if (mDevice.isConnected()) {
                        byte[] request = {0x01};
                        int length = request.length;
                        //实时步数
                        mDevice.write(length, 0x31, request);
                        //实时体温
                        mDevice.write(length, 0x44, request);
                        //实时心跳
                        mDevice.write(length, 0x41, request);
                    }
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_STYLE);
                    String time = sdf.format(new Date());

                    MyOKHttp.getInstance().post(
                            time,
                            MyDeviceData.getInstance().getTemperature(),
                            MyDeviceData.getInstance().getStep(),
                            MyDeviceData.getInstance().getHeart()
                    );

                    try {
                        Thread.sleep(3 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();

        return START_STICKY;
    }
}
