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

package com.tencent.polaris.router.version.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tencent.polaris.api.config.Configuration;
import com.tencent.polaris.api.core.ProviderAPI;
import com.tencent.polaris.api.rpc.InstanceDeregisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterRequest;
import com.tencent.polaris.api.rpc.InstanceRegisterResponse;
import com.tencent.polaris.factory.ConfigAPIFactory;
import com.tencent.polaris.factory.api.DiscoveryAPIFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

public class Provider {

    private static final String NAMESPACE_DEFAULT = "default";

    private static final String ECHO_SERVICE_NAME = "VersionServerJava";

    private static final int TTL = 5;

    public static void main(String[] args) throws Exception {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("version", "provider version", true, "version for instance");

        CommandLine commandLine = parser.parse(options, args);
        String version = commandLine.getOptionValue("version");

        String namespace = NAMESPACE_DEFAULT;
        String service = ECHO_SERVICE_NAME;

        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/echo", new EchoServerHandler(version));

        Configuration configuration = ConfigAPIFactory.defaultConfig();
        String localHost = getLocalHost(configuration);
        int localPort = server.getAddress().getPort();

        ProviderAPI providerAPI = DiscoveryAPIFactory.createProviderAPIByConfig(configuration);
        Provider.register(namespace, service, localHost, localPort, version, providerAPI);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
            deregister(namespace, service, localHost, localPort, providerAPI);
            providerAPI.close();
        }));
        server.start();
    }

    // do the instance register
    private static void register(String namespace, String service, String host, int port, String version,
            ProviderAPI providerAPI) {
        InstanceRegisterRequest registerRequest = new InstanceRegisterRequest();
        registerRequest.setNamespace(namespace);
        registerRequest.setService(service);
        registerRequest.setHost(host);
        registerRequest.setPort(port);
        registerRequest.setVersion(version);
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
        String serverAddress = configuration.getGlobal().getServerConnector().getAddresses().get(0);
        String[] tokens = serverAddress.split(":");
        try (Socket socket = new Socket(tokens[0], Integer.parseInt(tokens[1]))) {
            return socket.getLocalAddress().getHostAddress();
        }
    }

    private static class EchoServerHandler implements HttpHandler {

        private final String version;

        public EchoServerHandler(String version) {
            this.version = version;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> parameters = splitQuery(exchange.getRequestURI());
            String echoValue = parameters.get("value");
            String response = "version: " + version + ", echo: " + echoValue;
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
