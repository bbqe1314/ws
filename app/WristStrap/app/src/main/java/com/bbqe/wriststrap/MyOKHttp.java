package com.bbqe.wriststrap;



import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOKHttp {
    public static int PORT = 8080;
    public static String URL = "http://[ipv6]:" + PORT + "/ws/add";

    private static MyOKHttp mMyOKHttp;
    private OkHttpClient mClient;

    private MyOKHttp() {
        mClient = new OkHttpClient();
    }

    public static MyOKHttp getInstance() {
        if (mMyOKHttp == null)
            mMyOKHttp = new MyOKHttp();
        return mMyOKHttp;
    }

    public void post(String time, float temperature, int step, int heartBeat) {

        String json = "{\n" +
                "\t\"time\":\"" + time + "\",\n" +
                "\t\"heartBeat\":\"" + heartBeat + "\",\n" +
                "\t\"step\":\"" + step + "\",\n" +
                "\t\"temperature\":\"" + temperature + "\"\n" +
                "}";
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);


        final Request request = new Request.Builder()//创建Request 对象。
                .url(URL)
                .post(requestBody)
                .build();


        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("哈哈哈 : " + response.message());

            }
        });
    }

}


