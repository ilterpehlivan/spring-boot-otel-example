package org.ilt.userservice.adapter;

import io.grpc.ClientInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GrpcClientConfig {
    @GrpcGlobalClientInterceptor
    public ClientInterceptor configureClientInterceptor(OpenTelemetry opentelemetry) {
        GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(opentelemetry);
        return grpcTelemetry.newClientInterceptor();
    }
}
