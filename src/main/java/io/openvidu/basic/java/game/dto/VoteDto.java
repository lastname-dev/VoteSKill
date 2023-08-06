package io.openvidu.basic.java.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteDto {
    private String roomName;
    private String caster;
    private String target;
}
