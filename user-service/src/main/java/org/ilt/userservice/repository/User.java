package org.ilt.userservice.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;

@Document(collection = "users") // Specify the MongoDB collection name
@Data
public class User {
  @Id private String id;
  private String username;
  private String email;
  private List<String> moviesToWatch;

  public static User of(String name, String email) {
    User user = new User();
    user.setEmail(email);
    user.setUsername(name);
    user.setMoviesToWatch(Collections.emptyList());
    return user;
  }
}
