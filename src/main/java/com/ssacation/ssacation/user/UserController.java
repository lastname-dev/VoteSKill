package com.ssacation.ssacation.user;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("api/user")
@RestController
@Tag(name = "유저", description = "유저 관련 API 입니다.")
public class UserController {

  private final UserService userService;

  /**
   * Member 생성
   *
   * @return
   * @throws ParseException
   */
  @Operation(description = "유저 등록 메서드입니다.")
  @PostMapping()
  public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity newUser) throws ParseException {

    UserEntity model = UserEntity.builder()
    .id(newUser.getId())
    .password(newUser.getPassword())
    .name(newUser.getName())
    .nickname(newUser.getNickname())
    .email(newUser.getEmail())
    .birthDay(newUser.getBirthDay())
    .phoneNum(newUser.getPhoneNum())
    .build();

    UserEntity createdUser = userService.createUser(model);

    if(ObjectUtils.isEmpty(createdUser)) {

      return new ResponseEntity<>(createdUser, HttpStatus.FORBIDDEN);

    } else {

      return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }
  }
  
  /**
   * Member 수정
   *
   * @return
   * @throws ParseException
   */
  @Operation(description = "유저 수정 메서드입니다.")
  @PutMapping()
  public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity updateUser) throws ParseException {

    UserEntity model = UserEntity.builder()
    .id(updateUser.getId())
    .password(updateUser.getPassword())
    .name(updateUser.getName())
    .nickname(updateUser.getNickname())
    .email(updateUser.getEmail())
    .birthDay(updateUser.getBirthDay())
    .phoneNum(updateUser.getPhoneNum())
    .build();

    UserEntity updatedUser = userService.updateUser(model);

    if (!ObjectUtils.isEmpty(updatedUser)) {

      return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    } else {

      return new ResponseEntity<>(updatedUser, HttpStatus.NOT_FOUND);
    }
  }
  
  /**
   * Member List 조회
   *
   * @return
   */
  @Operation(description = "유저 전체 조회 메서드입니다.")
  @GetMapping()
  public ResponseEntity<List<UserEntity>> getUsers() {

    List<UserEntity> users = userService.getUsers();

    return new ResponseEntity<>(users, HttpStatus.OK);
  }
  
  /**
   * Id에 해당하는 Member 조회
   *
   * @param id
   * @return
   */
  @Operation(description = "특정 유저 조회 메서드입니다.")
  @GetMapping("{id}")
  public ResponseEntity<Optional<UserEntity>> getUser(@PathVariable("id") String id) {

    Optional<UserEntity> user = userService.getUser(id);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }
  
  /**
   * Id에 해당하는 Member 삭제
   *
   * @param id
   * @return
   */
  @Operation(description = "특정 유저 제거 메서드입니다.")
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {

    userService.deleteUser(id);
    
    return new ResponseEntity<>(id, HttpStatus.OK);
  }
}