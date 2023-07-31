package io.openvidu.basic.java.Dto;

import io.openvidu.basic.java.Room;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoomEnterResponseDto {
    String token;
    Room room;
}
