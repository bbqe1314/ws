package com.dt.wriststrap.service.impl;

import com.dt.wriststrap.bean.Response;
import com.dt.wriststrap.bean.WristStrapBean;
import com.dt.wriststrap.mapper.WSMapper;
import com.dt.wriststrap.service.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WSServiceImpl implements WSService {
    @Autowired
    private WSMapper mapper;

    @Override
    public Response addWristStrap(WristStrapBean wristStrapBean) {
        mapper.addWristStrap(wristStrapBean);
        return Response.buildOK();
    }

    @Override
    public Response queryWristStrap() {
        return Response.buildOK(mapper.queryWristStrap());
    }
}
