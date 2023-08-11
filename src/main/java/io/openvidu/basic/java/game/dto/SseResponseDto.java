package io.openvidu.basic.java.game.dto;

import java.util.List;

public class SseResponseDto {
  private int time;
  private int gameState; //1 : 첫번째낮, 2:첫번째밤 , 3:첫번째낮 ...
  private List<String> death;
  private List<String> messages;

}
