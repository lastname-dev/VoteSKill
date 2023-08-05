package com.voteskill.gameserver.game.dto;

import com.voteskill.gameserver.game.domain.Player;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class BasicGameSetting {
    List<Player> players;
}
