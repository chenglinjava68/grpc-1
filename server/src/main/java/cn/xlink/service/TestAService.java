package cn.xlink.service;

import cn.xlink.test.TestAReply;
import cn.xlink.test.TestARequest;
import cn.xlink.test.TestAServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class TestAService extends TestAServiceGrpc.TestAServiceImplBase {
    @Override
    public void testA(TestARequest request, StreamObserver<TestAReply> responseObserver) {
        System.out.println("service:" + request.getName());
        TestAReply reply = TestAReply.newBuilder().setMessage(("Hello: " + request.getName())).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}