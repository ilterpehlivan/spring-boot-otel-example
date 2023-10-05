package org.ilt.movie.adapter;

import io.grpc.ServerInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GrpcServerConfig {

  @GrpcGlobalServerInterceptor
  ServerInterceptor configureServerInterceptor(OpenTelemetry opentelemetry) {
    GrpcTelemetry grpcTelemetry = GrpcTelemetry.create(opentelemetry);
    return grpcTelemetry.newServerInterceptor();
  }
}
