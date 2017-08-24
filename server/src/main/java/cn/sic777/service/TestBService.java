package cn.sic777.service;

import cn.sic777.test.TestBReply;
import cn.sic777.test.TestBRequest;
import cn.sic777.test.TestBServiceGrpc;
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