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

package com.tencent.polaris.api.plugin;

import com.sun.net.httpserver.HttpHandler;

import java.util.Map;

/**
 *  plugin who want to expose port should implement this interface
 */
public interface HttpServerAware {

    /**
     * get host to listen to this plugin
     * @return host
     */
    String getHost();

    /**
     * get port to listen in this plugin
     * @return port
     */
    int getPort();

    /**
     * get the http server handlers
     * @return handlers
     */
    Map<String, HttpHandler> getHandlers();

    /**
     * 在端口冲突时允许端口漂移
     * @return boolean
     */
    boolean allowPortDrift();
}
