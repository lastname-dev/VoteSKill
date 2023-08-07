package com.voteskill.gameserver.game.controller;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.dto.GameStartDto;
import com.voteskill.gameserver.game.dto.VoteRequestDto;
import com.voteskill.gameserver.game.service.GameService;
import com.voteskill.gameserver.game.service.RedisService;
import com.voteskill.gameserver.game.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class GameController {

    private final GameService gameService;
    private final VoteService voteService;
    private final RedisService redisService;

    //방아이디를 받아온다. -> 방장이 게임 스타트를 눌렀을 때 실행되는 메서드
    @PostMapping("/{roomId}/start")
    public String gameStart(@PathVariable String roomId, @RequestBody GameStartDto gameStartDto) throws Exception {
        gameService.gameSetting(roomId, gameStartDto); // 게임방(정보) 클래스 초기화 : 생성 + 플레이어 별 역할 분배
        return "게임 스타트!";
    }

    //TODO : redis 와 연동해서 업데이트 된 정보 가져오기 구현

    //투표받는 로직
    @PostMapping("/{roomId}/vote")
    public ResponseEntity<String> recordVote(@RequestBody VoteRequestDto voteRequest) {
        String roomId = voteRequest.getRoomId();
        String voterNickname = voteRequest.getVoterNickname(); //투표한 사람
        String voteeNickname = voteRequest.getVoteeNickname(); //투표받은 사람

        // roomId 과 투표한 사람, 투표받은 사람이 유효한지 확인
        boolean isValid = voteService.isValidVote(roomId, voterNickname, voteeNickname);

        if (isValid) {
            // 투표 기록
            voteService.recordVote(roomId, voteeNickname);
            return ResponseEntity.ok("Vote recorded for player: " + voteeNickname);
        } else {
            return ResponseEntity.badRequest().body("Invalid vote request.");
        }
    }


//    @GetMapping("/get-value/{key}")
//    public String getValueFromRedis(@PathVariable String key) {
//        log.info(redisService.getValue(key));
//        return redisService.getValue(key);
//    }


}
