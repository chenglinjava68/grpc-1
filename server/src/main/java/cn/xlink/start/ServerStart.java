package cn.xlink.start;

import cn.xlink.start.config.ServerConfig;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ServerStart {


    private int port = 50051;
    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerStart server = new ServerStart();
        server.start();
        server.blockUntilShutdown();
    }

    /**
     * 启动
     *
     * @throws IOException
     */
    private void start() throws IOException {
        server = ServerConfig.instance().addServer(ServerBuilder.forPort(port)).build().start();
        System.out.println("service start...");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                ServerStart.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * 停止
     */
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * block 一直到退出程序
     *
     * @throws InterruptedException
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
