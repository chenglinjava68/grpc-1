package cn.xlink.start.config;

import cn.xlink.test.TestAServiceGrpc;
import cn.xlink.test.TestBServiceGrpc;
import io.grpc.ManagedChannel;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class ClientConfig {
    private TestAServiceGrpc.TestAServiceBlockingStub aBlockingStub;
    private TestBServiceGrpc.TestBServiceBlockingStub bBlockingStub;


    private final static ClientConfig singleton = new ClientConfig();

    public final static ClientConfig instance() {
        return singleton;
    }

    private ClientConfig() {
    }

    public final void init(ManagedChannel channel) {
        aBlockingStub = TestAServiceGrpc.newBlockingStub(channel);
        bBlockingStub = TestBServiceGrpc.newBlockingStub(channel);
    }

    public TestAServiceGrpc.TestAServiceBlockingStub getaBlockingStub() {
        return aBlockingStub;
    }

    public TestBServiceGrpc.TestBServiceBlockingStub getbBlockingStub() {
        return bBlockingStub;
    }

}
