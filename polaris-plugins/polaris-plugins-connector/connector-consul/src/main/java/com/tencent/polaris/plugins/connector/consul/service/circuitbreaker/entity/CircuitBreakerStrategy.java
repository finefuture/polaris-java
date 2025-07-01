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

package com.tencent.polaris.plugins.connector.consul.service.circuitbreaker.entity;

import java.io.Serializable;
import java.util.List;

import static com.tencent.polaris.plugins.connector.consul.service.circuitbreaker.entity.TsfCircuitBreakerConstant.*;


/**
 * @author zhixinzxliu
 */
public class CircuitBreakerStrategy implements Serializable {
    /**
     * 空构造函数
     */
    public CircuitBreakerStrategy() {
    }

    /**
     * 熔断策略ID
     */
    private String strategyId;

    /**
     * 熔断策略所属熔断规则ID
     */
    private String ruleId;

    /**
     * 熔断策略作用API
     * 只有在有API级别或者ALL级别时需要用的该field
     */
    private List<CircuitBreakerApi> apiList;

    /**
     * 滚动窗口统计时间
     */
    private int slidingWindowSize = 10;

    /**
     * 最少请求数
     */
    private int minimumNumberOfCalls = 10;

    /**
     * 失败请求比例
     */
    private int failureRateThreshold = 50;

    /**
     * 熔断开启到半开间隔,单位s
     */
    private int waitDurationInOpenState = 60;

    /**
     * 实例级别的话需要该参数
     * 最大熔断实例的比例，超过该比例后则不进行熔断了
     */
    private int maxEjectionPercent = 50;

    /**
     * 慢请求时间阈值定义
     */
    private int slowCallDurationThreshold = 60 * 1000;

    /**
     * 慢请求熔断比例阈值
     */
    private int slowCallRateThreshold = 50;

    public int getSlowCallDurationThreshold() {
        return slowCallDurationThreshold;
    }

    public void setSlowCallDurationThreshold(int slowCallDurationThreshold) {
        this.slowCallDurationThreshold = slowCallDurationThreshold;
    }

    public int getSlowCallRateThreshold() {
        return slowCallRateThreshold;
    }

    public void setSlowCallRateThreshold(int slowCallRateThreshold) {
        this.slowCallRateThreshold = slowCallRateThreshold;
    }

    public int getMaxEjectionPercent() {
        return maxEjectionPercent;
    }

    public void setMaxEjectionPercent(int maxEjectionPercent) {
        this.maxEjectionPercent = maxEjectionPercent;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public List<CircuitBreakerApi> getApiList() {
        return apiList;
    }

    public void setApiList(List<CircuitBreakerApi> apiList) {
        this.apiList = apiList;
    }

    public int getSlidingWindowSize() {
        return slidingWindowSize;
    }

    public void setSlidingWindowSize(int slidingWindowSize) {
        this.slidingWindowSize = slidingWindowSize;
    }

    public int getMinimumNumberOfCalls() {
        return minimumNumberOfCalls;
    }

    public void setMinimumNumberOfCalls(int minimumNumberOfCalls) {
        this.minimumNumberOfCalls = minimumNumberOfCalls;
    }

    public int getFailureRateThreshold() {
        return failureRateThreshold;
    }

    public void setFailureRateThreshold(int failureRateThreshold) {
        this.failureRateThreshold = failureRateThreshold;
    }

    public int getWaitDurationInOpenState() {
        return waitDurationInOpenState;
    }

    public void setWaitDurationInOpenState(int waitDurationInOpenState) {
        this.waitDurationInOpenState = waitDurationInOpenState;
    }

    public boolean validate() {
        if (slidingWindowSize > MAX_SLIDING_WINDOW_SIZE || slidingWindowSize < MIN_SLIDING_WINDOW_SIZE) {
            return false;
        }

        if (minimumNumberOfCalls < MINIMUN_NUMBER_OF_CALLS) {
            return false;
        }

        if (failureRateThreshold > MAX_FAILURE_RATE_THRESHOLD || failureRateThreshold < MIN_FAILURE_RATE_THRESHOLD) {
            return false;
        }

        if (slowCallRateThreshold > MAX_FAILURE_RATE_THRESHOLD || slowCallRateThreshold < MIN_FAILURE_RATE_THRESHOLD) {
            return false;
        }

        if (slowCallDurationThreshold < 1) {
            return false;
        }

        if (waitDurationInOpenState > MAX_WAIT_DURATION_IN_OPEN_STATE || waitDurationInOpenState < MIN_WAIT_DURATION_IN_OPEN_STATE) {
            return false;
        }

        if (maxEjectionPercent > MAX_EJECTION_RATE_THRESHOLD || maxEjectionPercent < MIN_EJECTION_RATE_THRESHOLD) {
            return false;
        }

        if (apiList != null) {
            for (CircuitBreakerApi circuitBreakerApi : apiList) {
                if (!circuitBreakerApi.validate()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "CircuitBreakerStrategy{" +
                "strategyId='" + strategyId + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", apiList=" + apiList +
                ", slidingWindowSize=" + slidingWindowSize +
                ", minimumNumberOfCalls=" + minimumNumberOfCalls +
                ", failureRateThreshold=" + failureRateThreshold +
                ", waitDurationInOpenState=" + waitDurationInOpenState +
                ", maxEjectionPercent=" + maxEjectionPercent +
                ", slowCallDurationThreshold=" + slowCallDurationThreshold +
                ", slowCallRateThreshold=" + slowCallRateThreshold +
                '}';
    }
}
