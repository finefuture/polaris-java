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

package com.tencent.polaris.api.config.provider;

import com.tencent.polaris.api.config.verify.Verifier;

import java.util.List;

public interface AuthConfig extends Verifier {

    /**
     * 是否启用服务鉴权
     * provider.auth.enable
     *
     * @return 启用鉴权
     */
    boolean isEnable();

    /**
     * 服务鉴权插件链配置
     * provider.auth.chain
     *
     * @return 鉴权插件链列表
     */
    List<String> getChain();
}
