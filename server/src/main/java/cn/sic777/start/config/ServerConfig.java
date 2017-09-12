package cn.sic777.start.config;

import cn.sic777.service.RpcDemoService;
import io.grpc.BindableService;
import io.grpc.ServerBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
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
        serviceList.add(new RpcDemoService());
    }

    /**
     * 加载配置
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

                }
            }
        }
        return pro;
    }

    /**
     * 注册服务
     *
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
