package com.tencent.polaris.api.plugin.stat;

import com.tencent.polaris.api.pojo.CircuitBreakerStatus;
import com.tencent.polaris.api.pojo.Service;

public class DefaultCircuitBreakResult implements CircuitBreakGauge {
    private String service;
    private String namespace;
    private String method;
    private String subset;
    private String host;
    private int port;
    private String instanceId;
    private Service callerService;
    private CircuitBreakerStatus circuitBreakStatus;

    private String ruleName;

    private String level;

    public void setService(String service) {
        this.service = service;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setCircuitBreakStatus(CircuitBreakerStatus circuitBreakStatus) {
        this.circuitBreakStatus = circuitBreakStatus;
    }

    public void setCallerService(Service callerService) {
        this.callerService = callerService;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setSubset(String subset) {
        this.subset = subset;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getSubset() {
        return subset;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getInstanceId() {
        return instanceId;
    }

    @Override
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public Service getCallerService() {
        return callerService;
    }

    @Override
    public CircuitBreakerStatus getCircuitBreakStatus() {
        return circuitBreakStatus;
    }

    @Override
    public String getLevel() {
        return level;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Override
    public String getService() {
        return service;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }
}
