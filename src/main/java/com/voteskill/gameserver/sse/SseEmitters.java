package com.voteskill.gameserver.sse;

import com.voteskill.gameserver.game.domain.Role;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Component
@Slf4j
public class SseEmitters {

    /**
     * 아래 주석의 콜백은 SseEmitter를 관리하는 다른 스레드에서 실행
     * 따라서 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용
     * ConcurrnetModificationException 방지를 위함
     * */
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

//    private static final AtomicLong counter = new AtomicLong();

    /**
     * SseEmitter 생성할 때는 비동기 요청이 완료되거나 타임아웃 발생 시 실행할 콜백을 등록 가능.
     * 타임아웃 발생 시 브라우저에서 재연결 요청을 보내는데,
     * 이때 새로운 Emitter 객체를 생성하기 때문에 => SseController의 connect()
     * 기존의 emitter 제거해야함. => onCompletion 콜백에서 this.emitters.remove(emitter); 자기자신 삭제
     * */
    SseEmitter add(SseEmitter emitter) {
        this.emitters.add(emitter);
        log.info("new emitter added: {}", emitter);
        log.info("emitter list size: {}", emitters.size());
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emitters.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });
        return emitter;
    }

    /**
     * kill 이벤트 발생
     * 특정 플레이어가 죽음
     * */
    public void kill() {
        String deadPlayerNickname = "죽은 플레이어의 닉네임";
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("count")
                    .data(deadPlayerNickname));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * reveal 이벤트 발생
     * 특정 플레이어의 직업을 밝힘
     * */
    public void reveal() {
        String playerNickname = "특정 플레이어의 닉네임";
        Role role = Role.DOCTOR; // 의사였다고 가정
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("count")
                    .data(playerNickname + role));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}