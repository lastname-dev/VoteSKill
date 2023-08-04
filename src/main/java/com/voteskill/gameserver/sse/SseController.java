package com.voteskill.gameserver.sse;

import com.voteskill.gameserver.sse.SseEmitters;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
public class SseController {

    private final SseEmitters sseEmitters;

    public SseController(SseEmitters sseEmitters) {
        this.sseEmitters = sseEmitters;
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect() {
        SseEmitter emitter = new SseEmitter(); //브라우저에서 새로운 요청이 emitter 객체 다시 생함
        sseEmitters.add(emitter);
        try {
            emitter.send(SseEmitter.event()
                .name("connect") // 해당이벤트 이름
                .data("connected!")); //더미데이터 넣어주기 -> 에러방지
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }

    @PostMapping("/count")
    public ResponseEntity<Void> count() {
        sseEmitters.count();
        return ResponseEntity.ok().build();
    }
}