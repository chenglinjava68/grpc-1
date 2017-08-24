package cn.sic777.service;

import cn.sic777.test.TestAReply;
import cn.sic777.test.TestARequest;
import cn.sic777.test.TestAServiceGrpc;
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