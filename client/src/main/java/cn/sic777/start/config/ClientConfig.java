package cn.sic777.start.config;

import cn.sic777.protobuf.RpcDemoGrpc;
import io.grpc.ManagedChannel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class ClientConfig {
    private RpcDemoGrpc.RpcDemoBlockingStub rpcDemoBlockingStub;//阻塞/同步存根
    private RpcDemoGrpc.RpcDemoStub rpcDemoStub;//非阻塞,异步存根


    private final static ClientConfig singleton = new ClientConfig();

    public final static ClientConfig instance() {
        return singleton;
    }

    private ClientConfig() {
    }


    /**
     * 加载配置
     *
     * @param configPath
     * @return
     */
    public final Properties load(String configPath) {
        FileInputStream file = null;
        Properties pro = new Properties();
        try {
            file = new FileInputStream(configPath);
            pro.load(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != file) {
                try {
                    file.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return pro;
    }

    /**
     * 初始化
     *
     * @param channel
     */
    public final void init(ManagedChannel channel) {
        rpcDemoBlockingStub = RpcDemoGrpc.newBlockingStub(channel);
        rpcDemoStub = RpcDemoGrpc.newStub(channel);
    }

    public RpcDemoGrpc.RpcDemoBlockingStub getRpcDemoBlockingStub() {
        return rpcDemoBlockingStub;
    }

    public RpcDemoGrpc.RpcDemoStub getRpcDemoStub() {
        return rpcDemoStub;
    }
}
