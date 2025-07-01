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

package com.tencent.polaris.location.example;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tencent.polaris.api.config.Configuration;
import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.rpc.InstanceDeregisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.api.utils.CollectionUtils;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;
import com.tencent.polaris.location.example.pb.LocationGRPCGrpc;
import com.tencent.polaris.location.example.pb.LocationGRPCService;
import com.tencent.polaris.location.example.utils.ProviderExampleUtils;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class Provider {

    private static final String NAMESPACE_DEFAULT = "default";

    private static final String ECHO_SERVICE_NAME = "LocationServerJava";

    private static final int TTL = 5;

    private static final int LISTEN_PORT = 15800;

    public static void main(String[] args) throws Exception {
        ProviderExampleUtils.InitResult initResult = ProviderExampleUtils.initProviderConfiguration(args);

        runHttpLocationServer();
        runGrpcLocationServer();

        String namespace = NAMESPACE_DEFAULT;
        String service = ECHO_SERVICE_NAME;

        HttpServer server = HttpServer.create(new InetSocketAddress(LISTEN_PORT), 0);
        int localPort = server.getAddress().getPort();
        server.createContext("/echo", new EchoServerHandler(localPort));
        Configuration configuration = ProviderExampleUtils.createConfiguration(initResult.getConfig());
        String localHost = getLocalHost(configuration);

        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPIByConfig(configuration);
        register(namespace, service, localHost, localPort, providerAPI);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
            deregister(namespace, service, localHost, localPort, providerAPI);
            providerAPI.close();
        }));

        server.start();
    }

    // do the instance register
    private static void register(String namespace, String service, String host, int port,
            ProviderAPI providerAPI) {
        InstanceRegisterRequest registerRequest = new InstanceRegisterRequest();
        registerRequest.setNamespace(namespace);
        registerRequest.setService(service);
        registerRequest.setHost(host);
        registerRequest.setPort(port);
        registerRequest.setTtl(TTL);
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

        private final int localPort;

        public EchoServerHandler(int localPort) {
            this.localPort = localPort;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> parameters = splitQuery(exchange.getRequestURI());
            String echoValue = parameters.get("value");
            String response = "echo: " + echoValue + ", from: " + localPort;
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


    private static void runHttpLocationServer() throws Exception {
        new Thread(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(LISTEN_PORT + 100), 0);
                server.createContext("/region", new HttpLocationServerHandler("region"));
                server.createContext("/zone", new HttpLocationServerHandler("zone"));
                server.createContext("/campus", new HttpLocationServerHandler("campus"));
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    server.stop(0);
                }));
                System.out.println("success run local http location server");
                server.start();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private static class HttpLocationServerHandler implements HttpHandler {

        private final String type;

        public HttpLocationServerHandler(String type) {
            this.type = type;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "";

            switch (type) {
            case "region":
                response = "china";
                break;
            case "zone":
                response = "ap-guangzhou";
                break;
            case "campus":
                response = "ap-guangzhou-1";
            }

            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private static void runGrpcLocationServer() throws Exception {
        new Thread(() -> {
            try {
                Server server = ServerBuilder.forPort(LISTEN_PORT + 200).addService(new GrpcLocationServerHandler()).build();
                System.out.println("success run local grpc location server");
                server = server.start();
                Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private static class GrpcLocationServerHandler extends LocationGRPCGrpc.LocationGRPCImplBase {

        @Override
        public void getLocation(LocationGRPCService.LocationRequest request, StreamObserver<LocationGRPCService.LocationResponse> responseObserver) {
            responseObserver.onNext(LocationGRPCService.LocationResponse.newBuilder()
                            .setRegion("china")
                            .setZone("ap-guangzhou")
                            .setCampus("ap-guangzhou-2")
                    .build());
            responseObserver.onCompleted();
        }
    }

}
