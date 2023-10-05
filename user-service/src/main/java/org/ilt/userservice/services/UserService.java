package org.ilt.userservice.services;

import io.grpc.ClientInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.autoconfigure.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.ilt.internal.comm.MovieOuterClass;
import org.ilt.internal.comm.MovieServiceGrpc;
import org.ilt.userservice.api.CreateUserDTO;
import org.ilt.userservice.api.MovieDTO;
import org.ilt.userservice.api.UserDTO;
import org.ilt.userservice.repository.User;
import org.ilt.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ImportAutoConfiguration({
  GrpcClientAutoConfiguration.class,
  GrpcClientMetricAutoConfiguration.class,
  GrpcClientHealthAutoConfiguration.class,
  GrpcClientSecurityAutoConfiguration.class,
  GrpcClientTraceAutoConfiguration.class,
  GrpcDiscoveryClientAutoConfiguration.class
})
public class UserService {

  private final UserRepository userRepository;

  @GrpcClient("movie-client")
  private MovieServiceGrpc.MovieServiceBlockingStub movieServiceStub;

  @Autowired private Tracer tracer;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDTO createUser(CreateUserDTO user) {
    return map2DTO(userRepository.save(map2Entity(user)));
  }

  public User updateUser(UserDTO user) {
    return userRepository.save(map2Entity(user));
  }

  public UserDTO getUserById(String userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return null;
    }
    UserDTO userDTO = map2DTO(user);
    log.info(
        "fetching movie details for user {} movie list {}",
        userDTO.getId(),
        userDTO.getWatchList());
    Span span = tracer.spanBuilder("movie-service-span").startSpan();
    for (MovieDTO m : userDTO.getWatchList()) {
      MovieOuterClass.Movie movieInfo;
      try (Scope scope = span.makeCurrent()) {
        // each movie in the list we get details from movie service
        movieInfo =
            movieServiceStub.getMovieInfo(
                MovieOuterClass.MovieRequest.newBuilder().setMovieId(m.getId()).build());
      } catch (Throwable t) {
        span.setStatus(StatusCode.ERROR, "received error while calling movie service");
        span.recordException(t);
        throw t;
      } finally {
        span.end(); // Cannot set a span after this call
      }
      m.setDirector(movieInfo.getDirector());
      m.setTitle(movieInfo.getTitle());
      //      m.setGenre(mo); //TODO
    }
    return userDTO;
  }

  public List<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(this::map2DTO).toList();
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  private UserDTO map2DTO(User entity) {
    UserDTO dto = new UserDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getUsername());
    dto.setEmail(entity.getEmail());
    if (entity.getMoviesToWatch() != null) {
      for (String mId : entity.getMoviesToWatch()) {
        dto.getWatchList().add(MovieDTO.of(mId));
      }
    }
    return dto;
  }

  private User map2Entity(CreateUserDTO user) {
    return User.of(user.getName(), user.getEmail());
  }

  private User map2Entity(UserDTO user) {
    return User.of(user.getName(), user.getEmail());
  }

  public UserDTO addMovie2WatchList(String userId, String movieId) {
    Optional<User> user = userRepository.findById(userId);

    return user.stream()
        .peek(
            u -> {
              List<String> moviesToWatch = u.getMoviesToWatch();
              log.info("adding movie {} to user list {}", movieId, moviesToWatch);
              moviesToWatch.add(movieId);
            })
        .flatMap(
            t -> {
              User x = userRepository.save(t);
              return Stream.of(x);
            })
        .findFirst()
        .map(this::map2DTO)
        .orElse(null);
  }
}
