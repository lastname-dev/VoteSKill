package com.voteskill.gameserver.sse;

import com.voteskill.gameserver.game.dto.DistributeRolesDto;
import com.voteskill.gameserver.game.dto.GameInfoResponseDto;
import com.voteskill.gameserver.game.dto.VoteResultResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    //TODO : 회의 후 수정
    //postmapping  이 아니라
    //@Scheduled(fixedRate = 3000) : 일정시간이 지나거나 (ex. 3초마다 데이터가 업데이트)
    // 데이터 변동이 있을 때 사용할 것임
    /**
     * 같은 방에 있는 모든 클라이언트들에게 방 정보 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-to-all-in-room/{roomId}")
    public void sendGameInfoToAllInRoom(@PathVariable String roomId, @RequestBody GameInfoResponseDto gameInfo) {
        sseService.notifyAllInRoom(roomId, gameInfo);
    }

    /**
     * 같은 방에 있는 모든 클라이언트들에게 투표결과 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-result-to-all-in-room/{roomId}")
    public void sendVoteResultToAllInRoom(@PathVariable String roomId, @RequestBody VoteResultResponseDto voteResultResponseDto) {
        sseService.sendVoteResultToAllInRoom(roomId, voteResultResponseDto);
    }

    // TODO: 모든 클라이언트에게 죽은사람정보

    /**
     * 한 클라이언트에게 부여된 역할 정보를 보낼 때 사용하는 메서드
     * */
    @PostMapping("/send-to-client/{roomId}/{userNickname}")
    public void sendToClient(@PathVariable String roomId, @PathVariable String userNickname, @RequestBody DistributeRolesDto rolesDto) {
        sseService.sendRolesInfoToUser(roomId, userNickname, rolesDto);
    }

}