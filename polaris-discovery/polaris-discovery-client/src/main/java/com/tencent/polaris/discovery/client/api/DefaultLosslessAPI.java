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

package com.tencent.polaris.discovery.client.api;

import com.tencent.polaris.api.core.LosslessAPI;
import com.tencent.polaris.api.exception.PolarisException;
import com.tencent.polaris.api.flow.DiscoveryFlow;
import com.tencent.polaris.api.plugin.lossless.LosslessActionProvider;
import com.tencent.polaris.api.pojo.BaseInstance;
import com.tencent.polaris.client.api.BaseEngine;
import com.tencent.polaris.client.api.SDKContext;
import com.tencent.polaris.discovery.client.util.Validator;
import com.tencent.polaris.logging.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultLosslessAPI extends BaseEngine implements LosslessAPI {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultLosslessAPI.class);

    private DiscoveryFlow discoveryFlow;

    public DefaultLosslessAPI(SDKContext sdkContext) {
        super(sdkContext);
    }

    @Override
    protected void subInit() throws PolarisException {
        discoveryFlow = sdkContext.getOrInitFlow(DiscoveryFlow.class);
        Map<BaseInstance, LosslessActionProvider> actionProviders = new ConcurrentHashMap<>();
        sdkContext.getValueContext().setValue(LosslessActionProvider.CTX_KEY, actionProviders);
    }

    @Override
    public void setLosslessActionProvider(BaseInstance instance, LosslessActionProvider losslessActionProvider) {
        checkAvailable("LosslessAPI");
        Validator.validateNotNull(losslessActionProvider, "losslessActionProvider");
        Map<BaseInstance, LosslessActionProvider> actionProviders = sdkContext.getValueContext().getValue(LosslessActionProvider.CTX_KEY);
        actionProviders.put(instance, losslessActionProvider);
        LOG.info("[LosslessAPI] losslessActionProvider updated, key {}, name is {}, instance {}",
                LosslessActionProvider.CTX_KEY, losslessActionProvider.getName(), instance);
    }

    @Override
    public void losslessRegister(BaseInstance instance) {
        checkAvailable("LosslessAPI");
        discoveryFlow.losslessRegister(instance);
    }

    @Override
    public void losslessDeRegister(BaseInstance instance) {
        discoveryFlow.losslessDeregister(instance);
    }

}
