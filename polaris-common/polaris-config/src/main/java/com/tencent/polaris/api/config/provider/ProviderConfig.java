/*
 * Tencent is pleased to support the open source community by making Polaris available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
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

package com.tencent.polaris.api.config.provider;

import com.tencent.polaris.api.config.verify.Verifier;
import java.util.List;
import java.util.Map;

/**
 * 被调端配置对象
 *
 * @author andrewshan, Haotian Zhang
 */
public interface ProviderConfig extends Verifier {

    /**
     * Configuration of prefix of "provider.registers".
     *
     * @return List of {@link RegisterConfig}
     */
    List<? extends RegisterConfig> getRegisters();

    /**
     * Configuration of prefix of "provider.registers".
     *
     * @return Map of {@link RegisterConfig}
     */
    Map<String, ? extends RegisterConfig> getRegisterConfigMap();

    /**
     * 获取限流配置
     *
     * @return 配置
     */
    RateLimitConfig getRateLimit();

    /**
     * get the current service config
     *
     * @return service
     */
    ServiceConfig getService();

    /**
     * Get minimal register interval millisecond
     *
     * @return minimal register interval millisecond
     */
    long getMinRegisterInterval();

    /**
     * 获取负责实例心跳上报线程的数量
     *
     * @return run heartbeat thread size
     */
    int getHeartbeatWorkerSize();

    /**
     * get the lossless configuration
     * @return configuration
     */
    LosslessConfig getLossless();
}
