package io.openvidu.basic.java.room.controller;

import io.openvidu.basic.java.jwt.JwtService;
import io.openvidu.basic.java.room.dto.RoomEnterDto;
import io.openvidu.basic.java.room.dto.RoomEnterResponseDto;
import io.openvidu.basic.java.Room;
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

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

	@Value("${OPENVIDU_URL}")
	private String OPENVIDU_URL;

	@Value("${OPENVIDU_SECRET}")
	private String OPENVIDU_SECRET;

//	private OpenVidu openvidu;

	private final RoomService roomService;
	private final JwtService jwtService;
//	@PostConstruct
//	public void init() {
////		this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
//		this.roomService = new RoomService(OPENVIDU_URL,OPENVIDU_SECRET);
//	}

	/**
	 * @param params The Session properties
	 * @return The Session ID
	 */
	@PostMapping("/api/sessions")
	public ResponseEntity<Room> makeRoom(@RequestBody(required = false) Map<String, Object> params, HttpServletRequest request)
			throws OpenViduJavaClientException, OpenViduHttpException {
		//todo : 현재는 방 이름이 영어 밖에 안됨.
		log.info("makeroom : {}", params);
		String nickName = jwtService.getNickName(request);
		params.put("nickname",nickName);
		SessionProperties properties = SessionProperties.fromJson(params).build();
		Session session = roomService.createSession(properties);
		return new ResponseEntity<>(roomService.createRoom(params), HttpStatus.OK);
	}
//	@PostMapping("/api/sessions")
//	public ResponseEntity<String> makeRoom(@RequestBody(required = false) Map<String, Object> params)
//			throws OpenViduJavaClientException, OpenViduHttpException {
//		log.info("makeroom : {}", params);
//		SessionProperties properties = SessionProperties.fromJson(params).build();
////		Session session = openvidu.createSession(properties);
//		Session session = roomService.createSession(properties);
//		roomService.createRoom(params);
//		return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
//	}
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
//	/**
//	 * @param sessionId The Session in which to create the Connection
//	 * @param params    The Connection properties
//	 * @return The Token associated to the Connection
//	 */
//	@PostMapping("/api/sessions/{sessionId}/connections")
//	public ResponseEntity<String> enterRoom(@PathVariable("sessionId") String sessionId,
//			@RequestBody(required = false) Map<String, Object> params)
//			throws Exception {
//		log.info("enterRoom : {}",params);
//		Session session = roomService.getActiveSession(sessionId);
//		if (session == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//		ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
//		Connection connection = session.createConnection(properties);
//
//		RoomEnterDto roomEnterDto = new RoomEnterDto(sessionId, (String) params.get("password"));
//		roomService.joinRoom(roomEnterDto);
//
//		return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
//	}
	@GetMapping("/zz/{roomName}")
	public ResponseEntity<Room> room(@PathVariable String roomName){
		return new ResponseEntity<>(roomService.getRoom(roomName),HttpStatus.OK);
	}
	@PostMapping("/api/sessions/{sessionId}/connections")
	public ResponseEntity<RoomEnterResponseDto> enterRoom(@PathVariable("sessionId") String sessionId,
														  @RequestBody(required = false) Map<String, Object> params,HttpServletRequest request)
			throws Exception {
		log.info("enterRoom : {}",params);
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
