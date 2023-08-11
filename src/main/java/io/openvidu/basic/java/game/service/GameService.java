package io.openvidu.basic.java.game.service;

import io.openvidu.basic.java.Room;
import io.openvidu.basic.java.game.domain.EssentialRole;
import io.openvidu.basic.java.game.domain.GameInfo;
import io.openvidu.basic.java.game.domain.OtherRole;
import io.openvidu.basic.java.game.domain.Player;
import io.openvidu.basic.java.game.dto.SkillDto;
import io.openvidu.basic.java.game.dto.SkillResultDto;
import io.openvidu.basic.java.game.dto.VoteDto;
import io.openvidu.basic.java.room.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class GameService {

    @Autowired
    private RoomService roomService;

    private final String gameKeyPrefix = "game:";


    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
//    private HashOperations<String, String, GameInfo> hashOperations;

//    @Autowired
//    public GameService(RedisTemplate<String,GameInfo> redisTemplate, RoomService roomService) {
//        this.redisTemplate = redisTemplate;
//        this.roomService = roomService;
//        this.hashOperations = redisTemplate.opsForHash();
//    }

    public void setting(String roomName) {
        Room room = roomService.getRoom(roomName);
        List<String> people = room.getPeople();
        List<Player> players = setRole(people);
        GameInfo gameInfo = new GameInfo(roomName, players, 1,0,new ArrayList<>(),6);
        redisTemplate.opsForHash().put(gameKeyPrefix, roomName, gameInfo);
    }
    public void vote(VoteDto voteDto){
        GameInfo gameInfo = getGame(voteDto.getRoomName());
        List<Player> players = gameInfo.getPlayers();
        Boolean politician = false;
        for(Player player: players){
            if(player.getNickname().equals(voteDto.getCaster())&&player.getRole().equals("ROLE_POLITICIAN")){
                politician= true;
            }
        }
        for(Player player : players){
            if(player.getNickname().equals(voteDto.getTarget())) {
                player.incrementVoteCount();
                if(politician)
                    player.incrementVoteCount();
            }
        }
        redisTemplate.opsForHash().put(gameKeyPrefix, voteDto.getRoomName(), gameInfo);
    }
    public GameInfo getGame(String roomName){
        GameInfo game = (GameInfo) redisTemplate.opsForHash().get(gameKeyPrefix, roomName);
        return game;
    }
    public void test(GameInfo gameInfo){
        redisTemplate.opsForHash().put(gameKeyPrefix,gameInfo.getGameRoomId(),gameInfo);
    }
    public ResponseEntity<SkillResultDto> skill(SkillDto skillDto) throws Exception {
        String caster = skillDto.getCaster();
        String target = skillDto.getTarget();
        String roomName = skillDto.getRoomId();

        GameInfo gameInfo = getGame(roomName);
        List<Player> players = gameInfo.getPlayers();
        for(Player player : players){
            if(player.getNickname().equals(caster))
                player.setPick(target);
        }
        redisTemplate.opsForHash().put(gameKeyPrefix,roomName,gameInfo);

        String role = checkRole(gameInfo, caster);

        if(role.equals("ROLE_POLICE")){
        // POLICE 직업의 스킬 처리 로직
        return policeSkill(caster, target, roomName);}

        return null;
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

    private ResponseEntity<SkillResultDto> policeSkill(String caster, String target, String roomName)
        throws Exception {
        // 스킬 처리 로직 구현
        GameInfo gameInfo = getGame(roomName);


        if(!checkRole(gameInfo,caster).equals("ROLE_POLICE")) {
            throw new Exception();
        }
        List<Player> players = gameInfo.getPlayers();
        for(Player player : players){
            if(player.getNickname().equals(target)){
                if(player.getRole().equals("ROLE_MAFIA"))
                    return new ResponseEntity<>(new SkillResultDto(target,true),HttpStatus.ACCEPTED);
                return new ResponseEntity<>(new SkillResultDto(target,false),HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(new SkillResultDto(target,false),HttpStatus.BAD_REQUEST);
    }

    private void reporterSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        GameInfo gameInfo = getGame(roomName);
    }


    private void gangsterSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private void priestSkill(String caster, String target, String roomName) {
        // 스킬 처리 로직 구현
        GameInfo gameInfo = getGame(roomName);
    }

}

