package io.openvidu.basic.java.Dto;

import io.openvidu.basic.java.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoomEnterResponseDto {
    String token;
    Room room;
}
