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

package com.tencent.polaris.plugins.router.rule;

import com.tencent.polaris.api.pojo.Instance;
import com.tencent.polaris.specification.api.v1.traffic.manage.RoutingProto.Destination;
import java.util.List;

/**
 * 带权重的实例subset
 */
public class WeightedSubset {

    private final Destination destination;

    // 实例subset
    private List<Instance> instances;
    // subset列表
    private long weight;

    public WeightedSubset(Destination destination) {
        this.destination = destination;
    }

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public Destination getDestination() {
        return destination;
    }
}

