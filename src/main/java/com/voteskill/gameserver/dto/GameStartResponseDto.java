package com.voteskill.gameserver.dto;

import com.voteskill.gameserver.domain.Player;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class GameStartResponseDto {
    List<Player> players;

    // 유저정보를 추가하는 메서드
    public void addPlayer(Player player) {
        players.add(player);
    }

}
