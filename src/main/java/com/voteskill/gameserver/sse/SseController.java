package com.voteskill.gameserver.sse;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Slf4j
@RequestMapping("/sse")
@RestController
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    //연결요청 들어왔을 때, roomId 와 userNickname 입력받음
    @GetMapping(value = "/enter/{roomId}/{userNickname}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable String roomId, @PathVariable String userNickname) {
        return sseService.createEmitter(roomId, userNickname);
    }

    /**
     * 같은 방에 있는 모든 클라이언트들에게 같은 정보를 동시에 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-to-all-in-room/{roomId}")
    public void sendToAllInRoom(@PathVariable String roomId, @RequestBody String data) {
        sseService.notifyAllInRoom(roomId, data);
    }

    /**
     * 한 클라이언트에게 특정한 정보를 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-to-client/{roomId}/{userNickname}")
    public void sendToClient(@PathVariable String roomId, @PathVariable String userNickname, @RequestBody String data) {
        sseService.notifyClient(roomId, userNickname, data);
    }




}