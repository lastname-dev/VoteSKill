package com.voteskill.gameserver.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SseRepository {
    // 모든 Emitters를 저장하는 ConcurrentHashMap
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>(); //userNickname, 해당 emitter
    private final Map<String, String > inGamePlayerList = new ConcurrentHashMap<>(); //gameRoomId, userNickname

    /**
     * 주어진 아이디와 이미터를 저장
     * @param userNickname     - 사용자 userNickname.
     * @param emitter - 이벤트 Emitter.
     */
    public void save(String roomId, String userNickname, SseEmitter emitter) {
        log.info("userNickname" + userNickname);
        log.info("roomId" + roomId);
        log.info("emitter" + emitter.toString());
        emitters.put(userNickname, emitter);
        inGamePlayerList.put(roomId, userNickname);
    }

    /**
     * 주어진 아이디의 Emitter를 제거
     * @param userNickname - 사용자 아이디.
     */
    public void deleteById(String userNickname) {
        emitters.remove(userNickname);
    }

    /**
     * 주어진 아이디의 Emitter를 가져옴.
     * @param userNickname - 사용자 아이디.
     * @return SseEmitter - 이벤트 Emitter.
     */
    public SseEmitter get(String userNickname) {
        return emitters.get(userNickname);
    }
}
