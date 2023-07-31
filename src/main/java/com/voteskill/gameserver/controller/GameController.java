package com.voteskill.gameserver.controller;

import com.voteskill.gameserver.dto.DistributeRolesDto;
import com.voteskill.gameserver.dto.GameStartDto;
import com.voteskill.gameserver.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class GameController {

    private final GameService gameService;
    //TODO : 게임서버 연결 -> 소켓 열기

    //대기방 입장할 때 여는 요청 + 리다이렉트 형식
    @GetMapping()
    public void openWebSocket(){

    }

    //파람으로 세션아이디를 받아온다.
    @PostMapping("/{roomId}/start")
    public String gameStart(@RequestBody GameStartDto gameStartDto) throws Exception {

        // TODO : 플레이어 별 역할 분배 + 프론트에 응답으로 보내기
        gameService.distributionRoles(gameStartDto);

        // TODO : 웹 소켓으로 보내기
        gameService.notifyRoles();
        return "게임 스타트!";
    }


}
