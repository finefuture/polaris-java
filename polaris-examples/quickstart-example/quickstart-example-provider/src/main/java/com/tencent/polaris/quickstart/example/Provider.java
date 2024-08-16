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

package com.tencent.polaris.quickstart.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tencent.polaris.api.config.Configuration;
import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.rpc.InstanceDeregisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.api.utils.CollectionUtils;
import com.tencent.polaris.client.api.SDKContext;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;
import com.tencent.polaris.quickstart.example.utils.ProviderExampleUtils;
import com.tencent.polaris.specification.api.v1.traffic.manage.RoutingProto;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Provider {

    private static final String NAMESPACE_DEFAULT = "default";

    private static final String ECHO_SERVICE_NAME = "EchoServerJava";

    private static final int TTL = 5;

    private static final int LISTEN_PORT = 0;

    public static void main(String[] args) throws Exception {
        ProviderExampleUtils.InitResult initResult = ProviderExampleUtils.initProviderConfiguration(args);

        String namespace = NAMESPACE_DEFAULT;
        String service = ECHO_SERVICE_NAME;

        Configuration configuration = ProviderExampleUtils.createConfiguration(initResult.getConfig());
        String localHost = getLocalHost(configuration);

        SDKContext context = SDKContext.initContextByConfig(configuration);

        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPIByContext(context);

        HttpServer server = HttpServer.create(new InetSocketAddress(LISTEN_PORT), 0);
        int localPort = server.getAddress().getPort();
        server.createContext("/echo", new EchoServerHandler(context, localPort));

        register(namespace, service, localHost, localPort, providerAPI);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
            deregister(namespace, service, localHost, localPort, providerAPI);
            providerAPI.close();
        }));

        server.start();
    }

    private static String providedInstanceId(String namespace, String service, String host, int port) {
        return String.format("%s#%s#%s#%d", namespace, service, host, port);
    }

    // do the instance register
    private static void register(String namespace, String service, String host, int port,
                                 ProviderAPI providerAPI) {
        InstanceRegisterRequest registerRequest = new InstanceRegisterRequest();
        registerRequest.setNamespace(namespace);
        registerRequest.setService(service);
        registerRequest.setHost(host);
        registerRequest.setPort(port);
        registerRequest.setProtocol("http");
        registerRequest.setVersion("1.0.0");
        registerRequest.setTtl(TTL);
        // 实例id不是必填，如果不填，服务端会默认生成一个唯一Id，否则当提供实例id时，需要保证实例id是唯一的
        registerRequest.setInstanceId(providedInstanceId(namespace, service, host, port));
        InstanceRegisterResponse registerResp = providerAPI.registerInstance(registerRequest);
        System.out.printf("register instance %s:%d to service %s(%s), id is %s%n",
                host, port, service, namespace, registerResp.getInstanceId());
    }

    // do the instance deregister
    private static void deregister(String namespace, String service, String host, int port,
                                   ProviderAPI providerAPI) {
        InstanceDeregisterRequest deregisterRequest = new InstanceDeregisterRequest();
        deregisterRequest.setNamespace(namespace);
        deregisterRequest.setService(service);
        deregisterRequest.setHost(host);
        deregisterRequest.setPort(port);
        // 实例id不是必填，如果注册时指定了实例id，则反注册时需要提供同样的id
        deregisterRequest.setInstanceID(providedInstanceId(namespace, service, host, port));
        providerAPI.deRegister(deregisterRequest);
        System.out.printf("deregister instance, address is %s:%d%n", host, port);
    }

    private static Map<String, String> splitQuery(URI uri) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = uri.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    private static String getLocalHost(Configuration configuration) throws Exception {
        String serverAddress;
        if (CollectionUtils.isNotEmpty(configuration.getGlobal().getServerConnectors())) {
            serverAddress = configuration.getGlobal().getServerConnectors().get(0).getAddresses().get(0);
        } else {
            serverAddress = configuration.getGlobal().getServerConnector().getAddresses().get(0);
        }
        String[] tokens = serverAddress.split(":");
        try (Socket socket = new Socket(tokens[0], Integer.parseInt(tokens[1]))) {
            return socket.getLocalAddress().getHostAddress();
        }
    }

    private static class EchoServerHandler implements HttpHandler {

        private final SDKContext context;

        private final int localPort;

        public EchoServerHandler(SDKContext context, int localPort) {
            this.context = context;
            this.localPort = localPort;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Map<String, String> location = new HashMap<>();
            location.put(RoutingProto.NearbyRoutingConfig.LocationLevel.REGION.name(), context.getValueContext().getValue(RoutingProto.NearbyRoutingConfig.LocationLevel.REGION.name()));
            location.put(RoutingProto.NearbyRoutingConfig.LocationLevel.ZONE.name(), context.getValueContext().getValue(RoutingProto.NearbyRoutingConfig.LocationLevel.ZONE.name()));
            location.put(RoutingProto.NearbyRoutingConfig.LocationLevel.CAMPUS.name(), context.getValueContext().getValue(RoutingProto.NearbyRoutingConfig.LocationLevel.CAMPUS.name()));

            Map<String, String> parameters = splitQuery(exchange.getRequestURI());
            String echoValue = parameters.get("value");
            String response = "echo: " + echoValue + ", from: " + localPort + ", location: " + location;
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
