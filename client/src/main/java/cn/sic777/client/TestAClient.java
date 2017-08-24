package cn.sic777.client;

import cn.sic777.start.config.ClientConfig;
import cn.xlink.test.TestAReply;
import cn.xlink.test.TestARequest;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class TestAClient {
    private TestAClient(){}
    private final static TestAClient singleton = new TestAClient();
    public final static TestAClient instance(){
        return singleton;
    }
    public void test(String name) {
        TestARequest request = TestARequest.newBuilder().setName(name).build();
        TestAReply response = ClientConfig.instance().getaBlockingStub().testA(request);
        System.out.println(response.getMessage());
    }
}
