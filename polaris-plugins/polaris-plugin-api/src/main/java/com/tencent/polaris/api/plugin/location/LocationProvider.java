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

package com.tencent.polaris.api.plugin.location;

import com.tencent.polaris.api.plugin.Plugin;
import com.tencent.polaris.specification.api.v1.model.ModelProto;

/**
 * @author <a href="mailto:liaochuntao@live.com">liaochuntao</a>
 */
public interface LocationProvider extends Plugin {

    enum ProviderType {

        LOCAL("local", 0), REMOTE_HTTP("remoteHttp", 1), REMOTE_SERVICE("remoteService", 2);

        final String name;

        final int priority;

        ProviderType(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        public String getName() {
            return name;
        }

        public int getPriority() {
            return priority;
        }
    }

    ModelProto.Location getLocation();

    ProviderType getProviderType();

}
