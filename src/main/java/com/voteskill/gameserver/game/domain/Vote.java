package com.voteskill.gameserver.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Vote {
    private String playerNickname;
    private int voteCount;

    public void increaseVoteCount() {
        this.voteCount++;
    }
}
