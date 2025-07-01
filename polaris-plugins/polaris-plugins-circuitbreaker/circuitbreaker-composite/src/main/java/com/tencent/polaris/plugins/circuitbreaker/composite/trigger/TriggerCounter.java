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

package com.tencent.polaris.plugins.circuitbreaker.composite.trigger;

import com.tencent.polaris.api.plugin.circuitbreaker.ResourceStat;
import com.tencent.polaris.api.plugin.circuitbreaker.entity.Resource;
import com.tencent.polaris.api.pojo.TrieNode;
import com.tencent.polaris.logging.LoggerFactory;
import com.tencent.polaris.plugins.circuitbreaker.composite.StatusChangeHandler;
import com.tencent.polaris.specification.api.v1.fault.tolerance.CircuitBreakerProto;
import com.tencent.polaris.specification.api.v1.fault.tolerance.CircuitBreakerProto.TriggerCondition;
import com.tencent.polaris.specification.api.v1.model.ModelProto;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Pattern;

public abstract class TriggerCounter {

    private static final Logger LOG = LoggerFactory.getLogger(TriggerCounter.class);

    protected final String ruleName;

    protected final List<CircuitBreakerProto.ErrorCondition> errorConditionList;

    protected final TriggerCondition triggerCondition;

    protected final Resource resource;

    protected final ModelProto.API api;

    protected final StatusChangeHandler statusChangeHandler;

    protected final Function<String, Pattern> regexFunction;

    protected final Function<String, TrieNode<String>> trieNodeFunction;

    protected final AtomicBoolean suspended = new AtomicBoolean(false);

    public TriggerCounter(String ruleName, CounterOptions counterOptions) {
        this.errorConditionList = counterOptions.getErrorConditionList();
        this.triggerCondition = counterOptions.getTriggerCondition();
        this.ruleName = ruleName;
        this.resource = counterOptions.getResource();
        this.api = counterOptions.getApi();
        this.statusChangeHandler = counterOptions.getStatusChangeHandler();
        this.regexFunction = counterOptions.getRegexFunction();
        this.trieNodeFunction = counterOptions.getTrieNodeFunction();
        init();
    }

    public void suspend() {
        suspended.set(true);
        LOG.info("[CircuitBreaker][Counter] counter {} suspend", ruleName);
    }

    public void resume() {
        suspended.set(false);
        LOG.info("[CircuitBreaker][Counter] counter {} resume", ruleName);
    }

    protected abstract void init();

    public abstract void report(ResourceStat resourceStat, Function<String, Pattern> regexPatternFunction);

    public abstract void report(boolean success, Function<String, Pattern> regexPatternFunction);

    public String getReason() {
        return "";
    }
}
