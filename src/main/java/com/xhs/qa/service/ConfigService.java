package com.xhs.qa.service;

import com.xhs.qa.model.CommonResponse;
import com.xhs.qa.model.ServiceData;

import java.util.List;

/**
 * Created on 18/3/12 15:09
 *
 * @author sunyumei
 */
public interface ConfigService {
    CommonResponse<List<ServiceData>> getConfigByServiceName(String serviceName);

    CommonResponse<List<ServiceData>> addOneConfig(ServiceData request);

    CommonResponse<List<ServiceData>> listAllByServiceName();
}
