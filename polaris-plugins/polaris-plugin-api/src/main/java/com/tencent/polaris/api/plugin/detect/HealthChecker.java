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

package com.tencent.polaris.api.plugin.detect;

import com.google.protobuf.Message;
import com.tencent.polaris.api.exception.PolarisException;
import com.tencent.polaris.api.plugin.Plugin;
import com.tencent.polaris.api.pojo.DetectResult;
import com.tencent.polaris.api.pojo.Instance;
import com.tencent.polaris.specification.api.v1.fault.tolerance.FaultDetectorProto.FaultDetectRule;

/**
 * 【扩展点接口】主动健康探测策略
 *
 * @author andrewshan
 * @date 2019/8/21
 */
public interface HealthChecker extends Plugin {

    int DEFAULT_TIMEOUT_MILLI = 1000;

    /**
     * 对单个实例进行探测，返回探测结果
     * 每个探测方法自己去判断当前周期是否需要探测，如果无需探测，则返回nil
     *
     * @param instance 单个服务实例
     * @param faultDetectRule 具体的探测规则，为空则使用SDK默认配置
     * @return 实例探测结果
     * @throws PolarisException 异常信息
     */
    DetectResult detectInstance(Instance instance, FaultDetectRule faultDetectRule) throws PolarisException;
}
