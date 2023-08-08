package io.openvidu.basic.java.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum OtherRole {
    REPORTER("ROLE_REPORTER"), SOLDIER("ROLE_SOLDIER"), POLITICIAN("ROLE_POLITICIAN"),
    DEVELOPER("ROLE_DEVELOPER"), GANGSTER("ROLE_GANGSTER"), PRIEST("ROLE_PRIEST");

    private final String roleName;

    public static List<String> getOtherRoles(){
        return Arrays.asList(
                REPORTER.getRoleName(),
                SOLDIER.getRoleName(),
                POLITICIAN.getRoleName(),
                DEVELOPER.getRoleName(),
                GANGSTER.getRoleName(),
                PRIEST.getRoleName()
        );
    }
}
