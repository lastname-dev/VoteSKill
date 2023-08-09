package com.voteskill.gameserver.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class GameSchedulerService {

  private final TaskScheduler taskScheduler;
  private ScheduledFuture<?> scheduledFuture;

//  public GameSchedulerService(TaskScheduler taskScheduler) {
//    this.taskScheduler = taskScheduler;
//  }

  public void startScheduling() {
    if (scheduledFuture == null || scheduledFuture.isCancelled()) {
      scheduledFuture = taskScheduler.scheduleAtFixedRate(this::executeScheduledMethod, 10000); // 10초마다 실행
    }
  }

  public void stopScheduling() {
    if (scheduledFuture != null) {
      scheduledFuture.cancel(false);
    }
  }

  private void executeScheduledMethod() {
    // 여기에 주기적으로 실행될 메소드 내용을 작성
    System.out.println("Scheduled method is running...");
  }
}