package com.voteskill.gameserver.game.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class SseResponseDto {
  private List<String> death;
  private List<String> messages;
  private int timer;
  private String type;
}

