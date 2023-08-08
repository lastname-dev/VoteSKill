package io.openvidu.basic.java.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SkillResultDto {
  private String target; // nickname
  private boolean isMafia; // true false;
}
