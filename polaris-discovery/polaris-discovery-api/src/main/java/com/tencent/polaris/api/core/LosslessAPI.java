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

package com.tencent.polaris.api.core;

import com.tencent.polaris.api.plugin.lossless.LosslessActionProvider;
import com.tencent.polaris.api.pojo.BaseInstance;

import java.io.Closeable;

public interface LosslessAPI extends AutoCloseable, Closeable {

    /**
     * 设置无损上下线相关的动作提供器
     * @param  instance 对应需要进行无损上下线的实例，每个实例可以有一个动作管理器
     * @param losslessActionProvider 无损上下线动作提供器
     */
    void setLosslessActionProvider(BaseInstance instance, LosslessActionProvider losslessActionProvider);

    /**
     * 对指定实例实施无损上线
     * @param  instance 对应需要进行无损上线的实例
     */
    void losslessRegister(BaseInstance instance);

    /**
     * 对指定实例实施无损下线
     * @param  instance 对应需要进行无损下线的实例
     */
    void losslessDeRegister(BaseInstance instance);

    void destroy();

    @Override
    default void close() {
        destroy();
    }

}
