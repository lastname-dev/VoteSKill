package com.voteskill.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserOauthInfoDto {
    String social_id;
    String nickName;
    boolean isUser;
    String ownJwtAccessToken;
    String ownJwtRefreshToken;
}
