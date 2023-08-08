package com.voteskill.gameserver.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SkillDto {
  String roomId;
  String caster;
  String target;
}