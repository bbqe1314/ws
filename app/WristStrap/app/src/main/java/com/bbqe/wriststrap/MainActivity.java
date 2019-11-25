package com.bbqe.wriststrap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.us.ble.central.BLEDevice;
import com.us.ble.central.BLEManager;
import com.us.ble.listener.BLEDeviceListener;
import com.us.ble.listener.RealtimeDataListener;
import com.us.ble.listener.ScanListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mStart;
    private Button mStop;
    private Button mLink;
    private Button mShowData;

    private ListView mDevice;
    private Context mContext;

    private BLEManager mBLEManager;

    private List<MyDevice> mDevices = new ArrayList<>();
    private BaseAdapter mAdapter;
    public static int currentPosition = -1;

    private static String currentMachine;

    public static BLEDevice device;

    private Handler mHandler;

    private static boolean isFirst = true;

    private ProgressBar mProgressBar;


    private static void L(String str) {
        Log.i("LYQ", str);
    }

    private void T(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    @SuppressLint("HandlerLeak")
    private void init() {
        mContext = this;
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mShowData.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                mStop.setEnabled(true);
                mLink.setEnabled(true);
                Intent intent = new Intent(mContext, DataService.class);
                startService(intent);
                isFirst = false;
            }
        };
        mStart = findViewById(R.id.start);
        mStop = findViewById(R.id.stop);
        mDevice = findViewById(R.id.device);
        mLink = findViewById(R.id.link);
        mShowData = findViewById(R.id.show);
        mBLEManager = new BLEManager(this);
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return mDevices == null ? 0 : mDevices.size();
            }

            @Override
            public Object getItem(int position) {
                return mDevices.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @SuppressLint("ViewHolder")
            @Override
            public View getView(final int position, View itemView, ViewGroup parent) {
                itemView = LayoutInflater.from(mContext).inflate(R.layout.device_item, parent, false);
                TextView name = itemView.findViewById(R.id.device_name);
                TextView address = itemView.findViewById(R.id.device_address);
                name.setText(mDevices.get(position).getName());
                address.setText(mDevices.get(position).getAddress());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = position;
                        currentMachine = mDevices.get(currentPosition).getName();
                        T("您选择了" +
                                currentMachine +
                                "设备");
                    }
                });
                return itemView;
            }
        };

        mDevice.setAdapter(mAdapter);
        mStop.setOnClickListener(this);
        mStart.setOnClickListener(this);
        mLink.setOnClickListener(this);
        mShowData.setOnClickListener(this);

        mLink.setEnabled(false);

        mShowData.setVisibility(View.INVISIBLE);

        mProgressBar = findViewById(R.id.progress);
        mProgressBar.setVisibility(View.INVISIBLE);


        mBLEManager.setScanListener(new ScanListener() {
            @Override
            public void onScanResult(int result, BLEDevice device, int i, byte[] bytes, String s) {
                //   L("扫描结果 : " + result + " " + (device == null));
                if (result == 0) {
                    if ((null != device.getName()) && (null != device.getAddress())) {
                        L("名称 : " + device.getName() + "   地址 : " + device.getAddress());
                        MyDevice temp = new MyDevice(device.getName(), device.getAddress(), false);
                        if (!isContains(temp))
                            mDevices.add(temp);
                    }
                } else {
                    mAdapter.notifyDataSetChanged();
                    T("扫描结束");
                    mStop.setEnabled(true);
                    mLink.setEnabled(true);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }

    public boolean isContains(MyDevice myDevice) {
        for (MyDevice device : mDevices)
            if (device.isContains(myDevice))
                return true;
        return false;
    }

    public static int getSecondGap(Date fromDate, Date toDate) throws ParseException {  // 传递的参数计算到了秒钟
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromTime = sdf.format(fromDate);
        String toTime = sdf.format(toDate);

        // 注意：hh:12小时制度，HH:小时制
        Date fromM = sdf.parse(fromTime.substring(0, 19));    // 截取到分钟
        Date toM = sdf.parse(toTime.substring(0, 19));

        long from = fromM.getTime();   // getTime()返回到毫秒
        long to = toM.getTime();
        int gap = (int) ((to - from) / 1000);
        return gap;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start: {
                mProgressBar.setVisibility(View.VISIBLE);
                T("开始搜索,请耐心等待5秒钟");
                mBLEManager.startScan(5);
            }
            break;
            case R.id.stop:
                mBLEManager.stopScan();
                //mProgressBar.setVisibility(View.INVISIBLE);
                mAdapter.notifyDataSetChanged();
                T("停止搜索");
                break;
            case R.id.link:
                if (currentPosition == -1)
                    T("您还未选择设备");
                else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    String address = mDevices.get(currentPosition).getAddress();
                    device = new BLEDevice(address, MainActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "正在连接请耐心等待, 请勿关闭设备或进行其他操作",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    device.setRealtimeDataListener(new RealtimeDataListener() {
                        @Override
                        public void onRealtimeSports(String s, int step, int distance, int calorie) {
                            MyDeviceData.getInstance().setStep(step);
                        }

                        @Override
                        public void onRealtimeHearts(String s, int heart) {
                            MyDeviceData.getInstance().setHeart(heart);
                        }

                        @Override
                        public void onRealtimeTemperature(String s, float temperature) {
                            MyDeviceData.getInstance().setTemperature(temperature);
                        }

                        @Override
                        public void onRealtimePressure(String s, float v, float v1, float v2) {

                        }

                        @Override
                        public void onRecentSleep(String s, int[] ints, int[] ints1, int[] ints2) {

                        }

                        @Override
                        public void onRealLocationAction(String s, int i, int i1) {

                        }

                        @Override
                        public void onRealElectricity(String s, int electricity) {
                        }

                        @Override
                        public void onRealAllHealthData(String s, int i, int i1, int i2, int i3, int i4) {

                        }

                        @Override
                        public void onRealRawHearrate(String s, byte[] bytes) {

                        }

                        @Override
                        public void onRealRawAcceleration(String s, int i, int i1, int i2) {

                        }

                        @Override
                        public void onRealRawEulerangles(String s, int i, int i1, int i2, int i3) {

                        }

                        @Override
                        public void onRealRawHearRatePeak(String s, String s1) {

                        }

                        @Override
                        public void onRawHearRatePeakPointer(String s, byte[] bytes) {

                        }
                    });

                    device.setBLEDeviceListener(new BLEDeviceListener() {
                        @Override
                        public void updateRssi(String s, int i) {
                        }

                        @Override
                        public void onConnected(String s) {
                            T("连接成功");
                            mHandler.sendEmptyMessage(1);
                        }

                        @Override
                        public void onDisConnected(String s) {
                            L("失败原因 ：" + s);
                            device.connect();
                            T("连接失败,尝试重新连接ing...");
                        }
                    });

                    L("current Thread " + Thread.currentThread().getName());
                    device.connect();

                }
                break;
            case R.id.show:
                byte[] ti = nowTimeToBytes();
                device.write(ti.length, 0x01, ti);
                DataActivity.show(MainActivity.this, currentMachine);
                break;
        }
    }

    byte[] nowTimeToBytes() {
        byte[] result = new byte[4];
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 2016;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        result[0] = (byte) ((year << 2) | (month >> 2 & 0b11));
        result[1] = (byte) (((month & 0b0011) << 6) | (day << 1) | (hour >> 4 & 0b1));
        result[2] = (byte) (((hour & 0b01111) << 4) | (minute >> 2 & 0b1111));
        result[3] = (byte) (((minute & 0b000011) << 6) | second);
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            mShowData.setVisibility(View.VISIBLE);
        }
        mStop.setEnabled(false);
        mLink.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        device.disconnect();
        super.onDestroy();

    }
}
