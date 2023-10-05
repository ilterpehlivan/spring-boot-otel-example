package org.ilt.userservice.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class MovieDTO {
  private String id;
  private String title;
  private String director;
  private String genre;

  public static MovieDTO of(String mId) {
    MovieDTO dto = new MovieDTO();
    dto.setId(mId);
    return dto;
  }
}
