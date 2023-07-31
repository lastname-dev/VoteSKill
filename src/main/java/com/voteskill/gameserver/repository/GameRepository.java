package com.voteskill.gameserver.repository;

import com.voteskill.gameserver.domain.Player;
import java.util.Optional;

public interface GameRepository{

    Optional<Player> findByNickname(String nickname);

}
