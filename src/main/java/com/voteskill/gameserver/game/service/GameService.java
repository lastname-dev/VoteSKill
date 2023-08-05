package com.voteskill.gameserver.game.service;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.domain.Role;
import com.voteskill.gameserver.game.dto.GameStartDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    /**
     * 게임 시작 시 기본 설정 정보 초기화하는 메서드
     * */
    public void gameSetting(String roomId, GameStartDto gameStartDto){
        GameInfo gameInfo = new GameInfo();
        gameInfo.setGameRoomId(roomId); //방 아이디
        gameInfo.setState(1); //1일차 낮
        gameInfo.setPlayers(distributionRoles(gameStartDto)); //
    }

    /**
     * 게임 시작 시 플레이어에게 역할을 부여하는 메소드
     * 필수 역할 분배 0: 마피아 1: 스파이 2: 의사 3: 경찰 (4명)
     * 랜덤 역할 분배 4: 기자 5: 군인  6:정치인 7:개발자 8: 갱스터 9:성직자 (택 2)
     * Player 의 필드 (nickName, Role, alive, useSkill 스킬사용여부)
     */
    public List<Player>  distributionRoles(GameStartDto gameStartDto) {

        List<Player> basicGameSetting = new ArrayList<>(); //서버가 가지고 있을 게임방 정보

        List<String> playerList = gameStartDto.getPlayers();  //넘어온 사용자 정보
        Collections.shuffle(playerList); //사용자 순서 재배치

        //필수 역할 배정
        basicGameSetting.add(new Player(playerList.get(0), Role.MAFIA, true, false));
        basicGameSetting.add(new Player(playerList.get(1), Role.SPY, true, false));
        basicGameSetting.add(new Player(playerList.get(2), Role.DOCTOR, true, false));
        basicGameSetting.add(new Player(playerList.get(3), Role.POLICE, true, false));

        //랜덤 역할 배정
        List<Role> randomRoleList = new ArrayList<>();
        Collections.addAll(randomRoleList, Role.REPORTER, Role.SOLDIER, Role.POLITICIAN,
            Role.DEVELOPER, Role.GANGSTER, Role.PRIEST);
        Collections.shuffle(randomRoleList); //역할 순서 재배치
        basicGameSetting.add(new Player(playerList.get(4), randomRoleList.get(0), true, false));
        basicGameSetting.add(new Player(playerList.get(5), randomRoleList.get(1), true, false));

        return basicGameSetting;
    }

    //연결된 플레이어 이름으로 생성된 에미터 정보를 가져오는 메서드
    public void setEmitterInfo(){

    }

}
