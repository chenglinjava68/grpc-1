package cn.sic777.service;

import cn.sic777.protobuf.RequestParams;
import cn.sic777.protobuf.ResponseParams;
import cn.sic777.protobuf.RpcDemoGrpc;
import io.grpc.stub.StreamObserver;


/**
 * Created by Zhengzhenxie on 2017/9/8.
 */
public class RpcDemoService extends RpcDemoGrpc.RpcDemoImplBase {
    /**
     * 简单prc
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void simpleRpc(RequestParams request, StreamObserver<ResponseParams> responseObserver) {
        System.out.println("request param: " + request.getMessage()+"\n");
        ResponseParams response = ResponseParams.newBuilder().setMessage("######response param######").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    /**
     * 服务端流式rpc
     *
     * @param request
     * @param responseObserver
     */
    @Override
    public void serverRpc(RequestParams request, StreamObserver<ResponseParams> responseObserver) {
        System.out.println("request param: " + request.getMessage()+"\n");
        ResponseParams response;
        for (int i = 1, size = 5; i <= size; i++) {
            response = ResponseParams.newBuilder().setMessage("####response param " + i + "####").build();
            responseObserver.onNext(response);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        responseObserver.onCompleted();
    }

    /**
     * 客户端流式rpc
     *
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<RequestParams> clientRpc(StreamObserver<ResponseParams> responseObserver) {
        return new StreamObserver<RequestParams>() {
            //客户端每写入一个requestParams,服务端就会调用该方法
            @Override
            public void onNext(RequestParams requestParams) {
                System.out.println("request param: " + requestParams.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                System.err.println("encountered error");
            }

            //客户端写入结束时调用
            @Override
            public void onCompleted() {
                System.out.println("finish\n");
                responseObserver.onNext(ResponseParams.newBuilder().setMessage("#####response param######").build());
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 双工rpc
     *
     * @param responseObserver
     * @return
     */
    @Override
    public StreamObserver<RequestParams> duplexingRpc(StreamObserver<ResponseParams> responseObserver) {
        return new StreamObserver<RequestParams>() {
            @Override
            public void onNext(RequestParams requestParams) {
                System.out.println("request param: " + requestParams.getMessage());
                responseObserver.onNext(ResponseParams.newBuilder().setMessage("#####response param#####").build());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                System.err.println("encountered error");
            }

            @Override
            public void onCompleted() {
                System.out.println("finish\n");
                responseObserver.onCompleted();
            }
        };
    }
}
