package com.voteskill.gameserver.game.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * 서버에서 관리하기 위해 가지고 있을 정보
 * */
public class GameInfo {

    String gameRoomId; //게임방 아이디

    List<Player> players; //게임에 참여한 플레이어 정보
    /**
     * state : 게임이 진행중인 방의 상태
     * 홀수: 낮, 짝수 : 밤
     * 1,2: 첫째 날 / 3,4: 둘째 날 / 5,6: 셋째 날 .....
     * */
    int state ;

    int time = 120; //2분
    private List<String>[] messages;
    int livePlayerNumber;
}
