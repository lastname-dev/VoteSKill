package com.ssacation.ssacation.user;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * User 생성
   * JPA Repository의 save Method를 사용하여 객체를 생성
   * Entity인 Model 객체에 @Id로 설정한 키 값이 없을 경우 해당하는 데이터를 추가
   * 만약 추가하려는 Entity인 Model 객체에 @Id 값이 이미 존재하면 갱신되기 때문에
   * 아래와 같이 추가하고자 하는 User가 존재하는지 체크하는 로직을 추가
   * 
   * @param model
   * @return
   */
  public UserEntity createUser(UserEntity model) {
    UserEntity creatededUser = null;
    // try {
      Optional<UserEntity> existUser = getUser(model.getId());
      if (ObjectUtils.isEmpty(existUser)) {
        creatededUser = userRepository.save(model);
      }
    // } catch (Exception e) {
    //   log.info("[Fail] e: " + e.toString());
    // }
    return creatededUser;
  }

  /**
   * User 수정
   * JPA Repository의 save Method를 사용하여 객체를 갱신
   * Entity인 Model 객체에 @Id로 설정한 키 값이 존재할 경우 해당하는 데이터를 갱신
   * 만약 수정하려는 Entity인 Model 객체에 @Id 값이 존재하지 않으면 데이터가 추가되기 때문에
   * 아래와 같이 갱신하고자 하는 User가 존재하는지 체크하는 로직을 추가
   *
   * @param model
   * @return
   */
  public UserEntity updateUser(UserEntity model) {
    UserEntity updatedUser = null;
    try {
      Optional<UserEntity> existUser = getUser(model.getId());
      if (!ObjectUtils.isEmpty(existUser)) {
        updatedUser = userRepository.save(model);
      }
    } catch (Exception e) {
      log.info("[Fail] e: " + e.toString());
    }
    return updatedUser;
  }

  /**
   * User List 조회
   * JPA Repository의 findAll Method를 사용하여 전체 User를 조회
   * 
   * @return
   */
  public List<UserEntity> getUsers() {
    return userRepository.findAll();
  }

  /**
   * Id에 해당하는 User 조회
   * JPA Repository의 findBy Method를 사용하여 특정 User를 조회
   * 
   * @param id
   * @return
   */
  public Optional<UserEntity> getUser(String id) {
    return userRepository.findById(id);
  }

  /**
   * Id에 해당하는 User 삭제
   * JPA Repository의 deleteBy Method를 사용하여 특정 User를 삭제
   * 
   * @param id
   */
  public void deleteUser(String id) {
    userRepository.deleteById(id);
  }
}