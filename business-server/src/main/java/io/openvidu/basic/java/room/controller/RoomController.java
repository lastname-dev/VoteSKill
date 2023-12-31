package io.openvidu.basic.java.room.controller;

import io.openvidu.basic.java.jwt.JwtService;
import io.openvidu.basic.java.room.dto.RoomEnterDto;
import io.openvidu.basic.java.room.dto.RoomEnterResponseDto;
import io.openvidu.basic.java.room.domain.Room;
import io.openvidu.basic.java.room.service.RoomService;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.openvidu.java.client.Connection;
import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import io.openvidu.java.client.SessionProperties;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

	@Value("${OPENVIDU_URL}")
	private String OPENVIDU_URL;

	@Value("${OPENVIDU_SECRET}")
	private String OPENVIDU_SECRET;


	private final RoomService roomService;
	private final JwtService jwtService;


	/**
	 * @param params The Session properties
	 * @return The Session ID
	 */
	@PostMapping("/api/sessions")
	public ResponseEntity<Room> makeRoom(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request)
			throws OpenViduJavaClientException, OpenViduHttpException {
		log.info("makeroom : {}", params);
		String nickName = jwtService.getNickName(request);
		params.put("nickname",nickName);
		SessionProperties properties = SessionProperties.fromJson(params).build();
		Session session = roomService.createSession(properties);
		return new ResponseEntity<>(roomService.createRoom(params), HttpStatus.OK);
	}

	@GetMapping("/api/sessions")
	public ResponseEntity<List<Room>> getRooms(){

		List<Room> rooms = roomService.getRooms();
		return new ResponseEntity<>(rooms,HttpStatus.OK);
	}
	@DeleteMapping("/api/sessions/{roomName}")
	public ResponseEntity exitRooms(@PathVariable String roomName,HttpServletRequest request){
		String nickName = jwtService.getNickName(request);
		roomService.exitRoom(nickName,roomName);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/api/sessions/{sessionId}/connections")
	public ResponseEntity<RoomEnterResponseDto> enterRoom(@PathVariable("sessionId") String sessionId,
														  @RequestBody(required = false) Map<String, Object> params,HttpServletRequest request)
			throws Exception {
		String nickName = jwtService.getNickName(request);
		Session session = roomService.getActiveSession(sessionId);
		if (session == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
		Connection connection = session.createConnection(properties);

		RoomEnterDto roomEnterDto = new RoomEnterDto(sessionId, (String) params.get("password"),nickName);

		RoomEnterResponseDto roomEnterResponseDto = new RoomEnterResponseDto(connection.getToken(),roomService.joinRoom(roomEnterDto));
		return new ResponseEntity<>(roomEnterResponseDto, HttpStatus.OK);
	}



}
