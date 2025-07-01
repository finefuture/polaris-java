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

package com.tencent.polaris.plugins.stat.otel;

import com.tencent.polaris.api.config.global.TraceReporterConfig;
import com.tencent.polaris.api.exception.PolarisException;
import com.tencent.polaris.api.plugin.PluginType;
import com.tencent.polaris.api.plugin.common.InitContext;
import com.tencent.polaris.api.plugin.common.PluginTypes;
import com.tencent.polaris.api.plugin.compose.Extensions;
import com.tencent.polaris.api.plugin.stat.TraceReporter;
import com.tencent.polaris.api.utils.ClassUtils;
import com.tencent.polaris.logging.LoggerFactory;
import com.tencent.polaris.logging.PolarisLogging;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;

import java.util.Map;

public class OtelTraceReporter implements TraceReporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PolarisLogging.class);

    @Override
    public String getName() {
        return TraceReporterConfig.DEFAULT_REPORTER_OTEL;
    }

    @Override
    public PluginType getType() {
        return PluginTypes.TRACE_REPORTER.getBaseType();
    }

    @Override
    public void init(InitContext ctx) throws PolarisException {

    }

    @Override
    public void postContextInit(Extensions ctx) throws PolarisException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean isEnabled() {
        return ClassUtils.isClassPresent("io.opentelemetry.api.trace.Span");
    }

    @Override
    public void setSpanAttributes(Map<String, String> attributes) {
        LOGGER.debug("OtelTraceReporter: setSpanAttributes: {}", attributes);
        Span span = Span.current();
        attributes.forEach(span::setAttribute);
    }

    @Override
    public Object setBaggageAttributes(Map<String, String> attributes) {
        LOGGER.debug("OtelTraceReporter: setBaggageAttributes: {}", attributes);
        BaggageBuilder builder = Baggage.current().toBuilder();
        attributes.forEach(builder::put);
        return builder.build().makeCurrent();
    }
}
