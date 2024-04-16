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

package com.tencent.polaris.logging;

/**
 * Constants of Logging.
 *
 * @author Haotian Zhang
 */
public interface LoggingConsts {

    String LOGGING_CONFIG_PROPERTY = "polaris.logging.config";

    String LOGGING_PATH_PROPERTY = "polaris.log.home";

    String LOGGING_UPDATE_EVENT = "polaris-update-event";

    String LOGGING_UPDATE_EVENT_ASYNC = "polaris-update-event-async";

    String LOGGING_CIRCUITBREAKER_EVENT = "polaris-circuitbreaker-event";

    String LOGGING_HEALTHCHECK_EVENT = "polaris-healthcheck-event";

    String LOGGING_LOSSLESS_EVENT = "polaris-lossless-event";

    String LOGGING_HEARTBEAT_RECORD = "polaris-instance-heartbeat";
}
