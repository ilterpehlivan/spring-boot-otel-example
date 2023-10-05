package org.ilt.movie.api;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.autoconfigure.GrpcReflectionServiceAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerSecurityAutoConfiguration;
import net.devh.boot.grpc.server.service.GrpcService;
import org.ilt.internal.comm.MovieOuterClass;
import org.ilt.internal.comm.MovieServiceGrpc;
import org.ilt.movie.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

@GrpcService
@Slf4j
public class MovieGrpcHandler extends MovieServiceGrpc.MovieServiceImplBase {

  @Autowired MovieService movieService;

  @Override
  public void getMovieInfo(
      MovieOuterClass.MovieRequest request,
      StreamObserver<MovieOuterClass.Movie> responseObserver) {
    String movieId = request.getMovieId();
    log.info("handling getMovie request for {}", movieId);
    MovieDTO movieDTO = movieService.findMovie(movieId);
    MovieOuterClass.Movie movieResponse =
        MovieOuterClass.Movie.newBuilder()
            .setMovieId(movieDTO.getId())
            .setDirector(movieDTO.getDirector())
            .setTitle(movieDTO.getTitle())
            .build();
    log.info("returning movie {}", movieDTO);
    responseObserver.onNext(movieResponse);
    responseObserver.onCompleted();
  }
}
