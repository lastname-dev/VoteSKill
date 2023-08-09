package com.voteskill.gameserver.sse;

import com.voteskill.gameserver.game.domain.GameInfo;
import com.voteskill.gameserver.game.domain.Player;
import com.voteskill.gameserver.game.domain.Role;
import com.voteskill.gameserver.game.dto.DistributeRolesDto;
import com.voteskill.gameserver.game.service.GameService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Component
@Slf4j
@Getter
public class SseEmitters {

    private Map<String, Map<String, SseEmitter>> emittersByRoomId = new HashMap<>();
    private final GameService gameService;

    public SseEmitters(GameService gameService) {
        this.gameService = gameService;
    }

    //방이름, 유저이름, 이미터 형식으로 저장
    SseEmitter add(String roomId, String userNickname, SseEmitter emitter) {
        this.emittersByRoomId.computeIfAbsent(roomId, key -> new HashMap<>()).put(userNickname, emitter);
        log.info("new emitter added: {}", emitter);
        log.info("roomId added: {}", roomId);
        log.info("userNickname added: {}", userNickname);
        emitter.onCompletion(() -> {
            log.info("onCompletion callback");
            this.emittersByRoomId.remove(emitter);    // 만료되면 리스트에서 삭제
        });
        emitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitter.complete();
        });
        return emitter;
    }

    //특정 클라이언트에게 역할 보내는 메서드
//    @Scheduled(cron = "0/2 * * * * ?")
    public void role(String roodId, String userNickname) {
 //   public void role() {
//        long count = counter.incrementAndGet();
//        Map<String, SseEmitter> playersInTheRoom = emittersByRoomId.get(roodId);
//        GameInfo gameInfo = gameService.getGame(roodId);
//        List<Player> players = gameInfo.getPlayers();
//        for (Player player : players) {
//            if (player.getNickname().equals(userNickname)) {
//                SseEmitter emitter = playersInTheRoom.get(userNickname);
//                try {
//                    emitter
//                        .send(SseEmitter.event()
//                            .name("role")
//                            .data(gameInfo));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }

       // String roodId = "testroom";
       // String userNickname = "11";

        DistributeRolesDto d = new DistributeRolesDto();
        d.setNickname("userNickname "+userNickname);
        d.setRole(Role.DEVELOPER);
        Object data = d;

        Map<String, SseEmitter> playersInTheRoom = emittersByRoomId.get(roodId);
        SseEmitter emitter = playersInTheRoom.get(userNickname);
            try {
                emitter
                    .send(SseEmitter.event()
                        .name("role")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    //모든 클라이언트에게 보내는 메서드
    public void roomInfo(String roodId) {
//        GameInfo gameInfo = gameService.getGame(roodId);

        GameInfo info = new GameInfo();
        info.setTime(1);
        info.setLivePlayerNumber(2);
        Object data = info;

        Map<String, SseEmitter> playersInTheRoom = emittersByRoomId.get(roodId);

        for (String key : playersInTheRoom.keySet()) {
            SseEmitter emitter = playersInTheRoom.get(key);
            try {
                emitter
                    .send(SseEmitter.event()
                        .name("roomInfo")
                        .data(data));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//        emittersByRoomId.forEach(emitter -> {
//            try {
//                emitter
//                    .send(SseEmitter.event()
//                    .name("role")
//                    .data(data));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

//    /**
//     * 콜백은 SseEmitter를 관리하는 다른 스레드에서 실행
//     * 따라서 thread-safe한 자료구조인 CopyOnWriteArrayList를 사용
//     * ConcurrnetModificationException 방지를 위함
//     * */
//    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
//
//
//    /**
//     * kill 이벤트 발생
//     * 특정 플레이어가 죽음
//     * */
//    public void kill() {
//        String deadPlayerNickname = "죽은 플레이어의 닉네임";
//        emitters.forEach(emitter -> {
//            try {
//                emitter.send(SseEmitter.event()
//                    .name("kill")
//                    .data(deadPlayerNickname));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    /**
//     * reveal 이벤트 발생
//     * 특정 플레이어의 직업을 밝힘
//     * */
//    public void reveal() {
//        String playerNickname = "특정 플레이어의 닉네임";
//        Role role = Role.DOCTOR; // 의사였다고 가정
//        emitters.forEach(emitter -> {
//            try {
//                emitter.send(SseEmitter.event()
//                    .name("reveal")
//                    .data(playerNickname + role));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }




}