package com.voteskill.gameserver.game.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GameStartDto {
    private List<String> players; //클라이언트에서 받아온 게임에 참여한 플레이어들의 정보: nickname
}
