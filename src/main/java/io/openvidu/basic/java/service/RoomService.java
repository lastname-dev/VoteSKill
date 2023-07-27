package io.openvidu.basic.java.service;

import io.openvidu.basic.java.Room;
import io.openvidu.basic.java.repository.RoomRepository;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class RoomService extends OpenVidu{


//  @Autowired
//  public void setRoomRepository(RoomRepository roomRepository) {
//    this.roomRepository = roomRepository;
//  }

//  @Autowired
  private RoomRepository roomRepository;

  private StringRedisTemplate redisTemplate;

  @Autowired
  public RoomService(@Value("${OPENVIDU_URL}")String hostname, @Value("${OPENVIDU_SECRET}")String secret,RoomRepository roomRepository,
      StringRedisTemplate redisTemplate) {
    super(hostname, secret);
    this.roomRepository= roomRepository;
    this.redisTemplate = redisTemplate;
  }



  @Override
  public Session createSession() throws OpenViduJavaClientException, OpenViduHttpException {

    Session session = super.createSession();

    return session;
  }
  @Override
  public Session createSession(SessionProperties properties) throws OpenViduJavaClientException, OpenViduHttpException {
//    Room room = new Room();
//    roomRepository.save();
    Session session = super.createSession(properties);
    return session;
  }
  public Room createRoom(Map<String,Object> roomInfo){
    String password = (String) roomInfo.get("password");
    log.info("세션 이름: {}" ,(String)roomInfo.get("customSessionId"));
    String name = (String) roomInfo.get("customSessionId");
    Room room = new Room(name,password);
    roomRepository.save(room);
    return room;
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
