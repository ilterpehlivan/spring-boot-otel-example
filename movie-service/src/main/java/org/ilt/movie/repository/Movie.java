package org.ilt.movie.repository;

import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movies")
@Data
public class Movie {
  @Id private String id;
  private String title;
  private String director;
  private String genre;

  public static Movie of(String title, String director,String genre) {
    Movie movie = new Movie();
    movie.setDirector(director);
    movie.setGenre(genre);
    movie.setTitle(title);
    return movie;
  }
}
