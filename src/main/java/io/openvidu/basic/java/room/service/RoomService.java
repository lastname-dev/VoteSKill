package io.openvidu.basic.java.room.service;

import io.openvidu.basic.java.room.dto.RoomEnterDto;
import io.openvidu.basic.java.room.domain.Room;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
public class RoomService extends OpenVidu{
  @Autowired
  @Qualifier("roomRedisTemplate")
  private RedisTemplate roomRedisTemplate;
  private SetOperations<String,Room> setOperations;
//  private HashOperations<String,String,Room> hashOperations;
  private final String roomKeyPrefix = "room";
//  private final ValueOperations<String,Room> stringRoomValueOperations;

  @Autowired
  public RoomService(@Value("${OPENVIDU_URL}")String hostname, @Value("${OPENVIDU_SECRET}")String secret
      ) {
    super(hostname, secret);
//    this.redisTemplate = redisTemplate;
//    this.stringRoomValueOperations = redisTemplate.opsForValue();
//    this.setOperations = redisTemplate.opsForSet();
//    this.hashOperations = redisTemplate.opsForHash();
  }



  @Override
  public Session createSession() throws OpenViduJavaClientException, OpenViduHttpException {

    Session session = super.createSession();

    return session;
  }
  @Override
  public Session createSession(SessionProperties properties) throws OpenViduJavaClientException, OpenViduHttpException {
    Session session = super.createSession(properties);
    return session;
  }

  public Room createRoom(Map<String,Object> roomInfo){

    String name = (String) roomInfo.get("customSessionId");
    String password = (String) roomInfo.get("password");
    String nickname = (String)roomInfo.get("nickname");

//    Room room = new Room(name,password,admitNumber,redisTemplate);
    Room room = new Room(name,password,1,nickname);
//    redisTemplate.opsForValue().set(roomKeyPrefix+name,room);
    roomRedisTemplate.opsForHash().put("room",name,room);
//    stringRoomValueOperations.set(roomKeyPrefix+name,room);
    return room;

  }
  public Room joinRoom(RoomEnterDto roomEnterDto) throws Exception {
    Room room = (Room) roomRedisTemplate.opsForHash().get("room", roomEnterDto.getRoomName());
    log.info("입장 방 정보 : {}",room.getName());
    if(room == null){
      throw new Exception();
    }
    log.info("입장 하려는 방:{} , 입력한 비밀번호:{}",room.getPassword(),roomEnterDto.getPassword());
    if(room.getPassword().equals("") || room.getPassword().equals(roomEnterDto.getPassword())){
      log.info("인원추가 :{}",roomEnterDto.getNickname() );
      room.getPeople().add(roomEnterDto.getNickname());
    }else{
      throw new AuthenticationException("비밀번호가 틀렸습니다");
    }
    roomRedisTemplate.opsForHash().put(roomKeyPrefix,roomEnterDto.getRoomName(),room);
    return room;
  }
  public List<Room> getRooms(){
    List<Room> rooms = roomRedisTemplate.opsForHash().values(roomKeyPrefix);
    return rooms;
  }
  public Room getRoom(String roomName){
//    Room room = (Room) redisTemplate.opsForValue().get(roomKeyPrefix+roomName);
    Room room = (Room) roomRedisTemplate.opsForHash().get(roomKeyPrefix, roomName);
    log.info("getRoom : {}",room);
    return room;
  }
  public void exitRoom(String nickname,String roomName) {
    Room room = getRoom(roomName);
    List<String> people = room.getPeople();
    for(String person : people){
      log.info("{} 에 있는 사람 : {}",roomName,person);
      if(person.equals(nickname)){
        people.remove(person);
      }
      if(people.size()==0)
        break;
    }
    roomRedisTemplate.opsForHash().put(roomKeyPrefix,roomName,room);
  }


  @Override
  public Session getActiveSession(String sessionId) {
    return super.getActiveSession(sessionId);
  }

  @Override
  public List<Session> getActiveSessions() {
    return super.getActiveSessions();
  }
}
