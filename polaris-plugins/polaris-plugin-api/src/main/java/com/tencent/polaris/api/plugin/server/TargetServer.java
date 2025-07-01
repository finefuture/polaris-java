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

import com.tencent.polaris.api.pojo.ServiceKey;

public class TargetServer {

    private final ServiceKey serviceKey;

    private final String host;

    private final int port;

    private final String labels;

    public TargetServer(ServiceKey serviceKey, String host, int port, String labels) {
        this.serviceKey = serviceKey;
        this.host = host;
        this.port = port;
        this.labels = labels;
    }

    public ServiceKey getServiceKey() {
        return serviceKey;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getLabels() {
        return labels;
    }
}
