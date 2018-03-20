package com.xhs.qa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 18/3/12 15:19
 *
 * @author sunyumei
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceData {
    private String service;
    private String config;
    private Boolean value = false;

    public boolean valid() {
        return service != null && config != null;
    }
}