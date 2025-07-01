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

package com.tencent.polaris.router.api.rpc;

import com.tencent.polaris.api.pojo.ServiceInstances;
import java.util.Map;

/**
 * 路由请求处理的应答
 */
public class ProcessRoutersResponse {

    private final ServiceInstances serviceInstances;

    private final Map<String, String> routeLabels;

    public ProcessRoutersResponse(ServiceInstances serviceInstances) {
        this(serviceInstances, null);
    }

    public ProcessRoutersResponse(ServiceInstances serviceInstances, Map<String, String> routeLabels) {
        this.serviceInstances = serviceInstances;
        this.routeLabels = routeLabels;
    }

    public ServiceInstances getServiceInstances() {
        return serviceInstances;
    }

    public Map<String, String> getRouteLabels() {
        return routeLabels;
    }
}
