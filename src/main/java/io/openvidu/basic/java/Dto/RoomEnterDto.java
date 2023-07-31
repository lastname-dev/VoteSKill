package io.openvidu.basic.java.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class RoomEnterDto {
    String roomName;
    String password;
}
