package com.voteskill.gameserver.game.dto;

import com.voteskill.gameserver.game.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 각 사용자에게 부여된 역할을 알려주기 위해 사용할 DTO
 * */
@NoArgsConstructor
@Getter
public class DistributeRolesDto {
    private String nickname; //유저 닉네임 중복 불가
    private Role role;  //부여된 역할
}
