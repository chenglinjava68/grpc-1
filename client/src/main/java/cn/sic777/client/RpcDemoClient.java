package cn.sic777.client;

import cn.sic777.protobuf.RequestParams;
import cn.sic777.protobuf.ResponseParams;
import cn.sic777.start.config.ClientConfig;
import io.grpc.stub.StreamObserver;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Zhengzhenxie on 2017/8/23.
 */
public class RpcDemoClient {
    private RpcDemoClient() {
    }

    private final static RpcDemoClient singleton = new RpcDemoClient();

    public final static RpcDemoClient instance() {
        return singleton;
    }

    /**
     * 简单prc
     *
     * @param message
     * @return
     */
    public void simpleRpc(String message) {
        RequestParams request = RequestParams.newBuilder().setMessage(message).build();
        ResponseParams response = ClientConfig.instance().getRpcDemoBlockingStub().simpleRpc(request);
        System.out.println(response.getMessage());
    }

    /**
     * 服务器流式rpc
     *
     * @param message
     */
    public void serverRpc(String message) {
        RequestParams request = RequestParams.newBuilder().setMessage(message).build();
        Iterator<ResponseParams> it = ClientConfig.instance().getRpcDemoBlockingStub().serverRpc(request);
        ResponseParams response;
        while (it.hasNext()) {
            response = it.next();
            System.out.println(response.getMessage());
        }
    }

    /**
     * 客户端流式rpc
     *
     * @param messages
     */
    public void clientRpc(List<String> messages) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        //建一个应答者接受返回数据
        StreamObserver<ResponseParams> responseObserver = new StreamObserver<ResponseParams>() {
            @Override
            public void onNext(ResponseParams responseParams) {
                System.out.println(responseParams.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("failed");
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("##########finish#########");
                finishLatch.countDown();
            }
        };
        //客户端写入操作
        StreamObserver<RequestParams> requestObserver = ClientConfig.instance().getRpcDemoStub().clientRpc(responseObserver);
        try {
            for (int i = 0; i < messages.size(); ++i) {
                String message = messages.get(i);
                System.out.println(message);
                requestObserver.onNext(RequestParams.newBuilder().setMessage(message).build());
                Thread.sleep(1000);
                if (finishLatch.getCount() == 0) {
                    return;
                }
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        //标识已经写完
        requestObserver.onCompleted();
        // Receiving happens asynchronously
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            System.out.println("can not finish within 1 minutes");
        }
    }

    /**
     * 双工rpc
     *
     * @param messages
     * @throws InterruptedException
     */
    public void duplexingRpc(List<String> messages) throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        //建一个应答者接受返回数据
        StreamObserver<ResponseParams> responseObserver = new StreamObserver<ResponseParams>() {
            @Override
            public void onNext(ResponseParams responseParams) {
                System.out.println(responseParams.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("encountered error");
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("##########finish########");
                finishLatch.countDown();
            }
        };
        //客户端写入操作
        StreamObserver<RequestParams> requestObserver = ClientConfig.instance().getRpcDemoStub().duplexingRpc(responseObserver);
        try {
            for (int i = 0; i < messages.size(); ++i) {
                String message = messages.get(i);
                System.out.println(message);
                requestObserver.onNext(RequestParams.newBuilder().setMessage(message).build());
                Thread.sleep(1000);
                if (finishLatch.getCount() == 0) {
                    return;
                }
            }
        } catch (RuntimeException e) {
            requestObserver.onError(e);
            throw e;
        }
        //标识已经写完
        requestObserver.onCompleted();
        // Receiving happens asynchronously
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            System.out.println("can not finish within 1 minutes");
        }
    }


}
