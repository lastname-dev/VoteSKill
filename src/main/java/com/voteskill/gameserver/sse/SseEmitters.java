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

//    /**
//     * 콜백은 SseEmitter를 관리하는 다른 스레드에서 실행
//     * 따라서 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용
//     * ConcurrnetModificationException 방지를 위함
//     * */
//    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
//
//
//    /**
//     * kill 이벤트 발생
//     * 특정 플레이어가 죽음
//     * */
//    public void kill() {
//        String deadPlayerNickname = "죽은 플레이어의 닉네임";
//        emitters.forEach(emitter -> {
//            try {
//                emitter.send(SseEmitter.event()
//                    .name("count")
//                    .data(deadPlayerNickname));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    /**
//     * reveal 이벤트 발생
//     * 특정 플레이어의 직업을 밝힘
//     * */
//    public void reveal() {
//        String playerNickname = "특정 플레이어의 닉네임";
//        Role role = Role.DOCTOR; // 의사였다고 가정
//        emitters.forEach(emitter -> {
//            try {
//                emitter.send(SseEmitter.event()
//                    .name("count")
//                    .data(playerNickname + role));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }


}