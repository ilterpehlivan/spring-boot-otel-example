package org.ilt.userservice.api;

import lombok.extern.slf4j.Slf4j;
import org.ilt.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserResource {

  @Autowired UserService userService;

  // GET /UserDTOs/{UserDTOId}
  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserDTOById(@PathVariable String userId) {
    UserDTO UserDTO = userService.getUserById(userId);
    if (UserDTO != null) {
      return new ResponseEntity<>(UserDTO, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  // POST /UserDTOs
  @PostMapping
  public ResponseEntity<UserDTO> createUserDTO(@RequestBody CreateUserDTO user) {
    UserDTO createdUserDTO = userService.createUser(user);
    return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
  }

  // PUT /UserDTOs/{UserDTOId}/movie
  @PutMapping("/{userId}/movie/{movieId}")
  public ResponseEntity<UserDTO> addMovieToUserDTO(
      @PathVariable String userId, @PathVariable String movieId) {

    if (StringUtils.hasLength(movieId)) {
      UserDTO user = userService.addMovie2WatchList(userId, movieId);
      log.info("service response after adding movie to user {}", user);
      if (user == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(user, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    List<UserDTO> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }
}
