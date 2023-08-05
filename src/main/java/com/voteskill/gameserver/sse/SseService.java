package com.voteskill.gameserver.sse;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final SseRepository sseRepository;

//    /**
//     * 클라이언트(플레이어)가 방 입장을 위해 호출하는 메서드.
//     * @param userNickname - 플레이어의 닉네임, roomId - 입장한 방 아이디
//     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
//     */
//    public SseEmitter connect(String roomId, String userNickname) {
//        SseEmitter emitter = createEmitter(roomId, userNickname ) ;
//        sendToClient(roomId, userNickname, "EventStream Created. [" + userNickname + " entered " + roomId + " .]"); //초기 연결 시 에러 방지를 위한 더미데이터
//        return emitter;
//    }
//
//    /**
//     * 서버의 이벤트를 클라이언트에게 보내는 메서드
//     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
//     * @param userNickname - 메세지를 전송할 사용자의 아이디.
//     * @param event  - 전송할 이벤트 객체.
//     */
//    public void notify(String roomId, String userNickname, Object event) {
//        sendToClient(roomId, userNickname, event);
//    }


    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     * @param userNickname - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    public SseEmitter createEmitter(String roomId, String userNickname) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT );
        sseRepository.save(roomId, userNickname, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> sseRepository.delete(roomId, userNickname));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> sseRepository.delete(roomId, userNickname));

        return emitter;
    }

    /**
     * 특정 방에 있는 모든 클라이언트들에게 같은 정보를 동시에 보내기
     * @param roomId - 방 ID
     * @param data - 보낼 데이터
     */
    public void notifyAllInRoom(String roomId, String data) {
        Map<String, SseEmitter> emittersByUser = sseRepository.getAllEmittersByRoom(roomId);
        for (SseEmitter emitter : emittersByUser.values()) {
            sendToEmitter(emitter, data);
        }
    }

    /**
     * 특정 방에 있는 한 클라이언트에게 특정한 정보를 보내기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @param data - 보낼 데이터
     */
    public void notifyClient(String roomId, String userNickname, String data) {
        SseEmitter emitter = sseRepository.getByRoomAndUser(roomId, userNickname);
        if (emitter != null) {
            sendToEmitter(emitter, data);
        }
    }

    private void sendToEmitter(SseEmitter emitter, String data) {
        try {
            emitter.send(SseEmitter.event().name("sse").data(data), MediaType.TEXT_PLAIN); // UTF-8 인코딩을 위해 MediaType.TEXT_PLAIN 사용
        } catch (IOException exception) {
            emitter.completeWithError(exception);
        }
    }

    /**
     * 특정 방에 들어와 있는 모든 유저에 대한 SSE Emitter들을 가져오기
     * @param roomId - 방 ID
     * @return 특정 방에 들어와 있는 모든 유저에 대한 SSE Emitter들의 맵
     */
    public Map<String, SseEmitter> getAllEmittersByRoom(String roomId) {
        return sseRepository.getAllEmittersByRoom(roomId);
    }

    /**
     * 특정 방에 특정 유저에 대한 SSE Emitter를 가져오기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @return 특정 방에 특정 유저에 대한 SSE Emitter
     */
    public SseEmitter getEmitterByRoomAndUser(String roomId, String userNickname) {
        return sseRepository.getByRoomAndUser(roomId, userNickname);
    }

}
