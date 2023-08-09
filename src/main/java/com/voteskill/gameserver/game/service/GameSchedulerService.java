package com.voteskill.gameserver.game.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class GameSchedulerService {
  private final TaskScheduler taskScheduler;
  private final RedisService redisService;
  private Map<String, ScheduledFuture<?>> scheduledFutures = new ConcurrentHashMap<>();



  public void startSchedulingForRoom(String roomName) {
    if (!scheduledFutures.containsKey(roomName)) {
      ScheduledFuture<?> scheduledFuture = taskScheduler.scheduleAtFixedRate(
          () -> {
              executeScheduledMethod(roomName);
          }, 1000); // 1초마다 실행
      scheduledFutures.put(roomName, scheduledFuture);
    }
  }

  public void stopSchedulingForRoom(String roomName) {
    ScheduledFuture<?> scheduledFuture = scheduledFutures.get(roomName);
    if (scheduledFuture != null) {
      scheduledFuture.cancel(false);
      scheduledFutures.remove(roomName);
    }
  }

  @Async
  public void executeScheduledMethod(String roomName) {
    // 각 방에 대한 주기적으로 실행될 메소드 내용을 작성
    if(roomName.equals("hello")) {
      while(true){

      }
    }
    System.out.println("Scheduled method is running for room: " + roomName +"        ");
  }
}