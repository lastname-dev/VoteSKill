package io.openvidu.basic.java.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GameInfo {
    String gameRoomId; //게임방 아이디

    List<Player> players; //게임에 참여한 플레이어 정보
    /**
     * state : 게임이 진행중인 방의 상태
     * 홀수: 낮, 짝수 : 밤
     * 1,2: 첫째 날 / 3,4: 둘째 날 / 5,6: 셋째 날 .....
     * */
    int state ;
    //TODO : 시간 설정 및 클라이언트와의 동기화를 위한 설정 고민해야함
}
