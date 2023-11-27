package com.voteskill.gameserver.game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteRequestDto {
    private String roomId;
    private String voterNickname; //투표 한 사람
    private String voteeNickname; //투표 받은 사람
}
