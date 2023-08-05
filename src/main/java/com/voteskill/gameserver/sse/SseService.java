package com.voteskill.gameserver.sse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final SseRepository sseRepository;

    /**
     * 클라이언트(플레이어)가 방 입장을 위해 호출하는 메서드.
     * @param userNickname - 플레이어의 닉네임, roomId - 입장한 방 아이디
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    public SseEmitter connect(String roomId, String userNickname) {
        SseEmitter emitter = createEmitter(roomId, userNickname ) ;
        sendToClient(roomId, userNickname, "EventStream Created. [" + userNickname + " entered " + roomId + " .]");
        return emitter;
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     * @param userNickname - 메세지를 전송할 사용자의 아이디.
     * @param event  - 전송할 이벤트 객체.
     */
    public void notify(String roomId, String userNickname, Object event) {//event type 을 String 으로 변
        sendToClient(roomId, userNickname, event);
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param userNickname   - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     */
    private void sendToClient(String roomId, String userNickname, Object data) { //data type 을 String 으로 변
        SseEmitter emitter = sseRepository.get(userNickname);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(String.valueOf(userNickname)).name("sse").data(data) ); // UTF-8 설정);
            } catch (IOException exception) {
                sseRepository.deleteById(userNickname);
                emitter.completeWithError(exception);
            }
        }
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     * @param userNickname - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private SseEmitter createEmitter(String roomId, String userNickname) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT ); // UTF-8 설정
        sseRepository.save(roomId, userNickname, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> sseRepository.deleteById(userNickname));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> sseRepository.deleteById(userNickname));

        return emitter;
    }



}
