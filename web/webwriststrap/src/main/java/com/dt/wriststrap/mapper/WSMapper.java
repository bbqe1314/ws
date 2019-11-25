package com.dt.wriststrap.mapper;

import com.dt.wriststrap.bean.WristStrapBean;

import java.util.List;

public interface WSMapper {

    void addWristStrap(WristStrapBean wristStrapBean);
    List<WristStrapBean> queryWristStrap();
}
