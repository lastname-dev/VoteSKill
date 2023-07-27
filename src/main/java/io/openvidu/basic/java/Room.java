package io.openvidu.basic.java;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "room", timeToLive = 3600) // timeToLive 설정 가능.
public class Room {

  private String id;
  @Id
//  @Indexed
  private String name;
  private String password;
  private String[] person;
  public Room(String name, String password) {
    this.name = name;
    this.password = password;
  }
}
