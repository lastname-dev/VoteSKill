package io.openvidu.basic.java.room.Dto;

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
