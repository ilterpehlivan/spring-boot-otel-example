package org.ilt.movie.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MovieDTO {
  private String id;
  private String title;
  private String director;
  private String genre;
}
