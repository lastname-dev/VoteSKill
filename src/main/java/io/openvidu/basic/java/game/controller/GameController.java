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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
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

    @PostMapping("/test")
    public void test(){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            players.add(new Player("user"+i,"ROLE","PERSON",0,true,false));
        }
        GameInfo gameInfo = new GameInfo("testroom",players,1,new List[9],6);
        gameService.test(gameInfo);
    }

}
