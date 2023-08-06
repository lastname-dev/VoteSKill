package io.openvidu.basic.java.game.service;

import io.openvidu.basic.java.Room;
import io.openvidu.basic.java.game.domain.EssentialRole;
import io.openvidu.basic.java.game.domain.GameInfo;
import io.openvidu.basic.java.game.domain.OtherRole;
import io.openvidu.basic.java.game.domain.Player;
import io.openvidu.basic.java.game.dto.SkillDto;
import io.openvidu.basic.java.game.dto.VoteDto;
import io.openvidu.basic.java.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class GameService {

    private RoomService roomService;

    private final String gameKeyPrefix = "game:";
    private RedisTemplate redisTemplate;
    private HashOperations<String, String, GameInfo> hashOperations;

    @Autowired
    public GameService(RedisTemplate redisTemplate, RoomService roomService) {
        this.redisTemplate = redisTemplate;
        this.roomService = roomService;
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void setting(String roomName) {
        Room room = roomService.getRoom(roomName);
        List<String> people = room.getPeople();
        List<Player> players = setRole(people);
        GameInfo gameInfo = new GameInfo(roomName, players, 1);
        hashOperations.put(gameKeyPrefix, roomName, gameInfo);
    }
    public void vote(VoteDto voteDto){
        GameInfo gameInfo = getGame(voteDto.getRoomName());
        List<Player> players = gameInfo.getPlayers();
        for(Player player : players){
            if(player.getNickname().equals(voteDto.getTarget()))
                player.incrementVoteCount();
        }
        hashOperations.put(gameKeyPrefix, voteDto.getRoomName(), gameInfo);
    }
    public GameInfo getGame(String roomName){
        GameInfo game = hashOperations.get(gameKeyPrefix, roomName);
        return game;
    }
    public void skill(SkillDto skillDto) {
        String caster = skillDto.getCaster();
        String target = skillDto.getTarget();
        String roomName = skillDto.getRoomId();

        GameInfo gameInfo = hashOperations.get(gameKeyPrefix, roomName);
        List<Player> players = gameInfo.getPlayers();
        String role = checkRole(gameInfo, caster);

        switch (role) {
            case "ROLE_MAFIA":
                // MAFIA 직업의 스킬 처리 로직
                mafiaSkill(caster, target, roomName);
                break;
            case "ROLE_SPY":
                // SPY 직업의 스킬 처리 로직
                spySkill(caster, target, roomName);
                break;
            case "ROLE_DOCTOR":
                // DOCTOR 직업의 스킬 처리 로직
                doctorSkill(caster, target, roomName);
                break;
            case "ROLE_POLICE":
                // POLICE 직업의 스킬 처리 로직
                policeSkill(caster, target, roomName);
                break;
            case "ROLE_REPORTER":
                // DOCTOR 직업의 스킬 처리 로직
                reporterSkill(caster, target, roomName);
                break;
            case "ROLE_SOLDIER":
                // DOCTOR 직업의 스킬 처리 로직
                soldierSkill(caster, target, roomName);
                break;
            case "ROLE_POLITICIAN":
                // DOCTOR 직업의 스킬 처리 로직
                politicianSkill(caster, target, roomName);
                break;
            case "ROLE_DEVELOPER":
                // DOCTOR 직업의 스킬 처리 로직
                developerSkill(caster, target, roomName);
                break;
            case "ROLE_GANGSTER":
                gangsterSkill(caster, target, roomName);
                break;
            case "ROLE_PRIEST":
                priestSkill(caster, target, roomName);
                break;

            default:
                // 처리할 수 없는 직업일 경우, 에러 처리 또는 기본 처리 로직
                break;
        }

    }

    public String checkRole(GameInfo gameInfo, String nickname) {

        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            if (player.getNickname().equals(nickname)) {
                return player.getRole();
            }
        }
        return null;
    }

    public List<Player> setRole(List<String> people) {
        List<Player> players = new ArrayList<>();
        List<String> essentialRoles = EssentialRole.getEssentialRoles();
        List<String> otherRoles = OtherRole.getOtherRoles();

        Collections.shuffle(otherRoles);
        List<String> selectedRoles = new ArrayList<>(essentialRoles);
        selectedRoles.add(otherRoles.get(0));
        selectedRoles.add(otherRoles.get(1));

        // 역할 리스트에서 역할을 랜덤으로 선택하여 Player 객체를 생성하고 반환
        Random random = new Random();

        for (String nickname : people) {
            // 역할 랜덤 선택
            String randomRole = selectedRoles.remove(random.nextInt(selectedRoles.size()));

            // Player 객체 생성 및 리스트에 추가
            Player player = new Player(nickname, randomRole,"",0, true, false);

            players.add(player);
        }

        return players;
    }

    // MAFIA 직업의 스킬 처리 로직
    private void mafiaSkill(String caster, String target, String roomName) {

    }

    // SPY 직업의 스킬 처리 로직
    private void spySkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    // DOCTOR 직업의 스킬 처리 로직
    private void doctorSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void policeSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void reporterSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void soldierSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void politicianSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void developerSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void gangsterSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void priestSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

}

