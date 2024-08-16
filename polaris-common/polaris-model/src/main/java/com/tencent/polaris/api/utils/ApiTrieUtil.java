/*
 * Tencent is pleased to support the open source community by making Polaris available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company. All rights reserved.
 *
 *  Licensed under the BSD 3-Clause License (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.tencent.polaris.api.utils;

import com.tencent.polaris.api.pojo.HttpElement;
import com.tencent.polaris.api.pojo.TrieNode;

public class ApiTrieUtil {

    /**
     * @param apiPath
     * @return TrieNode
     */
    public static TrieNode<String> buildSimpleTrieNode(String apiPath) {
        if (StringUtils.isEmpty(apiPath)) {
            return null;
        }
        return buildSimpleTrieNode(new String[]{apiPath});
    }

    public static TrieNode<String> buildSimpleTrieNode(String[] apiPathInfoList) {
        if (apiPathInfoList.length == 0) {
            return null;
        }

        TrieNode<String> root = new TrieNode<>(TrieNode.ROOT_PATH);
        for (String apiPathInfo : apiPathInfoList) {
            int flag = apiPathInfo.lastIndexOf("-");
            String method = null;
            String path = apiPathInfo;
            if (flag != -1) {
                method = apiPathInfo.substring(flag + 1);
                path = apiPathInfo;
                if (HttpElement.HTTP_METHOD_SET.contains(method)) {
                    path = apiPathInfo.substring(0, flag);
                } else {
                    method = null;
                }
            }

            // 因为前端的改动（最初的 tagValue 只有 path，某次前端组件改动后变成了 path-method，非客户提的），有兼容性问题，
            // 临时简化处理，不处理 method，前面逻辑保留是为了取出正确的 path
            method = null;
            String[] apiPaths = path.split("/");

            // 跳过第一个为空的str
            TrieNode<String> node = root;
            for (int i = 1; i < apiPaths.length; i++) {
                node = node.getOrCreateSubNode(apiPaths[i]);

                // 叶子节点，需要 info
                if (i == apiPaths.length - 1) {
                    node.setNodeInfo(TrieNode.SIMPLE_VALID_INFO + "method:" + method);
                }
            }
        }
        return root;
    }

    public static boolean checkSimple(TrieNode<String> root, String apiPathInfo) {
        if (root == null) {
            return false;
        }
        int flag = apiPathInfo.lastIndexOf("-");
        String method = apiPathInfo.substring(flag + 1);
        String path = apiPathInfo;
        if (HttpElement.HTTP_METHOD_SET.contains(method)) {
            path = apiPathInfo.substring(0, flag);
        } else {
            method = null;
        }
        // 因为前端的改动（最初的 tagValue 只有 path，某次前端组件改动后变成了 path-method，非客户提的），有兼容性问题，
        // 临时简化处理，不处理 method，前面逻辑保留是为了取出正确的 path
        method = null;
        String[] apiPaths = path.split("/");

        TrieNode<String> node = root;
        for (int i = 1; i < apiPaths.length; i++) {
            if (node == null) {
                return false;
            }
            node = node.getSubNode(apiPaths[i]);
            // 叶子节点
            if (i == apiPaths.length - 1) {
                if (node == null) {
                    return false;
                } else {
                    return StringUtils.equals(TrieNode.SIMPLE_VALID_INFO + "method:" + method, node.getNodeInfo());
                }
            }
        }

        return false;
    }

}
