package io.openvidu.basic.java.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum OtherRole {
    REPORTER("REPORTER"), SOLDIER("SOLDIER"), POLITICIAN("POLITICIAN"),
    PRIEST("PRIEST");

    private final String roleName;

    public static List<String> getOtherRoles(){
        return Arrays.asList(
                REPORTER.getRoleName(),
                SOLDIER.getRoleName(),
                POLITICIAN.getRoleName(),
                PRIEST.getRoleName()
        );
    }
}
