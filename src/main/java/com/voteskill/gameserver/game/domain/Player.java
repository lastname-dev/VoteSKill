package com.voteskill.gameserver.game.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Setter
public class Player {
    private String nickname;
    private Role role;
    private Boolean alive; //생존 여부, true: 생존  false:  죽음
    private Boolean useSkill; // 스킬 사용여부, true: 사용, false: 미사용
}
