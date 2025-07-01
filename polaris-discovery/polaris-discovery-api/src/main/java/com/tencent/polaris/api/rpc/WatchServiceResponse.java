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

/**
 * 服务事件变更请求响应
 *
 * @author <a href="mailto:liaochuntao@live.com">liaochuntao</a>
 */
public class WatchServiceResponse extends BaseEntity {

    /**
     * 发起 watch 请求时，会查询一下当前服务下的实例列表信息
     */
    private final InstancesResponse response;

    /**
     * 添加 List<ServiceListener> 是否成功
     */
    private final boolean success;

    public WatchServiceResponse(InstancesResponse response, boolean result) {
        this.response = response;
        this.success = result;
    }

    public InstancesResponse getResponse() {
        return response;
    }

    public boolean isSuccess() {
        return success;
    }
}
