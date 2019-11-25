package com.bbqe.wriststrap;

public class MyDevice {
    private String name;
    private String address;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public MyDevice(String name, String address,boolean checked) {
        this.name = name;
        this.address = address;
        this.checked = checked;
    }

    public boolean isContains(MyDevice device) {
        return (this.name.equals(device.getName())) && (this.address.equals(device.getAddress()));
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
}
