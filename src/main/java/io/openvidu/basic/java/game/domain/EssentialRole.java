package io.openvidu.basic.java.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Getter
public enum EssentialRole {
    MAFIA("ROLE_MAFIA"), SPY("ROLE_SPY"), DOCTOR("ROLE_DOCTOR"), POLICE("ROLE_POLICE");

    private final String roleName;

    public static List<String> getEssentialRoles(){
        return Arrays.asList(
                MAFIA.getRoleName(),
                SPY.getRoleName(),
                DOCTOR.getRoleName(),
                POLICE.getRoleName()
        );
    }
}
