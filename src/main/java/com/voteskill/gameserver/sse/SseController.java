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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Slf4j
@RequestMapping("/sse")
@RestController
@RequiredArgsConstructor
public class SseController {

//    private final SseEmitters sseEmitters;

    private final SseService sseService;

//    public SseController(SseEmitters sseEmitters) {
//        this.sseEmitters = sseEmitters;
//    }

//    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public ResponseEntity<SseEmitter> connect() {
//        /**
//         * 생성자를 통해 만료시간을 설정가능하며, 디폴트 값은 서버에 다름.
//         * 스프링 부트의 내장 톰캣을 사용하면 30초로 설정.
//         * 만료시간이 되면 브라우저에서 자동으로 서버에 재연결 요청을 보냄.
//         * */
//        SseEmitter emitter = new SseEmitter(60 * 1000L); //브라우저에서 새로운 요청이 emitter 객체 다시 생성함
//        sseEmitters.add(emitter);
//        try {
//            emitter.send(SseEmitter.event()
//                .name("connect") // 초기 연결을 위한 요청 이벤트명 : connect
//                .data("connected!")); //더미데이터 넣어주기 -> 에러방지
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok(emitter);
//    }


    //연결요청 들어왔을 때, roomId 와 userNickname 입력받음
    @GetMapping(value = "/enter/{roomId}/{userNickname}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable String roomId, @PathVariable String userNickname) {
        return sseService.connect(roomId, userNickname);
    }

    @PostMapping("/send-data/{userNickname}")
    public void sendData(@PathVariable String userNickname) {
        sseService.notify( "roomID",userNickname, "connect test!!");
    }


//    @PostMapping("/count")
//    public ResponseEntity<Void> count() {
//        sseEmitters.count();
//        return ResponseEntity.ok().build();
//    }
//
//
//    @GetMapping("/kill")
//    public ResponseEntity<Void> kill(){
//        sseEmitters.kill();
//        return ResponseEntity.ok().build();
//    }

}