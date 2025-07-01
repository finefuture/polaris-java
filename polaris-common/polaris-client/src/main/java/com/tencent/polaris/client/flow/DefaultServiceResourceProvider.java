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

import com.tencent.polaris.api.plugin.compose.Extensions;
import com.tencent.polaris.api.pojo.DefaultServiceEventKeysProvider;
import com.tencent.polaris.api.pojo.ServiceEventKey;
import com.tencent.polaris.api.pojo.ServiceEventKey.EventType;
import com.tencent.polaris.api.pojo.ServiceInstances;
import com.tencent.polaris.api.pojo.ServiceKey;
import com.tencent.polaris.api.pojo.ServiceResourceProvider;
import com.tencent.polaris.api.pojo.ServiceRule;

public class DefaultServiceResourceProvider implements ServiceResourceProvider {

    private final Extensions extensions;

    public DefaultServiceResourceProvider(Extensions extensions) {
        this.extensions = extensions;
    }

    @Override
    public ServiceRule getServiceRule(ServiceEventKey serviceEventKey) {
        DefaultServiceEventKeysProvider getResourcesRequest = new DefaultServiceEventKeysProvider();
        getResourcesRequest.setSvcEventKey(serviceEventKey);
        getResourcesRequest.setUseCache(true);
        DefaultFlowControlParam flowControlParam = new DefaultFlowControlParam(
                extensions.getConfiguration().getGlobal().getAPI());
        ResourcesResponse resourcesResponse = BaseFlow
                .syncGetResources(extensions, true, getResourcesRequest,
                        flowControlParam);
        return resourcesResponse.getServiceRule(serviceEventKey);
    }

    @Override
    public ServiceInstances getServiceInstances(ServiceKey serviceKey) {
        ServiceEventKey serviceEventKey = new ServiceEventKey(serviceKey, EventType.INSTANCE);
        DefaultServiceEventKeysProvider getResourcesRequest = new DefaultServiceEventKeysProvider();
        getResourcesRequest.setSvcEventKey(serviceEventKey);
        getResourcesRequest.setUseCache(true);
        DefaultFlowControlParam flowControlParam = new DefaultFlowControlParam(
                extensions.getConfiguration().getGlobal().getAPI());
        ResourcesResponse resourcesResponse = BaseFlow
                .syncGetResources(extensions, true, getResourcesRequest,
                        flowControlParam);
        return resourcesResponse.getServiceInstances(serviceEventKey);
    }
}
