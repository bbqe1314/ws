package com.dt.wriststrap.service;

import com.dt.wriststrap.bean.Response;
import com.dt.wriststrap.bean.WristStrapBean;

import java.util.List;

public interface WSService {

    Response addWristStrap(WristStrapBean wristStrapBean);
    Response queryWristStrap();
}
