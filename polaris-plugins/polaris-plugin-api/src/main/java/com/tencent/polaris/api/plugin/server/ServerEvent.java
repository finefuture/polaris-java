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

package com.tencent.polaris.api.plugin.server;


import com.tencent.polaris.api.exception.PolarisException;
import com.tencent.polaris.api.pojo.ServiceEventKey;

import java.util.Optional;

import static com.tencent.polaris.api.config.plugin.DefaultPlugins.SERVER_CONNECTOR_GRPC;

/**
 * 服务变更事件
 *
 * @author andrewshan, Haotian Zhang
 */
public class ServerEvent {

    /**
     * 获取服务标识
     */
    private final ServiceEventKey serviceEventKey;
    /**
     * 获取错误信息，只有当出错的时候才返回
     */
    private PolarisException error;
    /**
     * 获取泛型的值
     */
    private Object value;
    /**
     * Polaris的版本号
     * <p>
     * 如果修改了value中的版本号，那么将原来Polaris的版本号保存在这里,
     * 主要用于 CompositeServiceUpdateTask。
     */
    private Optional<String> polarisRevision = Optional.empty();

    private String connectorType;

    public ServerEvent(ServiceEventKey serviceEventKey, Object value, PolarisException error) {
        this(serviceEventKey, value, error, SERVER_CONNECTOR_GRPC);
    }

    public ServerEvent(ServiceEventKey serviceEventKey, Object value, PolarisException error, String connectorType) {
        this.serviceEventKey = serviceEventKey;
        this.value = value;
        this.error = error;
        this.connectorType = connectorType;
    }

    public ServiceEventKey getServiceEventKey() {
        return serviceEventKey;
    }

    public Object getValue() {
        return value;
    }

    public Optional<String> getPolarisRevision() {
        return polarisRevision;
    }

    public void setPolarisRevision(String revision) {
        polarisRevision = Optional.ofNullable(revision);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public PolarisException getError() {
        return error;
    }

    public void setError(PolarisException error) {
        this.error = error;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }
}
