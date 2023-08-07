package com.voteskill.gameserver.sse;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SseRepository {

    // 방별로 유저 이름과 SSE Emitter를 매핑하는 맵
    private Map<String, Map<String, SseEmitter>> emittersByRoomId = new HashMap<>(); //방이름, 유저이름, 에미터

    /**
     * 특정 방에 유저 이름과 SSE Emitter를 매핑하여 저장하기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @param emitter - SSE Emitter
     */
    public void save(String roomId, String userNickname, SseEmitter emitter) {
        emittersByRoomId.computeIfAbsent(roomId, key -> new HashMap<>()).put(userNickname, emitter);
        printEmittersByRoomId();
    }

    //출력용
    public void printEmittersByRoomId() {
        for (Map.Entry<String, Map<String, SseEmitter>> entry : emittersByRoomId.entrySet()) {
            String roomId = entry.getKey();
            Map<String, SseEmitter> emittersInRoom = entry.getValue();

            System.out.println("Room ID: " + roomId);

            for (Map.Entry<String, SseEmitter> emitterEntry : emittersInRoom.entrySet()) {
                String userNickname = emitterEntry.getKey();
                SseEmitter emitter = emitterEntry.getValue();

                System.out.println("  User Nickname: " + userNickname);
                System.out.println("  SseEmitter: " + emitter);

            }
        }
    }

    /**
     * 특정 방의 모든 유저 이름에 해당하는 SSE Emitter들을 가져오기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @return 특정 방의 해당 유저 이름에 대한 SSE Emitter들의 목록
     */
    public Map<String, SseEmitter> getAllEmittersByRoomAndUser(String roomId, String userNickname) {
        return emittersByRoomId.getOrDefault(roomId, new HashMap<>());
    }

    /**
     * 특정 방의 모든 SSE Emitter들을 가져오기
     * @param roomId - 방 ID
     * @return 특정 방의 모든 SSE Emitter들의 목록
     */
    public Map<String, SseEmitter> getAllEmittersByRoom(String roomId) {
        return emittersByRoomId.getOrDefault(roomId, new HashMap<>());
    }

    /**
     * 특정 방의 특정 유저 이름에 해당하는 SSE Emitter를 가져오기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @return 특정 방의 해당 유저 이름에 대한 SSE Emitter
     */
    public SseEmitter getByRoomAndUser(String roomId, String userNickname) {
        return emittersByRoomId.getOrDefault(roomId, new HashMap<>()).get(userNickname);
    }

    /**
     * 특정 방의 특정 유저 이름에 해당하는 SSE Emitter를 삭제하기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     */
    public void delete(String roomId, String userNickname) {
        emittersByRoomId.getOrDefault(roomId, new HashMap<>()).remove(userNickname);
    }

    /**
     * 특정 방의 모든 SSE Emitter를 삭제하기
     * @param roomId - 방 ID
     */
    public void deleteAll(String roomId) {
        emittersByRoomId.remove(roomId);
    }
}
