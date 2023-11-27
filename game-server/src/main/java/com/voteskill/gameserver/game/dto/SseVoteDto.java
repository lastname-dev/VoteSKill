package com.voteskill.gameserver.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SseVoteDto {
    private boolean vote;
    int timer;
}
