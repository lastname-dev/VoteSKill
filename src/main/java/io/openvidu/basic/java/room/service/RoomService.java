package io.openvidu.basic.java.room.service;

import io.openvidu.basic.java.room.Dto.RoomEnterDto;
import io.openvidu.basic.java.Room;
import io.openvidu.basic.java.room.repository.RoomRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@Transactional
public class RoomService extends OpenVidu{
  private RoomRepository roomRepository;
  private RedisTemplate redisTemplate;
  private SetOperations<String,Room> setOperations;
  private HashOperations<String,String,Room> hashOperations;
  private final String roomKeyPrefix = "room:";
  private final ValueOperations<String,Room> stringRoomValueOperations;

  @Autowired
  public RoomService(@Value("${OPENVIDU_URL}")String hostname, @Value("${OPENVIDU_SECRET}")String secret,RoomRepository roomRepository,
      RedisTemplate redisTemplate) {
    super(hostname, secret);
    this.roomRepository= roomRepository;
    this.redisTemplate = redisTemplate;
    this.stringRoomValueOperations = redisTemplate.opsForValue();
    this.setOperations = redisTemplate.opsForSet();
    this.hashOperations = redisTemplate.opsForHash();
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
//    int admitNumber = (int) roomInfo.get("admitNumber");

    //todo: 사용자 정보가지고 와서 Room host와 people에 넣어주기.
//    Room room = new Room(name,password,admitNumber,redisTemplate);
    Room room = new Room(name,password,1,redisTemplate);
//    redisTemplate.opsForValue().set(roomKeyPrefix+name,room);
    hashOperations.put("room",name,room);
//    stringRoomValueOperations.set(roomKeyPrefix+name,room);
    return room;

  }
  public Room joinRoom(RoomEnterDto roomEnterDto) throws Exception {
    Room room =  hashOperations.get("room", roomEnterDto.getRoomName());
    log.info("입장 방 정보 : {}",room.getName());
    if(room == null){
      throw new Exception();
    }
    log.info("입장 하려는 방:{} , 입력한 비밀번호:{}",room.getPassword(),roomEnterDto.getPassword());
    if(room.getPassword()==null || room.getPassword().equals(roomEnterDto.getPassword())){
      room.getPeople().add("안녕");
    }else{
      throw new AuthenticationException("비밀번호가 틀렸습니다");
    }
    redisTemplate.opsForValue().set(roomKeyPrefix+roomEnterDto.getRoomName(),room);
    return room;
  }
  public List<Room> getRooms(){
    List<Room> rooms = hashOperations.values("room");
    return rooms;
  }
  public Room getRoom(String roomName){
//    Room room = (Room) redisTemplate.opsForValue().get(roomKeyPrefix+roomName);
    Room room = hashOperations.get("room", roomName);
    log.info("getRoom : {}",room);
    return room;
  }
  public void exitRoom(String roomName) {
      List<String> people = getRoom(roomName).getPeople();
      //todo : people에서 해당 사람 삭제
  }
  public void deleteRoom(String roomName){
    roomRepository.deleteByName(roomName);
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
