package io.openvidu.basic.java.game.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Component
public class GameInfo {

    private String gameRoomId; //게임방 아이디

    private List<Player> players; //게임에 참여한 플레이어 정보
    /**
     * state : 게임이 진행중인 방의 상태
     * 홀수: 낮, 짝수 : 밤
     * 1,2: 첫째 날 / 3,4: 둘째 날 / 5,6: 셋째 날 .....
     * */
    private int state ;
    private int time;
    private List<String> messages;
    private int livePlayerNumber=6;

    //TODO : 시간 설정 및 클라이언트와의 동기화를 위한 설정 고민해야함
}
