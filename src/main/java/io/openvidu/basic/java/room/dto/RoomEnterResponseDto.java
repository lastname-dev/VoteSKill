package io.openvidu.basic.java.room.dto;

import io.openvidu.basic.java.room.domain.Room;
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
