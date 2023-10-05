package org.ilt.movie.api;

import net.devh.boot.grpc.server.autoconfigure.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

@ImportAutoConfiguration({
  GrpcAdviceAutoConfiguration.class,
  GrpcHealthServiceAutoConfiguration.class,
  GrpcReflectionServiceAutoConfiguration.class,
  GrpcServerAutoConfiguration.class,
  GrpcServerFactoryAutoConfiguration.class,
  GrpcServerSecurityAutoConfiguration.class,
  GrpcServerMetricAutoConfiguration.class,
  GrpcServerTraceAutoConfiguration.class
})
@org.springframework.context.annotation.Configuration(proxyBeanMethods = false)
public class GrpcConfiguration {}
