package com.voteskill.gameserver.sse;

import com.voteskill.gameserver.game.service.GameService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@Slf4j
@RequestMapping("/sse")
@RestController
@RequiredArgsConstructor
public class SseController {

    private final GameService gameService;

    private final SseEmitters sseEmitters;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;


    @GetMapping(value = "/enter/{roomId}/{userNickname}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connectRoom(@PathVariable String roomId, @PathVariable String userNickname) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT );
        sseEmitters.add(roomId,userNickname,emitter);
        try {
            emitter.send(SseEmitter.event()
                .name("connect")
                .data("connected!!!!!!!"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(emitter);
    }
}