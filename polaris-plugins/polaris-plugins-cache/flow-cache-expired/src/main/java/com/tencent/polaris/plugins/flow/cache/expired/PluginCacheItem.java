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

package com.tencent.polaris.plugins.flow.cache.expired;

import java.util.concurrent.atomic.AtomicLong;

public class PluginCacheItem {

    private final Object key;

    private final Object value;

    private final AtomicLong lastAccessTime = new AtomicLong();

    public PluginCacheItem(Object key, Object value) {
        this.key = key;
        this.value = value;
        lastAccessTime.set(System.currentTimeMillis());
    }

    public Object getValue() {
        lastAccessTime.set(System.currentTimeMillis());
        return value;
    }

    public Object getKey() {
        return key;
    }

    public long getLastAccessTime() {
        return lastAccessTime.get();
    }
}
