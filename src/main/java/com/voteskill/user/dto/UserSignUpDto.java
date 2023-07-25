package com.voteskill.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {
    private String socialId;
    private String email;
    private String password;
    private String nickname;
    private int age;
}