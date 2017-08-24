package cn.xlink.client;

import cn.xlink.test.TestAReply;
import cn.xlink.test.TestARequest;
import cn.xlink.test.TestAServiceGrpc;
import cn.xlink.test.TestBServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;


public class TestClient {

    private final ManagedChannel channel;
    private final TestAServiceGrpc.TestAServiceBlockingStub aBlockingStub;
    private final TestBServiceGrpc.TestBServiceBlockingStub bBlockingStub;



    public TestClient(String host, int port){
        channel = ManagedChannelBuilder.forAddress(host,port)
                .usePlaintext(true)
                .build();

        aBlockingStub = TestAServiceGrpc.newBlockingStub(channel);
        bBlockingStub = TestBServiceGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }



    public static void main(String[] args) throws InterruptedException {
        TestClient client = new TestClient("127.0.0.1",50051);
        for(int i=0;i<5;i++){
            client.greet("world:"+i);
        }
    }


    public  void greet(String name){
        TestARequest request = TestARequest.newBuilder().setName(name).build();
        TestAReply response = aBlockingStub.testA(request);
        System.out.println(response.getMessage());
    }
}