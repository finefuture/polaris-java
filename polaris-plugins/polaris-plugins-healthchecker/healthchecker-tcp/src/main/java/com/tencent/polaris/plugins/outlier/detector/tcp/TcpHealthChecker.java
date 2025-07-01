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

package com.tencent.polaris.plugins.outlier.detector.tcp;

import com.tencent.polaris.api.config.consumer.OutlierDetectionConfig;
import com.tencent.polaris.api.config.plugin.PluginConfigProvider;
import com.tencent.polaris.api.config.verify.DefaultValues;
import com.tencent.polaris.api.config.verify.Verifier;
import com.tencent.polaris.api.exception.PolarisException;
import com.tencent.polaris.api.plugin.PluginType;
import com.tencent.polaris.api.plugin.common.InitContext;
import com.tencent.polaris.api.plugin.common.PluginTypes;
import com.tencent.polaris.api.plugin.compose.Extensions;
import com.tencent.polaris.api.plugin.detect.HealthChecker;
import com.tencent.polaris.api.pojo.DetectResult;
import com.tencent.polaris.api.pojo.Instance;
import com.tencent.polaris.api.pojo.RetStatus;
import com.tencent.polaris.api.utils.ConversionUtils;
import com.tencent.polaris.api.utils.IOUtils;
import com.tencent.polaris.api.utils.StringUtils;
import com.tencent.polaris.logging.LoggerFactory;
import com.tencent.polaris.specification.api.v1.fault.tolerance.FaultDetectorProto.FaultDetectRule;
import com.tencent.polaris.specification.api.v1.fault.tolerance.FaultDetectorProto.FaultDetectRule.Protocol;
import com.tencent.polaris.specification.api.v1.fault.tolerance.FaultDetectorProto.TcpProtocolConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;

/**
 * TcpOutlierDetector.java
 *
 * @author andrewshan
 * @date 2019/9/19
 */
public class TcpHealthChecker implements HealthChecker, PluginConfigProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TcpHealthChecker.class);

    private TcpProtocolConfig config;

    @Override
    public DetectResult detectInstance(Instance instance, FaultDetectRule faultDetectRule) throws PolarisException {
        String host = instance.getHost();
        int port = instance.getPort();

        TcpProtocolConfig curConfig = config;

        int timeoutMs = DEFAULT_TIMEOUT_MILLI;
        if (null != faultDetectRule && faultDetectRule.getProtocol() == Protocol.TCP) {
            if (faultDetectRule.getTimeout() > 0) {
                timeoutMs = faultDetectRule.getTimeout();
            }
            if (faultDetectRule.getPort() > 0) {
                port = faultDetectRule.getPort();
            }
            curConfig = faultDetectRule.getTcpConfig();
        }
        byte[] sendBytes = null;
        int maxLength = 0;
        List<byte[]> expectRecvStrs = new ArrayList<>();
        if (null != curConfig) {
            if (StringUtils.isNotBlank(curConfig.getSend())) {
                sendBytes = ConversionUtils.anyStringToByte(curConfig.getSend());
            }
            for (String receiveStr : curConfig.getReceiveList()) {
                byte[] receiveBytes = ConversionUtils.anyStringToByte(receiveStr);
                if (receiveBytes.length > maxLength) {
                    maxLength = receiveBytes.length;
                }
                expectRecvStrs.add(receiveBytes);
            }
        }
        long startTimeMillis = System.currentTimeMillis();
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            boolean needSendData = null != sendBytes && sendBytes.length > 0;
            if (!needSendData) {
                //未配置发送包，则连接成功即可
                long delayMillis = System.currentTimeMillis() - startTimeMillis;
                return new DetectResult(0, delayMillis, RetStatus.RetSuccess);
            }
            socket.setSoTimeout(timeoutMs);
            OutputStream os = socket.getOutputStream();
            //发包
            os.write(sendBytes);
            os.flush();
            long delayMillis = System.currentTimeMillis() - startTimeMillis;
            if (expectRecvStrs.isEmpty()) {
                return new DetectResult(0, delayMillis, RetStatus.RetSuccess);
            }
            byte[] recvBytes = recvFromSocket(socket);
            System.out.println("[TCP] checker receive bytes " + new String(recvBytes));
            boolean found = false;
            for (byte[] expectRecvStr : expectRecvStrs) {
                byte[] recvBytesClone = Arrays.copyOfRange(recvBytes, 0, expectRecvStr.length);
                if (Arrays.equals(expectRecvStr, recvBytesClone)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                //回包符合预期
                return new DetectResult(0, delayMillis, RetStatus.RetSuccess);
            }
            return new DetectResult(-1, delayMillis, RetStatus.RetFail);
        } catch (IOException e) {
            LOG.warn("tcp detect instance, create sock exception, host:{}, port:{}, error {}", host, port,
                    e.getMessage());
            long delayMillis = System.currentTimeMillis() - startTimeMillis;
            return new DetectResult(-1, delayMillis, RetStatus.RetFail);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOG.info("tcp detect instance, close sock exception, host:{}, port:{}, e:{}", host, port, e);
                }
            }
        }
    }

    private byte[] recvFromSocket(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();

        byte[] recvBytes = new byte[1024];
        int read = IOUtils.read(is, recvBytes, 0, recvBytes.length);
        return Arrays.copyOfRange(recvBytes, 0, read);
    }


    @Override
    public Class<? extends Verifier> getPluginConfigClazz() {
        return Config.class;
    }

    @Override
    public String getName() {
        return DefaultValues.DEFAULT_HEALTH_CHECKER_TCP;
    }

    @Override
    public PluginType getType() {
        return PluginTypes.HEALTH_CHECKER.getBaseType();
    }

    @Override
    public void init(InitContext ctx) throws PolarisException {
        OutlierDetectionConfig outlierDetection = ctx.getConfig().getConsumer().getOutlierDetection();
        Config cfg = outlierDetection.getPluginConfig(getName(), Config.class);
        TcpProtocolConfig.Builder builder = TcpProtocolConfig.newBuilder();
        if (null != cfg && StringUtils.isNotBlank(cfg.getSend())) {
            builder.setSend(cfg.getSend());
        }
        if (null != cfg && StringUtils.isNotBlank(cfg.getReceive())) {
            builder.addReceive(cfg.getReceive());
        }
        this.config = builder.build();
    }

    @Override
    public void postContextInit(Extensions extensions) throws PolarisException {

    }

    @Override
    public void destroy() {

    }
}