package io.openvidu.basic.java.game.service;

import io.openvidu.basic.java.room.domain.Room;
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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void vote(VoteDto voteDto) throws Exception {
        GameInfo gameInfo = getGame(voteDto.getRoomName());
        List<Player> players = gameInfo.getPlayers();
        Boolean politician = false;
        for(Player player: players){
            if(player.getNickname().equals(voteDto.getCaster()) && player.getUseVote()){
                throw new Exception();
            }
            else if(player.getNickname().equals(voteDto.getCaster())&&player.getRole().equals("POLITICIAN")){
                politician= true;
            }
            if(player.getNickname().equals(voteDto.getCaster())){
                player.setUseVote(true);
            }
        }
        for(Player player : players){
            if(player.getNickname().equals(voteDto.getTarget())) {
                player.incrementVoteCount();
                if(politician)
                    player.incrementVoteCount();
            }
            if(player.getRole().equals("POLICE")){
                player.setUseSkill(false);
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

        String role = checkRole(gameInfo, caster);

        if(role.equals("POLICE")){
            return policeSkill(caster, target, roomName,gameInfo);
        }

        List<Player> players = gameInfo.getPlayers();
        for(Player player : players){
            if(player.getNickname().equals(caster)) {
                if(!player.getAlive()){
                    return null;
                }
                player.setPick(target);
            }
        }
        redisTemplate.opsForHash().put(gameKeyPrefix,roomName,gameInfo);



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
        selectedRoles.add(otherRoles.get(2));

        // 역할 리스트에서 역할을 랜덤으로 선택하여 Player 객체를 생성하고 반환
        Random random = new Random();

        for (String nickname : people) {
            // 역할 랜덤 선택
            String randomRole = selectedRoles.remove(random.nextInt(selectedRoles.size()));

            // Player 객체 생성 및 리스트에 추가
            Player player = new Player(nickname, randomRole,"",0, true, false,false);

            players.add(player);
        }
        for(Player player: players){
            log.info("player {} 의 직업은 {} 입니다.",player.getNickname(),player.getRole());
        }

        return players;
    }

    private ResponseEntity<SkillResultDto> policeSkill(String caster, String target, String roomName,GameInfo gameInfo)
        throws Exception {
        // 스킬 처리 로직 구현

        if(!checkRole(gameInfo,caster).equals("POLICE")) {
            throw new Exception();
        }
        List<Player> players = gameInfo.getPlayers();
        for(Player player : players){
            if(player.getRole().equals("POLICE")&&player.getUseSkill()){
                return new ResponseEntity<>(new SkillResultDto(target,false,"능력은 한번만 사용 가능합니다."),HttpStatus.ACCEPTED);
            }
            if(player.getNickname().equals(target)){
                player.setUseSkill(true);
                if(player.getRole().equals("MAFIA"))
                    return new ResponseEntity<>(new SkillResultDto(target,true,target+"은 마피아입니다."),HttpStatus.ACCEPTED);
                return new ResponseEntity<>(new SkillResultDto(target,false,target+"은 마피아가 아닙니다."),HttpStatus.ACCEPTED);
            }
        }
        return new ResponseEntity<>(new SkillResultDto(target,false,target+"은 마피아가 아닙니다."),HttpStatus.BAD_REQUEST);
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

