package com.kivilev.service;

import com.kivilev.protobuf.generated.SmsSendServiceGrpc;
import com.kivilev.service.grpc.mapper.GrpcObjectMapper;
import com.kivilev.service.model.Sms;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class CommonSmsGrpcServiceImpl implements CommonSmsService {
    private static final String GRPC_CLIENT_PARAM_NAME = "common-sms-service";

    @GrpcClient(GRPC_CLIENT_PARAM_NAME)
    private SmsSendServiceGrpc.SmsSendServiceBlockingStub smsSendServiceStub;
    private final GrpcObjectMapper grpcObjectMapper;

    public CommonSmsGrpcServiceImpl(GrpcObjectMapper grpcObjectMapper) {
        this.grpcObjectMapper = grpcObjectMapper;
    }

    @Override
    public Sms sendSms(Sms sms) {
        var request = grpcObjectMapper.toSendSmsRequest(sms);
        var smsStatusResponse = smsSendServiceStub.sendSms(request);
        return grpcObjectMapper.toSms(smsStatusResponse);
    }

    @Override
    public List<Sms> getSmsMessages(Long clientId, int limit) {
        var request = grpcObjectMapper.toSmsStatusMessageRequest(clientId, limit);
        var smsStatusResponses = smsSendServiceStub.getSmsMessageStatus(request);
        return convertIteratorToList(smsStatusResponses, grpcObjectMapper::toSms);
    }

    public static <ResponseT, EntityT> List<EntityT> convertIteratorToList(Iterator<ResponseT> iterator,
                                                                           Function<ResponseT, EntityT> mapper) {
        return Stream.generate(() -> null)
                .takeWhile(x -> iterator.hasNext())
                .map(n -> iterator.next())
                .map(mapper)
                .toList();
    }
}
