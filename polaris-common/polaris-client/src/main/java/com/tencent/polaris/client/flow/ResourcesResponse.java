/*
 * Tencent is pleased to support the open source community by making polaris-java available.
 *
 * Copyright (C) 2021 Tencent. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.polaris.client.flow;

import com.tencent.polaris.api.pojo.ServiceEventKey;
import com.tencent.polaris.api.pojo.ServiceInstances;
import com.tencent.polaris.api.pojo.ServiceRule;
import com.tencent.polaris.api.pojo.Services;
import com.tencent.polaris.client.pojo.ServiceInstancesByProto;
import com.tencent.polaris.client.pojo.ServiceRuleByProto;
import com.tencent.polaris.client.pojo.ServicesByProto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Response for resources query request.
 *
 * @author andrewshan
 */
public class ResourcesResponse {

    private final Map<ServiceEventKey, Services> servicesMap = new ConcurrentHashMap<>();

    private final Map<ServiceEventKey, ServiceInstances> instancesMap = new ConcurrentHashMap<>();

    private final Map<ServiceEventKey, ServiceRule> rules = new ConcurrentHashMap<>();

    private final Map<ServiceEventKey, Throwable> errors = new ConcurrentHashMap<>();

    /**
     * 添加服务实例应答
     *
     * @param svcEventKey 服务标识
     * @param instances 实例列表
     */
    public void addServiceInstances(ServiceEventKey svcEventKey, ServiceInstances instances) {
        instancesMap.put(svcEventKey, instances);
    }

    /**
     * 添加服务应答
     *
     * @param svcEventKey 服务标识
     * @param services 服务列表
     */
    public void addServices(ServiceEventKey svcEventKey, Services services) {
        servicesMap.put(svcEventKey, services);
    }

    /**
     * 获取服务实例应答对象
     *
     * @param svcEventKey 服务标识
     * @return ServiceRuleResponse
     */
    public ServiceInstances getServiceInstances(ServiceEventKey svcEventKey) {
        ServiceInstances serviceInstances= instancesMap.get(svcEventKey);
        if (null == serviceInstances) {
            serviceInstances = ServiceInstancesByProto.EMPTY_INSTANCES;
        }
        return serviceInstances;
    }

    /**
     * 获取所有的实例应答缓存
     *
     * @return services
     */
    public Map<ServiceEventKey, ServiceInstances> getAllServiceInstances() {
        return instancesMap;
    }

    /**
     * 获取所有的规则缓应答缓存
     *
     * @return rules
     */
    public Map<ServiceEventKey, ServiceRule> getAllServiceRules() {
        return rules;
    }

    /**
     * 添加规则应答
     *
     * @param svcEventKey 服务标识
     * @param rule 规则数据
     */
    public void addServiceRule(ServiceEventKey svcEventKey, ServiceRule rule) {
        rules.put(svcEventKey, rule);
    }

    /**
     * 获取规则应答对象
     *
     * @param svcEventKey 服务标识
     * @return ServiceRuleResponse
     */
    public ServiceRule getServiceRule(ServiceEventKey svcEventKey) {
        ServiceRule serviceRule = rules.get(svcEventKey);
        if (serviceRule == null) {
            serviceRule = ServiceRuleByProto.EMPTY_SERVICE_RULE;
        }
        return serviceRule;
    }

    /**
     * 获取服务列表应答对象
     *
     * @param svcEventKey 服务标识
     * @return Services 服务列表
     */
    public Services getServices(ServiceEventKey svcEventKey) {
        Services services = servicesMap.get(svcEventKey);
        if (services == null) {
            services = ServicesByProto.EMPTY_SERVICES;
        }
        return services;
    }

    /**
     * 添加错误信息
     *
     * @param svcEventKey 服务标识
     * @param error 异常信息
     */
    public void addError(ServiceEventKey svcEventKey, Throwable error) {
        errors.put(svcEventKey, error);
    }

    /**
     * 返回所有的错误
     *
     * @return 错我列表
     */
    public Map<ServiceEventKey, Throwable> getErrors() {
        return errors;
    }
}
