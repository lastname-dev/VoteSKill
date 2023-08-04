package com.voteskill.gameserver.game.service;

import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.domain.Role;
import com.voteskill.gameserver.game.dto.GameStartDto;
import com.voteskill.gameserver.game.dto.GameStartResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

   // private final GameRepository gameRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 게임 시작 시 플레이어에게 역할을 부여하는 메소드
     * 필수 역할 분배 0: 마피아 1: 스파이 2: 의사 3: 경찰 (4명)
     * 랜덤 역할 분배 4: 기자 5: 군인  6:정치인 7:개발자 8: 갱스터 9:성직자 (택 2)
     */
    public GameStartResponseDto distributionRoles(GameStartDto gameStartDto) {

        GameStartResponseDto gameStartResponseDto = new GameStartResponseDto();
        List<String> playerList = gameStartDto.getPlayers(); //넘어온 사용자 정보
        Collections.shuffle(playerList); //사용자 순서 재배치

        //필수 역할 배정
        gameStartResponseDto.addPlayer(new Player(playerList.get(0), Role.MAFIA));
        gameStartResponseDto.addPlayer(new Player(playerList.get(1), Role.SPY));
        gameStartResponseDto.addPlayer(new Player(playerList.get(2), Role.DOCTOR));
        gameStartResponseDto.addPlayer(new Player(playerList.get(3), Role.POLICE));

        //랜덤 역할 배정
        List<Role> randomRoleList = new ArrayList<>();
        Collections.addAll(randomRoleList, Role.REPORTER, Role.SOLDIER, Role.POLITICIAN,
            Role.DEVELOPER, Role.GANGSTER, Role.PRIEST);
        Collections.shuffle(randomRoleList); //역할 순서 재배치
        gameStartResponseDto.addPlayer(new Player(playerList.get(4), randomRoleList.get(0)));
        gameStartResponseDto.addPlayer(new Player(playerList.get(5), randomRoleList.get(1)));

        return gameStartResponseDto;
    }

    /**
     * 부여된 역할 정보를 웹소켓으로 보내는 메서드
     */
    public void notifyRoles(GameStartResponseDto gameStartResponseDto) {
        messagingTemplate.convertAndSend("/topic/roles/" + gameStartResponseDto);
    }

    /*
    *
    * // JavaScript를 사용한 웹소켓 구독 예시 (프론트엔드 프레임워크나 기술 스택에 맞게 수정 가능)
const socket = new WebSocket('ws://your-server-hostname/ws');

socket.onopen = () => {
  console.log('WebSocket 연결이 열렸습니다.');
};

socket.onmessage = (event) => {
  const role = event.data; // 메시지에서 역할 정보를 가져옵니다.
  console.log('받은 역할:', role);
};

socket.onclose = () => {
  console.log('WebSocket 연결이 닫혔습니다.');
};

    * */

}
