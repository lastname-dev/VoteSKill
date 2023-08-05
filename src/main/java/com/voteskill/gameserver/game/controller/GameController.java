package com.voteskill.gameserver.game.controller;

import com.voteskill.gameserver.game.dto.GameStartDto;
import com.voteskill.gameserver.game.dto.BasicGameSetting;
import com.voteskill.gameserver.game.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class GameController {

    private final GameService gameService;

    //방아이디를 받아온다. -> 방장이 게임 스타트를 눌렀을 때 실행되는 메서드
    @PostMapping("/{roomId}/start")
    public String gameStart(@PathVariable String roomId, @RequestBody GameStartDto gameStartDto) throws Exception {

        gameService.gameSetting(roomId, gameStartDto); // 게임방(정보) 클래스 초기화 : 생성 + 플레이어 별 역할 분배

        // TODO : 게임정보 전달
        //gameService.notifyRoles(basicGameSetting);
        return "게임 스타트!";
    }


}
