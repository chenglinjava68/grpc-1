package cn.sic777.start;

import cn.sic777.client.RpcDemoClient;
import cn.sic777.protobuf.ResponseParams;
import cn.sic777.start.args.ClientArgs;
import cn.sic777.start.config.ClientConfig;
import cn.sic777.start.constants.ClientContant;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class ClientStart {
    private ManagedChannel channel;

    private final void shutdown() throws InterruptedException {
        channel.awaitTermination(3, TimeUnit.SECONDS);
    }

    private final void start(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
        ClientConfig.instance().init(channel);
    }

    public static void main(String[] args) throws InterruptedException {
        ClientArgs.load(args);
        Properties pro = ClientConfig.instance().load(ClientArgs.CONFIG_PATH);
        String host = pro.getProperty(ClientContant.RPC_SERVER_HOST, "127.0.0.1");
        int port = Integer.parseInt(pro.getProperty(ClientContant.RPC_SERVER_PORT, "10000"));
        ClientStart start = new ClientStart();
        start.start(host, port);
        demo();//demo
        start.shutdown();
    }

    private static void demo() throws InterruptedException {
        System.out.println("*****simple stream rpc****");
        RpcDemoClient.instance().simpleRpc("simple stream rpc");
        System.out.println("**************************\n");
        Thread.sleep(2000);

        System.out.println("****server stream rpc***");
        RpcDemoClient.instance().serverRpc("server stream rpc");
        System.out.println("************************\n");
        Thread.sleep(2000);

        System.out.println("****client stream rpc****");
        List<String> client_messages = new ArrayList<>(5);
        for (int i = 1; i <= 5; i++) {
            client_messages.add("###client stream rpc " + i +"###");
        }
        RpcDemoClient.instance().clientRpc(client_messages);
        System.out.println("*************************\n");
        Thread.sleep(2000);

        System.out.println("******duplexing rpc*****");
        List<String> duplexing_messages = new ArrayList<>(5);
        for (int i = 1; i <= 5; i++) {
            duplexing_messages.add("#duplexing stream rpc " + i + "#");
        }
        RpcDemoClient.instance().duplexingRpc(duplexing_messages);
        System.out.println("************************\n");
    }
}
