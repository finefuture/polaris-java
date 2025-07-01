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

package com.tencent.polaris.plugins.connector.consul.service.authority.entity;

import java.io.Serializable;
import java.util.List;

public class AuthRuleGroup implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1643183648126834802L;
    /**
     * 规则列表
     */
    private List<AuthRule> rules;
    /**
     * 规则计算规则
     */
    private String ruleProgram;

    /**
     * 鉴权类型
     */
    private String type;

    public List<AuthRule> getRules() {
        return rules;
    }

    public void setRules(List<AuthRule> rules) {
        this.rules = rules;
    }

    public String getRuleProgram() {
        return ruleProgram;
    }

    public void setRuleProgram(String ruleProgram) {
        this.ruleProgram = ruleProgram;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AuthRuleGroup{");
        sb.append("rules=").append(rules);
        sb.append(", ruleProgram='").append(ruleProgram).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
