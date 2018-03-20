package com.xhs.qa.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xhs.qa.model.CommonResponse;
import com.xhs.qa.model.NodeData;
import com.xhs.qa.model.ServiceData;
import com.xhs.qa.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 18/3/12 15:10
 *
 * @author sunyumei
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    private static final String BASE_PATH = "/";
    private final CuratorFramework curatorFramework;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TreeCache treeCache;

    @Autowired
    public ConfigServiceImpl(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @PostConstruct
    private void init() throws Exception {
        try {
            curatorFramework.create().creatingParentsIfNeeded().forPath(BASE_PATH);
        } catch (KeeperException.NodeExistsException ex) {
        }
        treeCache = new TreeCache(curatorFramework, BASE_PATH);
        treeCache.start();
    }

    @Override
    public CommonResponse<List<ServiceData>> getConfigByServiceName(String serviceName) {
        Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(BASE_PATH + serviceName);
        List<ServiceData> result = new ArrayList<>();
        try {
            if (currentChildren == null) {
                log.error("no such service in zk!");

                return CommonResponse.fail(String.format("%s not in zk!", serviceName));
            }
            for (ChildData child : currentChildren.values()) {
                String config = StringUtils.substringAfterLast(child.getPath(), "/");
                NodeData nodeData = objectMapper.readValue(child.getData(), NodeData.class);
                result.add(new ServiceData(serviceName, config, nodeData.getValue()));
            }
            return CommonResponse.success(result);
        } catch (IOException e) {
            log.error("Fail to read value from zk", e);
            return CommonResponse.fail(e.getMessage());
        }
    }

    @Override
    public CommonResponse<List<ServiceData>> addOneConfig(ServiceData request) {
        String configFullPath = fullPathForConfig(request);

        try {
            curatorFramework.create().creatingParentContainersIfNeeded()
                    .forPath(configFullPath, objectMapper.writeValueAsBytes(new NodeData(request.getValue())));
            List<ServiceData> result = getAllConfigInformation(request.getService());
            result.add(request);
            return CommonResponse.success(result);
        } catch (KeeperException.NodeExistsException ex) {
            return updateOneConfig(request);
        } catch (Exception e) {
            log.error("Fail to add config", e);
            return CommonResponse.fail(e.getMessage());
        }
    }

    private CommonResponse<List<ServiceData>> updateOneConfig(ServiceData request) {
        String configFullPath = fullPathForConfig(request);
        try {
            curatorFramework.setData().forPath(configFullPath,
                    objectMapper.writeValueAsBytes(new NodeData(request.getValue())));
            List<ServiceData> result = getAllConfigInformation(request.getService());
            for (ServiceData aResult : result) {
                if (aResult.getConfig().equals(request.getConfig())) {
                    aResult.setValue(request.getValue());
                }
            }
            return CommonResponse.success(result);
        } catch (Exception e) {
            log.error("Fail to update config", e);
            return CommonResponse.fail(e.getMessage());
        }
    }

    private List<ServiceData> getAllConfigInformation(String serviceName) {
        List<ServiceData> result = new ArrayList<>();
        Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(BASE_PATH + serviceName);
        try {
            for (ChildData child : currentChildren.values()) {
                String config = StringUtils.substringAfterLast(child.getPath(), "/");
                NodeData nodeData = objectMapper.readValue(child.getData(), NodeData.class);
                result.add(new ServiceData(serviceName, config, nodeData.getValue()));
            }
        } catch (Exception e) {
            log.error("get config information error", e);
        }
        return result;
    }

    private String fullPathForConfig(ServiceData request) {
        return String.format("%s%s/%s", BASE_PATH, request.getService(), request.getConfig());
    }

    @Override
    public CommonResponse<List<ServiceData>> listAllByServiceName() {
        Map<String, ChildData> currentChildren = treeCache.getCurrentChildren(BASE_PATH);
        List<String> services = new ArrayList<>();
        List<ServiceData> result = new ArrayList<>();
        for (ChildData child : currentChildren.values()) {
            services.add(StringUtils.substringAfterLast(child.getPath(), "/"));
        }
        for (String serviceName : services) {
            result.addAll(getAllConfigInformation(serviceName));
        }
        return CommonResponse.success(result);
    }
}
