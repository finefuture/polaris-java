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

package com.tencent.polaris.circuitbreak.api;

import com.tencent.polaris.api.plugin.circuitbreaker.ResourceStat;
import com.tencent.polaris.api.plugin.circuitbreaker.entity.Resource;
import com.tencent.polaris.circuitbreak.api.pojo.CheckResult;
import com.tencent.polaris.circuitbreak.api.pojo.FunctionalDecoratorRequest;
import com.tencent.polaris.circuitbreak.api.pojo.InvokeContext;

import java.io.Closeable;

public interface CircuitBreakAPI extends AutoCloseable, Closeable {

    /**
     * check and acquire circuitbreaker
     *
     * @param resource
     * @return pass or not, and fallback config if needed
     */
    CheckResult check(Resource resource);

    /**
     * report the resource invoke result
     *
     * @param reportStat
     */
    void report(ResourceStat reportStat);


    /**
     * make the function decorator
     *
     * @param functionalDecoratorRequest
     * @return decorator
     */
    FunctionalDecorator makeFunctionalDecorator(FunctionalDecoratorRequest functionalDecoratorRequest);

    /**
     * make the invoke handler
     *
     * @param requestContext
     * @return InvokeHandler
     */
    InvokeHandler makeInvokeHandler(InvokeContext.RequestContext requestContext);

    /**
     * 清理并释放资源
     */
    void destroy();

    @Override
    default void close() {
        destroy();
    }
}
