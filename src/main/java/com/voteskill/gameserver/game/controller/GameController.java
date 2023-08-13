package com.voteskill.gameserver.game.controller;

import static org.springframework.http.HttpStatus.OK;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.dto.GameStartDto;
import com.voteskill.gameserver.game.dto.VoteRequestDto;
import com.voteskill.gameserver.game.service.GameSchedulerService;
import com.voteskill.gameserver.game.service.GameService;
import com.voteskill.gameserver.game.service.RedisService;
import com.voteskill.gameserver.game.service.VoteService;
import com.voteskill.gameserver.sse.SseEmitters;
import java.util.ArrayList;
import java.util.List;
import jdk.jshell.Snippet.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
@CrossOrigin
public class GameController {

  private final GameService gameService;
  private final VoteService voteService;
  private final SseEmitters sseEmitters;
  private final GameSchedulerService gameSchedulerService;


  @PostMapping("/{roomid}/stop")
  public String gameStop(@PathVariable String roomid) {
    gameSchedulerService.stopSchedulingForRoom(roomid);
    return "Scheduling stopped.";
  }

  //방아이디를 받아온다. -> 방장이 게임 스타트를 눌렀을 때 실행되는 메서드
  @PostMapping("/{roomId}/start")
  public ResponseEntity<?> gameStart(@PathVariable String roomId) throws Exception {
    sseEmitters.role(roomId);
    gameSchedulerService.startSchedulingForRoom(roomId);

    return new ResponseEntity<>(OK);
  }

  @GetMapping("/{roomId}")
  public GameInfo test(@PathVariable String roomId){
    return gameService.getGame(roomId);
  }


}
