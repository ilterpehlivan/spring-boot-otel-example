package org.ilt.movie.services;

import org.ilt.movie.api.MovieDTO;
import org.ilt.movie.repository.Movie;
import org.ilt.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
  @Autowired MovieRepository movieRepository;

  public MovieDTO createMovie(MovieDTO movie) {
    return map2Dto(movieRepository.save(map2Entity(movie)));
  }

  private MovieDTO map2Dto(Movie movieEntity) {
    MovieDTO movieDTO = new MovieDTO();
    movieDTO.setId(movieEntity.getId());
    movieDTO.setDirector(movieEntity.getDirector());
    movieDTO.setTitle(movieEntity.getTitle());
    movieDTO.setGenre(movieEntity.getGenre());
    return movieDTO;
  }

  private Movie map2Entity(MovieDTO movie) {
    return Movie.of(movie.getTitle(), movie.getDirector(), movie.getGenre());
  }

  public MovieDTO findMovie(String movieId) {
    return movieRepository.findById(movieId).map(this::map2Dto).orElse(null);
  }

  public List<MovieDTO> findAllMovies() {
    return movieRepository.findAll().stream().map(this::map2Dto).toList();
  }
}
