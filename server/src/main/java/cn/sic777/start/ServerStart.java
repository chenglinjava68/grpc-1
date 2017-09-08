package cn.sic777.start;

import cn.sic777.start.args.ServerArgs;
import cn.sic777.start.config.ServerConfig;
import cn.sic777.start.contants.ServerContant;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Properties;

public class ServerStart {
    private static int SERVER_PORT;

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerArgs.load(args);
        Properties pro = ServerConfig.instance().load(ServerArgs.CONFIG_PATH);
        SERVER_PORT = Integer.parseInt(pro.getProperty(ServerContant.PRC_SERVER_PORT, "10000"));
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
        server = ServerConfig.instance().addServer(ServerBuilder.forPort(SERVER_PORT)).build().start();
        System.out.println("service start in port [" + server.getPort() + "]...");
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
