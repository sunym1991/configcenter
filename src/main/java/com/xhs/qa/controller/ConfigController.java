package com.xhs.qa.controller;

import com.xhs.qa.model.CommonResponse;
import com.xhs.qa.model.ServiceData;
import com.xhs.qa.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 18/3/12 15:01
 *
 * @author sunyumei
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class ConfigController {
    private ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/service/{name}", method = RequestMethod.GET)
    public CommonResponse<List<ServiceData>> getConfigByService(@PathVariable String name) {
        return configService.getConfigByServiceName(name);
    }

    @RequestMapping(value = "/service", method = RequestMethod.POST)
    public CommonResponse<List<ServiceData>> addClient(@RequestBody ServiceData request) {
        return configService.addOneConfig(request);
    }

    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public CommonResponse<List<ServiceData>> listAllByServiceName(){
        return configService.listAllByServiceName();
    }

}
