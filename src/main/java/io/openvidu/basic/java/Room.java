package io.openvidu.basic.java;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

@Getter
@Setter
@RedisHash("room") // timeToLive 설정 가능.
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {

  //todo: 현재는 중복 이름의 방은 못 만듬.
  @Id
//  @Indexed
  private String name;
  private String password = "";
  private String host;
  private int admitNumber;
//  private List<Person> people;
  private List<String> people;
  public Room(String name, String password,int admitNumber, String host) {
    this.name = name;
    this.password = password;
    this.admitNumber = admitNumber;
    this.host=host;
    this.people= new ArrayList<>();
  }
//  public void addPerson(Person person){
//    people.add(person);
//  }
}
