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

package com.tencent.polaris.api.rpc;

import com.tencent.polaris.api.pojo.ServiceEventKey;

/**
 * 服务实例拉取请求
 */
public class GetServiceRuleRequest extends RequestBaseEntity {

    private ServiceEventKey.EventType ruleType;

    public ServiceEventKey.EventType getRuleType() {
        return ruleType;
    }

    public void setRuleType(ServiceEventKey.EventType ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    @SuppressWarnings("checkstyle:all")
    public String toString() {
        return "GetServiceRuleRequest{" +
                "ruleType=" + ruleType +
                '}';
    }
}
