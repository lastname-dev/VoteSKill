package com.voteskill.gameserver.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voteskill.gameserver.game.dto.DistributeRolesDto;
import com.voteskill.gameserver.game.dto.GameInfoResponseDto;
import com.voteskill.gameserver.game.dto.VoteResultResponseDto;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final SseRepository sseRepository;
    private final ObjectMapper objectMapper; //JSON 형태의 문자열로 변환하여 데이터 전송하기 위함

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     * @param userNickname - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    public SseEmitter createEmitter(String roomId, String userNickname) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT );
        sseRepository.save(roomId, userNickname, emitter);


        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> sseRepository.delete(roomId, userNickname));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> sseRepository.delete(roomId, userNickname));

        return emitter;
    }

    /**
     * 특정 방에 있는 모든 클라이언트들에게 GameInfo 정보를 보내기
     * @param roomId 방 ID
     *      * @param gameInfoResponseDto - 보낼 GameInfo 정보
     *      */
    public void notifyAllInRoom(String roomId, GameInfoResponseDto gameInfoResponseDto) {
        String jsonGameInfo = null;
        try {
            jsonGameInfo = objectMapper.writeValueAsString(gameInfoResponseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // JSON 변환 실패 처리
        }

        if (jsonGameInfo != null) {
            Map<String, SseEmitter> emittersByUser = sseRepository.getAllEmittersByRoom(roomId);
            for (SseEmitter emitter : emittersByUser.values()) {
                sendToEmitter(emitter, jsonGameInfo);
            }
        }
    }

    /**
     * 같은 방에 있는 모든 클라이언트들에게 투표 결과를 보내기
     * @param roomId - 방 ID
     * @param responseDto - 보낼 투표 결과 리스트
     */
    public void sendVoteResultToAllInRoom(String roomId, VoteResultResponseDto responseDto) {

        String jsonData = null;
        try {
            jsonData = objectMapper.writeValueAsString(responseDto);
        } catch (JsonProcessingException e) {
            // JSON 변환 실패 처리
            e.printStackTrace();
        }

        if (jsonData != null) {
            Map<String, SseEmitter> emittersByUser = sseRepository.getAllEmittersByRoom(roomId);
            for (SseEmitter emitter : emittersByUser.values()) {
                sendToEmitter(emitter, jsonData);
            }
        }
    }


//    /**
//     * 특정 방에 있는 한 클라이언트에게 특정한 정보를 보내기
//     * @param roomId - 방 ID
//     * @param userNickname - 유저 이름
//     * @param data - 보낼 데이터
//     */
//    public void notifyClient(String roomId, String userNickname, String data) {
//        SseEmitter emitter = sseRepository.getByRoomAndUser(roomId, userNickname);
//        if (emitter != null) {
//            sendToEmitter(emitter, data);
//        }
//    }

    /**
     * 특정 사용자에게 부여된 역할 정보 보내기 - DistributeRolesDto 사용
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @param distributeRolesDto - 보낼 DistributeRolesDto 정보
     */
    public void sendRolesInfoToUser(String roomId, String userNickname, DistributeRolesDto distributeRolesDto) {
        String jsonRolesInfo = null;
        try {
            jsonRolesInfo = objectMapper.writeValueAsString(distributeRolesDto);
        } catch (JsonProcessingException e) {
            // JSON 변환 실패 처리
            e.printStackTrace();
        }
        if (jsonRolesInfo != null) {
            SseEmitter emitter = sseRepository.getByRoomAndUser(roomId, userNickname);
            if (emitter != null) {
                sendToEmitter(emitter, jsonRolesInfo);
            }
        }
    }

    private void sendToEmitter(SseEmitter emitter, String data) {
        try {
            emitter.send(SseEmitter.event().name("sse").data(data), MediaType.TEXT_PLAIN); // UTF-8 인코딩을 위해 MediaType.TEXT_PLAIN 사용
        } catch (IOException exception) {
            emitter.completeWithError(exception);
        }
    }

    /**
     * 특정 방에 들어와 있는 모든 유저에 대한 SSE Emitter들을 가져오기
     * @param roomId - 방 ID
     * @return 특정 방에 들어와 있는 모든 유저에 대한 SSE Emitter들의 맵
     */
    public Map<String, SseEmitter> getAllEmittersByRoom(String roomId) {
        return sseRepository.getAllEmittersByRoom(roomId);
    }

    /**
     * 특정 방에 특정 유저에 대한 SSE Emitter를 가져오기
     * @param roomId - 방 ID
     * @param userNickname - 유저 이름
     * @return 특정 방에 특정 유저에 대한 SSE Emitter
     */
    public SseEmitter getEmitterByRoomAndUser(String roomId, String userNickname) {
        return sseRepository.getByRoomAndUser(roomId, userNickname);
    }

}
