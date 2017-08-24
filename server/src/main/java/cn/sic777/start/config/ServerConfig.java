package cn.sic777.start.config;

import cn.sic777.service.TestAService;
import cn.sic777.service.TestBService;
import io.grpc.BindableService;
import io.grpc.ServerBuilder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class ServerConfig {
    private List<BindableService> serviceList = new CopyOnWriteArrayList<>();

    private ServerConfig() {
        init();
    }

    private final static ServerConfig singleton = new ServerConfig();

    public final static ServerConfig instance() {
        return singleton;
    }

    /**
     * 初始化
     */
    private void init() {
        serviceList.add(new TestAService());
        serviceList.add(new TestBService());
    }

    /**
     * 注册服务
     * @param server
     * @return
     */
    public final ServerBuilder addServer(ServerBuilder server) {
        for (BindableService service : serviceList) {
            server.addService(service);
        }
        return server;
    }
}
