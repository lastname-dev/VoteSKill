package com.voteskill.gameserver.game.dto;

import java.util.List;
import lombok.AllArgsConstructor;



@AllArgsConstructor
public class SseResponseDto {
  private List<String> death;
  private List<String> messages;
}

