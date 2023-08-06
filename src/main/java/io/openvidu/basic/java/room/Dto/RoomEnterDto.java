package io.openvidu.basic.java.room.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class RoomEnterDto {
    String roomName;
    String password;
    String nickname;
}
