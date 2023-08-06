package io.openvidu.basic.java.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Player {
    private String nickname;
    private String role;
    private String pick;
    private int voteCount;
    private Boolean alive; //생존 여부, true: 생존  false:  죽음
    private Boolean useSkill; // 스킬 사용여부, true: 사용, false: 미사용

    public void incrementVoteCount() {
        voteCount++;
    }
}
