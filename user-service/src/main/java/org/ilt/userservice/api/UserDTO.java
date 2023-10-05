package org.ilt.userservice.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class UserDTO {
  private String id;
  private String name;
  private String email;
  private List<MovieDTO> watchList;

  public UserDTO() {
    watchList = new ArrayList<>();
  }
}
