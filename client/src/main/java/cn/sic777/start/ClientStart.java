package cn.sic777.start;

import cn.sic777.client.TestAClient;
import cn.sic777.start.config.ClientConfig;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class ClientStart {
    private ManagedChannel channel;

    private final void shutdown() throws InterruptedException {
        channel.awaitTermination(5, TimeUnit.SECONDS);
    }

    private final void start(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
        ClientConfig.instance().init(channel);
    }

    public static void main(String[] args) throws InterruptedException {
        ClientStart start = new ClientStart();
        start.start("127.0.0.1", 50051);
        TestAClient.instance().test("xx");
        start.shutdown();
    }
}
