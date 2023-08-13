package com.voteskill.gameserver.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SseRoleDto {
    String role;
    int timer;
}
