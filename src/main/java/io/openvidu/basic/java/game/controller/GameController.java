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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final JwtService jwtService;
    private final GameService gameService;
    @PostMapping("/rooms/{roomId}/start")
    public void start(@PathVariable String roomId){
        gameService.setting(roomId);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "http://13.125.113.149:8080/rooms/"+roomId+"/start", //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class
        );
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
}
