package io.openvidu.basic.java.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoomEnterDto {
    String roomName;
    String password;
    String nickname;
}
