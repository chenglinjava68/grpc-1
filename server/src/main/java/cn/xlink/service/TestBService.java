package cn.xlink.service;

import cn.xlink.test.TestBReply;
import cn.xlink.test.TestBRequest;
import cn.xlink.test.TestBServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class TestBService extends TestBServiceGrpc.TestBServiceImplBase{
    public void testB(TestBRequest req, StreamObserver<TestBReply> responseObserver) {
        System.out.println("service:" + req.getName());
        TestBReply reply = TestBReply.newBuilder().setMessage(("Hello: " + req.getName())).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}