package io.openvidu.basic.java.game.controller;

import io.openvidu.basic.java.Room;
import io.openvidu.basic.java.game.domain.GameInfo;
import io.openvidu.basic.java.game.domain.Player;
import io.openvidu.basic.java.game.dto.SkillDto;
import io.openvidu.basic.java.game.dto.SkillResultDto;
import io.openvidu.basic.java.game.dto.VoteDto;
import io.openvidu.basic.java.game.service.GameService;
import io.openvidu.basic.java.jwt.JwtService;
import io.openvidu.basic.java.room.service.RoomService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final JwtService jwtService;
    private final GameService gameService;
    @PostMapping("/rooms/{roodId}/start")
    public void start(@PathVariable String roomId){
        gameService.setting(roomId);
    }
    @PostMapping("/rooms/{roomId}/skill/{target}")
    public ResponseEntity<SkillResultDto> skill(@PathVariable String roomId,@PathVariable String target, HttpServletRequest request)
        throws Exception {
        String nickName = jwtService.getNickName(request);
        SkillDto skillDto = new SkillDto(roomId,nickName,target);
        return gameService.skill(skillDto);
    }
    @PostMapping("/rooms/{roomId}/vote/{target}")
    public void vote(@PathVariable String roomId,@PathVariable String target,HttpServletRequest request){
        String nickName = jwtService.getNickName(request);
        VoteDto voteDto = new VoteDto(roomId,nickName,target);
        gameService.vote(voteDto);
    }
    @PostMapping("/dummy")
    public void dummy() {
        List<Player> players = new ArrayList<>();

        players.add(new Player("user1" , "ROLE_MAFIA", "PERSON", 0, true, false));
        players.add(new Player("user2" , "ROLE_DOCTOR", "PERSON", 0, true, false));
        players.add(new Player("user3" , "ROLE_GANGSTER", "PERSON", 0, true, false));
        players.add(new Player("user4" , "ROLE_POLITICIAN", "PERSON", 0, true, false));
        players.add(new Player("user5" , "ROLE_PRIEST", "PERSON", 0, true, false));
        players.add(new Player("user6" , "ROLE_POLICE", "PERSON", 0, true, false));

        GameInfo gameInfo = new GameInfo("testroom2", players, 1, 0, new ArrayList<>(), 6);
        gameService.test(gameInfo);
        log.info("더미 데이터 삽입 완료");
    }
    @GetMapping("/{gameId}")
    public GameInfo test(@PathVariable String gameId){
        log.info("game : {}",gameService.getGame(gameId));
        return gameService.getGame(gameId);
    }
}
