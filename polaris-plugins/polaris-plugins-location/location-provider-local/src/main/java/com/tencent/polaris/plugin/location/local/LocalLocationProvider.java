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

package com.tencent.polaris.plugin.location.local;

import com.google.protobuf.StringValue;
import com.tencent.polaris.api.utils.StringUtils;
import com.tencent.polaris.logging.LoggerFactory;
import com.tencent.polaris.plugin.location.base.BaseLocationProvider;
import com.tencent.polaris.specification.api.v1.model.ModelProto.Location;
import org.slf4j.Logger;

/**
 * @author <a href="mailto:liaochuntao@live.com">liaochuntao</a>
 */
public class LocalLocationProvider extends BaseLocationProvider<BaseLocationProvider.GetOption> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalLocationProvider.class);

    public LocalLocationProvider() {
        super(GetOption.class);
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.LOCAL;
    }

    @Override
    public Location doGet(GetOption option) {
        String region = StringUtils.defaultString(option.getRegion());
        String zone = StringUtils.defaultString(option.getZone());
        String campus = StringUtils.defaultString(option.getCampus());

        if (StringUtils.isAllEmpty(region, zone, campus)) {
            return null;
        }

        return Location.newBuilder()
                .setRegion(StringValue.newBuilder().setValue(region).build())
                .setZone(StringValue.newBuilder().setValue(zone).build())
                .setCampus(StringValue.newBuilder().setValue(campus).build())
                .build();
    }

}
