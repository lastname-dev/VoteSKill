package com.voteskill.gameserver.game.service;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.dto.SseResponseDto;
import com.voteskill.gameserver.sse.SseEmitters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameSchedulerService {
  private final TaskScheduler taskScheduler;
  private final RedisTemplate redisTemplate;
  private final GameService gameService;
  private final SseEmitters sseEmitters;
  private final String gameKeyPrefix = "game:";
  private Map<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();
  private Map<String,Integer> time = new ConcurrentHashMap<>();


  public void startSchedulingForRoom(String roomName) {
    log.info("room : {}",roomName);
    if (!scheduledFutures.containsKey(roomName)) {
      ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
          () -> {
              executeVoteMethod(roomName);
          } ,500000); // 1초마다 실행
      scheduledFutures.put(roomName, scheduledFuture);
      scheduledFuture = taskScheduler.scheduleAtFixedRate(
          () -> {
            try {
              executeSkillMethod(roomName);
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }, 400000); // 1초마다 실행
      scheduledFutures.put(roomName,scheduledFuture);
    }
  }

// 낮 : 2분 ,5초, 투표시간 :30초 ,5초, 기술사용:30초


  public void stopSchedulingForRoom(String roomName) {
    ScheduledFuture<?> scheduledFuture = scheduledFutures.get(roomName);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(false);
      scheduledFutures.remove(roomName);
    }
  }

  @Async
  public void executeVoteMethod(String roomName) {
    log.info("voteRoom : {}",roomName);
    SseResponseDto dto = toVoteDto(roomName);
    sseEmitters.roomInfo(roomName,dto);
  }
  @Async
  public void executeSkillMethod(String roomName) throws Exception {
    log.info("skillRoom : {}",roomName);
    gameService.skill(roomName);
    SseResponseDto skillDto = toSkillDto(roomName);
    sseEmitters.roomInfo(roomName,skillDto);
  }
  public SseResponseDto toSkillDto(String roomName){
    GameInfo gameInfo = gameService.getGame(roomName);
    List<Player> players = gameInfo.getPlayers();
    List<String> death = new ArrayList<>();
    List<String> message= gameInfo.getMessages();
    for(Player player: players){
      if(!player.getAlive()){
        death.add(player.getNickname());
      }
      player.setPick("");
    }
    SseResponseDto sseResponseDto = new SseResponseDto(death,message);

    return sseResponseDto;
  }
  public SseResponseDto toVoteDto(String roomName){
    GameInfo gameInfo =gameService.getGame(roomName);
    List<Player> players = gameInfo.getPlayers();
    List<String> death = new ArrayList<>();
    List<String> message =new ArrayList<>();
    for(Player player : players){
      if(!player.getAlive()){
        death.add(player.getNickname());
      }
      if(player.getVoteCount()>(gameInfo.getLivePlayerNumber()/2)){
        if(player.getRole().equals("ROLE_POLITICIAN")){
          message.add(player.getNickname()+"님은 정치인입니다");
          break;
        }
        death.add(player.getNickname());
        player.setAlive(false);
        gameInfo.setLivePlayerNumber(gameInfo.getLivePlayerNumber()-1);
        message.add(player.getNickname()+"님이 사망하였습니다.");
      }
    }
    for(Player player:players){
      player.setVoteCount(0);
    }
    //todo: Redis에 gameinfo 넣기
    return new SseResponseDto(death,message);
  }
}