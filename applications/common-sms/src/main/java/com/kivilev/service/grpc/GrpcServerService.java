package com.kivilev.service.grpc;

import com.kivilev.protobuf.generated.GetSmsStatusMessageRequest;
import com.kivilev.protobuf.generated.SendSmsRequest;
import com.kivilev.protobuf.generated.SmsMessageResponse;
import com.kivilev.protobuf.generated.SmsSendServiceGrpc;
import com.kivilev.service.SmsService;
import com.kivilev.service.grpc.mapper.GrpcObjectMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcServerService extends SmsSendServiceGrpc.SmsSendServiceImplBase {

    private final SmsService smsService;
    private final GrpcObjectMapper grpcObjectMapper;

    public GrpcServerService(SmsService smsService, GrpcObjectMapper grpcObjectMapper) {
        this.smsService = smsService;
        this.grpcObjectMapper = grpcObjectMapper;
    }

    @Override
    public void sendSms(SendSmsRequest request,
                        StreamObserver<SmsMessageResponse> responseObserver) {
        var processedSms = smsService.processIncomingSmsRequest(grpcObjectMapper.toSms(request));
        responseObserver.onNext(grpcObjectMapper.toSmsMessageResponse(processedSms));
        responseObserver.onCompleted();
    }

    public void getSmsMessageStatus(GetSmsStatusMessageRequest request,
                                    StreamObserver<SmsMessageResponse> responseObserver) {
        var smsMessages = smsService.getSmsMessages(request.getClientId(), request.getLimit());

        smsMessages.stream()
                .map(grpcObjectMapper::toSmsMessageResponse)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
