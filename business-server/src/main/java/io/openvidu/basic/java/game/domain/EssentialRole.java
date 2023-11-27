package io.openvidu.basic.java.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Getter
public enum EssentialRole {
    MAFIA("MAFIA"),  DOCTOR("DOCTOR"), POLICE("POLICE");

    private final String roleName;

    public static List<String> getEssentialRoles(){
        return Arrays.asList(
                MAFIA.getRoleName(),
                DOCTOR.getRoleName(),
                POLICE.getRoleName()
        );
    }
}
