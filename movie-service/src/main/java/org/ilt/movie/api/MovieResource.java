package org.ilt.movie.api;

import lombok.extern.slf4j.Slf4j;
import org.ilt.movie.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieResource {

  @Autowired MovieService movieService;

  @PostMapping
  public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movie) {
    log.info("creating movie {}", movie);
    MovieDTO movieDto = movieService.createMovie(movie);
    return new ResponseEntity<>(movieDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<MovieDTO>> getAllMovies() {
    List<MovieDTO> movies = movieService.findAllMovies();

    return new ResponseEntity<>(movies, HttpStatus.OK);
  }
}
