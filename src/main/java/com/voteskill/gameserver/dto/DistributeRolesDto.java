package com.voteskill.gameserver.dto;

import com.voteskill.gameserver.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class DistributeRolesDto {
    private String nickname; //유저 닉네임 중복 불가
    private Role role;  //부여된 역할
}
