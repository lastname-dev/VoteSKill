package com.voteskill.gameserver.game.service;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.domain.Role;
import com.voteskill.gameserver.game.domain.Room;
import com.voteskill.gameserver.game.dto.GameInfoResponseDto;
import com.voteskill.gameserver.game.dto.GameStartDto;
import com.voteskill.gameserver.game.dto.SkillDto;

import com.voteskill.gameserver.game.dto.SseResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {

    private final String gameKeyPrefix = "game:";

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;


    public GameInfoResponseDto skill(String roomName) throws Exception {
        GameInfo gameInfo = getGame(roomName);
        List<Player> players = gameInfo.getPlayers();
        List<String> messages = gameInfo.getMessages();
        String mafiaPick = "mafia";
        String docterPick = "docter";
        for (Player player : players) {

            String role = player.getRole();
            String target = player.getPick();
            log.info("{} : {}({})가 {}에게 능력을 사용합니다", roomName, player.getNickname(), role, target);
            if (target.equals("")) continue;
            switch (role) {
                case "MAFIA":
                    mafiaPick = player.getPick();
                    break;
                case "SPY":
                    spySkill(target, roomName);
                    break;
                case "DOCTOR":
                    docterPick = player.getPick();
                    break;
                case "REPORTER":
                    if (player.getUseSkill()) {
                        break;
                    }
                    player.setUseSkill(true);
                    messages.add(reporterSkill(target, roomName));
                    break;
                case "PRIEST":
                    if (player.getUseSkill()) {
                        break;
                    }
                    player.setUseSkill(true);
                    messages.add(priestSkill(target, roomName,gameInfo));
                    break;

                default:
                    // 처리할 수 없는 직업일 경우, 에러 처리 또는 기본 처리 로직
                    break;
            }
        }

        if (!docterPick.equals(mafiaPick) && !mafiaPick.equals("mafia"))  {
            messages.add(mafiaSkill(mafiaPick, roomName,gameInfo));
        } else if (docterPick.equals(mafiaPick)){
            messages.add(docterSkill(docterPick, roomName));
        }
        redisTemplate.opsForHash().put(gameKeyPrefix, roomName, gameInfo);
        return null;
    }
    public SseResponseDto vote(String roomName) {
        GameInfo gameInfo = getGame(roomName);
        List<Player> players = gameInfo.getPlayers();
        List<String> death = new ArrayList<>();
        List<String> message = new ArrayList<>();
        for (Player player : players) {
            log.info("투표 : {} 가 {} 표를 받았습니다.", player.getNickname(),player.getVoteCount());
            if (!player.getAlive()) {
                death.add(player.getNickname());
            }
            if (player.getVoteCount() > (gameInfo.getLivePlayerNumber() / 2)) {
                if (player.getRole().equals("POLITICIAN")) {
                    message.add(player.getNickname() + "님은 정치인입니다");
                    break;
                }
                death.add(player.getNickname());
                player.setAlive(false);
                gameInfo.setLivePlayerNumber(gameInfo.getLivePlayerNumber() - 1);
                message.add(player.getNickname() + "님이 사망하였습니다.");
            }
        }
        for (Player player : players) {
            player.setVoteCount(0);
        }
        redisTemplate.opsForHash().put(gameKeyPrefix, roomName, gameInfo);
        //todo: Redis에 gameinfo 넣기
        return new SseResponseDto(death, message, 15,"vote");
    }
    public SseResponseDto toSkillDto(String roomName) {
        GameInfo gameInfo = getGame(roomName);
        List<Player> players = gameInfo.getPlayers();
        List<String> death = new ArrayList<>();
        List<String> message = gameInfo.getMessages();
        for (Player player : players) {
            if (!player.getAlive()) {
                death.add(player.getNickname());
            }
            player.setPick("");
        }
        SseResponseDto sseResponseDto = new SseResponseDto(death, message, 120,"skill");
        redisTemplate.opsForHash().put(gameKeyPrefix, roomName, gameInfo);
        return sseResponseDto;
    }
    public void initMessage(String roomName){
        GameInfo game = getGame(roomName);
        game.setMessages(new ArrayList<>());
        List<Player> players = game.getPlayers();
        for(Player player : players){
            player.setUseVote(false);
        }

        redisTemplate.opsForHash().put(gameKeyPrefix,roomName,game);
    }
    public String checkGameOver(String roomName){
        GameInfo game =getGame(roomName);
        List<Player> players = game.getPlayers();
        int livePerson =0;
        for(Player player : players){
            if(player.getRole().equals("MAFIA") ){
                if(!player.getAlive())
                    return "시민 승리";
            }
            else{
                if(player.getAlive()){
                    livePerson++;
                }
            }
        }
        if(livePerson==0){
            return "마피아 승리";
        }
        return "";
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

    private String politicianSkill(String caster, String roomName) {
        return caster + "는 정치인 입니다.";
    }

    private String mafiaSkill(String target, String roomName,GameInfo gameInfo) {
        gameInfo.setLivePlayerNumber(gameInfo.getLivePlayerNumber() - 1);
        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            if (player.getNickname().equals(target)) {
                if (player.getRole().equals("SOLDIER") && !player.getUseSkill()) {
                    player.setUseSkill(true);
                    return target + " 이 군인이었습니다.";
                }
                log.info("mafia한테 지목당한 사람: {}",target);
                player.setAlive(false);
            }
        }
        return target + " 이 죽었습니다.";
    }

    // SPY 직업의 스킬 처리 로직
    private String docterSkill(String target, String roomName) {
        // 스킬 처리 로직 구현
        return "의사가 " + target + "을 살렸습니다.";
    }

    private void spySkill(String target, String roomName) {
        // 스킬 처리 로직 구현
        GameInfo gameInfo = getGame(roomName);
    }

    // DOCTOR 직업의 스킬 처리 로직

    private String reporterSkill(String target, String roomName) {
        // 스킬 처리 로직 구현
        GameInfo gameInfo = getGame(roomName);
        List<Player> players = gameInfo.getPlayers();
        String job = "";
        for (Player player : players) {
            if (player.getNickname().equals(target)) {
                job = player.getRole();
            }
        }
        return target + "의 직업은 " + job + " 입니다.";
    }

    private void gangsterSkill(String target, String roomName) {
        // 스킬 처리 로직 구현
        // ...
    }

    private String priestSkill(String target, String roomName,GameInfo gameInfo) {
        // 스킬 처리 로직 구현
        gameInfo.setLivePlayerNumber(gameInfo.getLivePlayerNumber() + 1);
        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            if (!player.getAlive() &&player.getNickname().equals(target)) {
                player.setAlive(true);
            }
        }
        return "성직자가 " + target + "을 살렸습니다.";
    }

    public GameInfo getGame(String roomName) {
        GameInfo game = (GameInfo) redisTemplate.opsForHash().get(gameKeyPrefix, roomName);
        log.info("game : {}", game);
        return game;
    }

    public List<GameInfo> getGames() {
        List<GameInfo> values = redisTemplate.opsForHash().values(gameKeyPrefix);
        return values;
    }

    public void putGame(GameInfo gameInfo) {
        redisTemplate.opsForHash().put(gameKeyPrefix, gameInfo.getGameRoomId(), gameInfo);
    }


}
