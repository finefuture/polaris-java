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

package com.tencent.polaris.encrypt.impl;

import com.tencent.polaris.encrypt.ConfigEncryptProvider;
import com.tencent.polaris.encrypt.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigEncryptAESProvider extends ConfigEncryptProvider {

    private static final Logger log = LoggerFactory.getLogger(ConfigEncryptAESProvider.class);

    @Override
    public String encrypt(String content, String password) {
        try {
            return AESUtil.encrypt(content, password);
        } catch (Exception e) {
            log.error("[TSF SDK] Error on encrypting.", e);
            return content;
        }
    }

    @Override
    public String decrypt(String encryptedContent, String password) {
        try {
            return AESUtil.decrypt(encryptedContent, password);
        } catch (Exception e) {
            log.error("[TSF SDK] Error on decrypting.", e);
            return encryptedContent;
        }
    }
}
