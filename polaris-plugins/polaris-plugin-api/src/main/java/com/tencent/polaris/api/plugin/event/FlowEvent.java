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

package com.tencent.polaris.api.plugin.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程事件。
 *
 * @author Haotian Zhang
 */
public class FlowEvent implements BaseEvent {

    /**
     * 事件类型
     */
    @JsonProperty("event_type")
    private final EventConstants.EventType eventType;

    /**
     * 事件名称
     */
    @JsonProperty("event_name")
    private final EventConstants.EventName eventName;

    /**
     * 时间戳
     */
    @JsonProperty("event_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss:SSSS")
    private final LocalDateTime timestamp;

    /**
     * 客户端ID
     */
    @JsonProperty("client_id")
    private final String clientId;

    /**
     * 客户端IP
     */
    @JsonProperty("client_ip")
    private final String clientIp;

    /**
     * 被调命名空间
     */
    @JsonProperty("namespace")
    private final String namespace;

    /**
     * 被调服务名
     */
    @JsonProperty("service")
    private final String service;

    /**
     * 接口协议
     */
    @JsonProperty("api_protocol")
    private final String apiProtocol;

    /**
     * 接口路径
     */
    @JsonProperty("api_path")
    private final String apiPath;

    /**
     * 接口方法
     */
    @JsonProperty("api_method")
    private final String apiMethod;

    /**
     * 实例IP
     */
    @JsonProperty("host")
    private final String host;

    /**
     * 实例端口
     */
    @JsonProperty("port")
    private final String port;

    /**
     * 主调命名空间
     */
    @JsonProperty("source_namespace")
    private final String sourceNamespace;

    /**
     * 主调服务名
     */
    @JsonProperty("source_service")
    private final String sourceService;

    /**
     * 主调标签
     */
    @JsonProperty("labels")
    private final String labels;

    /**
     * 当前事件状态
     */
    private final EventConstants.Status currentStatus;

    /**
     * 上一次事件状态
     */
    private final EventConstants.Status previousStatus;

    /**
     * 资源类型
     */
    private final EventConstants.ResourceType resourceType;

    /**
     * 治理规则名
     */
    private final String ruleName;

    /**
     * 状态转换原因
     */
    @JsonProperty("reason")
    private final String reason;

    private final Map<String, String> additionalParams;

    private FlowEvent(Builder builder) {
        this.eventType = builder.eventType;
        this.eventName = builder.eventName;
        this.timestamp = builder.timestamp;
        this.clientId = builder.clientId;
        this.clientIp = builder.clientIp;
        this.namespace = builder.namespace;
        this.service = builder.service;
        this.apiProtocol = builder.apiProtocol;
        this.apiPath = builder.apiPath;
        this.apiMethod = builder.apiMethod;
        this.host = builder.host;
        if (builder.port != null) {
            this.port = String.valueOf(builder.port);
        } else {
            this.port = "";
        }
        this.sourceNamespace = builder.sourceNamespace;
        this.sourceService = builder.sourceService;
        this.labels = builder.labels;
        this.currentStatus = builder.currentStatus;
        this.previousStatus = builder.previousStatus;
        this.resourceType = builder.resourceType;
        this.ruleName = builder.ruleName;
        this.reason = builder.reason;
        additionalParams = new HashMap<>();
    }

    public static class Builder {
        private EventConstants.EventType eventType;
        private EventConstants.EventName eventName;
        private LocalDateTime timestamp;
        private String clientId = "";
        private String clientIp = "";
        private String namespace = "";
        private String service = "";
        private String apiProtocol = "";
        private String apiPath = "";
        private String apiMethod = "";
        private String host = "";
        private Integer port;
        private String sourceNamespace = "";
        private String sourceService = "";
        private String labels = "";
        private EventConstants.Status currentStatus;
        private EventConstants.Status previousStatus;
        private EventConstants.ResourceType resourceType;
        private String ruleName = "";
        private String reason = "";

        public Builder withEventType(EventConstants.EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder withEventName(EventConstants.EventName eventName) {
            this.eventName = eventName;
            return this;
        }

        public Builder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withClientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public Builder withNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder withService(String service) {
            this.service = service;
            return this;
        }

        public Builder withApiProtocol(String apiProtocol) {
            this.apiProtocol = apiProtocol;
            return this;
        }

        public Builder withApiPath(String apiPath) {
            this.apiPath = apiPath;
            return this;
        }

        public Builder withApiMethod(String apiMethod) {
            this.apiMethod = apiMethod;
            return this;
        }

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder withSourceNamespace(String sourceNamespace) {
            this.sourceNamespace = sourceNamespace;
            return this;
        }

        public Builder withSourceService(String sourceService) {
            this.sourceService = sourceService;
            return this;
        }

        public Builder withLabels(String labels) {
            this.labels = labels;
            return this;
        }

        public Builder withCurrentStatus(EventConstants.Status currentStatus) {
            this.currentStatus = currentStatus;
            return this;
        }

        public Builder withPreviousStatus(EventConstants.Status previousStatus) {
            this.previousStatus = previousStatus;
            return this;
        }

        public Builder withResourceType(EventConstants.ResourceType resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder withRuleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public Builder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public FlowEvent build() {
            return new FlowEvent(this);
        }
    }

    public EventConstants.EventType getEventType() {
        return eventType;
    }

    public EventConstants.EventName getEventName() {
        return eventName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getService() {
        return service;
    }

    public String getApiProtocol() {
        return apiProtocol;
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getSourceNamespace() {
        return sourceNamespace;
    }

    public String getSourceService() {
        return sourceService;
    }

    public String getLabels() {
        return labels;
    }

    public EventConstants.Status getCurrentStatus() {
        return currentStatus;
    }

    public EventConstants.Status getPreviousStatus() {
        return previousStatus;
    }

    public EventConstants.ResourceType getResourceType() {
        return resourceType;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String convertMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String formattedDateTime = "";
        if (this.getTimestamp() != null) {
            formattedDateTime = formatter.format(this.getTimestamp());
        }

        String eventType = "";
        if (this.getEventType() != null) {
            eventType = this.getEventType().name();
        }

        String eventName = "";
        if (this.getEventName() != null) {
            eventName = this.getEventName().name();
        }

        String currentStatus = "";
        if (this.getCurrentStatus() != null) {
            currentStatus = this.getCurrentStatus().name();
        }

        String previousStatus = "";
        if (this.getPreviousStatus() != null) {
            previousStatus = this.getPreviousStatus().name();
        }

        String resourceType = "";
        if (this.getResourceType() != null) {
            resourceType = this.getResourceType().name();
        }

        return eventType + "|" + eventName + "|" + formattedDateTime + "|" + this.getClientId() + "|"
                + this.getClientIp() + "|" + this.getNamespace() + "|" + this.getService() + "|"
                + this.getApiProtocol() + "|" + this.getApiPath() + "|" + this.getApiMethod() + "|"
                + this.getHost() + "|" + this.getPort() + "|" + this.getSourceNamespace() + "|"
                + this.getSourceService() + "|" + this.getLabels() + "|" + currentStatus + "|"
                + previousStatus + "|" + resourceType + "|" + this.getRuleName() + "|" + this.getReason();
    }

    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }

    @Override
    public String toString() {
        return "FlowEvent{" +
                "eventType=" + eventType +
                ", eventName=" + eventName +
                ", timestamp=" + timestamp +
                ", clientId='" + clientId + '\'' +
                ", clientIp='" + clientIp + '\'' +
                ", namespace='" + namespace + '\'' +
                ", service='" + service + '\'' +
                ", apiProtocol='" + apiProtocol + '\'' +
                ", apiPath='" + apiPath + '\'' +
                ", apiMethod='" + apiMethod + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", sourceNamespace='" + sourceNamespace + '\'' +
                ", sourceService='" + sourceService + '\'' +
                ", labels='" + labels + '\'' +
                ", currentStatus=" + currentStatus +
                ", previousStatus=" + previousStatus +
                ", resourceType=" + resourceType +
                ", ruleName='" + ruleName + '\'' +
                ", reason='" + reason + '\'' +
                ", additionalParams=" + additionalParams +
                '}';
    }
}