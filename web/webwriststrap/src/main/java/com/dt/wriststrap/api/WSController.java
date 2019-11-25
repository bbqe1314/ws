package com.dt.wriststrap.api;

import com.dt.wriststrap.bean.Response;
import com.dt.wriststrap.bean.WristStrapBean;
import com.dt.wriststrap.service.WSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WSController {

    @Autowired
    private WSService service;

    @PostMapping("/ws/add")
    public Response addWristStrap(@RequestBody WristStrapBean wristStrapBean) {
        return service.addWristStrap(wristStrapBean);
    }

    @GetMapping("/ws/query")
    public Response queryWristStrap() {
        return service.queryWristStrap();
    }
}
