package com.voteskill.gameserver.game.service;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.domain.Role;
import com.voteskill.gameserver.game.domain.Room;
import com.voteskill.gameserver.game.dto.GameInfoResponseDto;
import com.voteskill.gameserver.game.dto.GameStartDto;
import com.voteskill.gameserver.game.dto.SkillDto;

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


    public void test(GameInfo gameInfo) {

        redisTemplate.opsForHash().put(gameKeyPrefix, gameInfo.getGameRoomId(), gameInfo);

    }


    public void gameStart(String roomId) {

    }

    public void setState(String roomId) {

    }

    public void reset(String roomId) {
        GameInfo gameInfo = getGame(roomId);
        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            player.setPick("");
            player.setVoteCount(0);
        }
    }

    public void vote(String roomName) {
        GameInfo gameInfo = getGame(roomName);
        List<String> messages = gameInfo.getMessages();
        int playerNumber = gameInfo.getLivePlayerNumber();
        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            if (player.getVoteCount() > (playerNumber + 1) / 2) {
                if (player.getRole().equals("ROLE_POLITICIAN")) {
                    messages.add(politicianSkill(player.getNickname(), roomName));
                }
            }
        }
    }

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
                case "ROLE_MAFIA":
                    mafiaPick = player.getPick();
                    break;
                case "ROLE_SPY":
                    spySkill(target, roomName);
                    break;
                case "ROLE_DOCTOR":
                    docterPick = player.getPick();
                    break;
                case "ROLE_REPORTER":
                    if (player.getUseSkill()) {
                        break;
                    }
                    player.setUseSkill(true);
                    messages.add(reporterSkill(target, roomName));
                    break;
                case "ROLE_PRIEST":
                    if (player.getUseSkill()) {
                        break;
                    }
                    player.setUseSkill(true);
                    messages.add(priestSkill(target, roomName));
                    break;

                default:
                    // 처리할 수 없는 직업일 경우, 에러 처리 또는 기본 처리 로직
                    break;
            }
        }

        if (!docterPick.equals(mafiaPick)) {
            messages.add(mafiaSkill(mafiaPick, roomName));
        } else {
            messages.add(docterSkill(docterPick, roomName));
        }
        redisTemplate.opsForHash().put(gameKeyPrefix, roomName, gameInfo);
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

    private String politicianSkill(String caster, String roomName) {
        return caster + "는 정치인 입니다.";
    }

    private String mafiaSkill(String target, String roomName) {
        GameInfo gameInfo = getGame(roomName);
        gameInfo.setLivePlayerNumber(gameInfo.getLivePlayerNumber() - 1);
        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            if (player.getNickname().equals(target)) {
                if (player.getRole().equals("ROLE_SOLDIER") && !player.getUseSkill()) {
                    player.setUseSkill(true);
                    return target + " 이 군인이었습니다.";
                }
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

    private String priestSkill(String target, String roomName) {
        // 스킬 처리 로직 구현
        GameInfo gameInfo = getGame(roomName);
        gameInfo.setLivePlayerNumber(gameInfo.getLivePlayerNumber() + 1);
        List<Player> players = gameInfo.getPlayers();
        for (Player player : players) {
            if (player.getNickname().equals(target)) {
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
