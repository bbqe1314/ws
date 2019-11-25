package com.bbqe.wriststrap;

public class MyDeviceData {

    private String name;
    private String address;
    private int heart = 0;//心跳
    private float temperature = 0;//温度
    private int step = 0;
    private static MyDeviceData instance;

    private MyDeviceData() {
    }

    public static MyDeviceData getInstance() {
        if (instance == null)
            instance = new MyDeviceData();
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "MyDeviceData{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", heart=" + heart +
                ", temperature=" + temperature +
                ", step=" + step +
                '}';
    }
}
