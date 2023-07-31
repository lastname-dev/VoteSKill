package com.voteskill.gameserver.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    //필수로 지정될 역할
    MAFIA("ROLE_MAFIA"), SPY("ROLE_SPY"), DOCTOR("ROLE_DOCTOR"), POLICE("ROLE_POLICE"),

    //랜덤으로 선택되어 지정될 역할
    REPORTER("ROLE_REPORTER"), SOLDIER("ROLE_SOLDIER"), POLITICIAN("ROLE_POLITICIAN"),
    DEVELOPER("ROLE_DEVELOPER"), GANGSTER("ROLE_GANGSTER"), PRIEST("ROLE_PRIEST");

    private final String key;
}
