package com.voteskill.gameserver.sse;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.dto.DistributeRolesDto;
import com.voteskill.gameserver.game.dto.GameInfoResponseDto;
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
        //TODO : 에러방지 위한 더미데이터 전송해줘야함.
        return sseService.createEmitter(roomId, userNickname);

    }

    /**
     * 같은 방에 있는 모든 클라이언트들에게 방 정보 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-to-all-in-room/{roomId}")
    public void sendToAllInRoom(@PathVariable String roomId, @RequestBody GameInfoResponseDto gameInfo) {
        sseService.notifyAllInRoom(roomId, gameInfo);
    }

    // TODO: 모든 클라이언트에게 죽은사람정보, 투표결과 보낼 때 사용할 메서드 구현

    /**
     * 한 클라이언트에게 부여된 역할 정보를 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-to-client/{roomId}/{userNickname}")
    public void sendToClient(@PathVariable String roomId, @PathVariable String userNickname, @RequestBody DistributeRolesDto rolesDto) {
        sseService.sendRolesInfoToUser(roomId, userNickname, rolesDto);
    }

}