//package io.openvidu.basic.java.game.service;
//
//import io.openvidu.basic.java.game.domain.GameInfo;
//import io.openvidu.basic.java.game.domain.Player;
//import io.openvidu.basic.java.game.dto.SkillDto;
//import io.openvidu.basic.java.game.dto.SkillResultDto;
//import io.openvidu.basic.java.game.dto.VoteDto;
//import io.openvidu.basic.java.game.service.GameService;
//import io.openvidu.basic.java.room.domain.Room;
//import io.openvidu.basic.java.room.service.RoomService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//public class GameServiceTest {
//
//    @Mock
//    private RoomService roomService;
//
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @InjectMocks
//    private GameService gameService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testSetting() {
//        // Given
//        String roomName = "testRoom";
//        List<String> people = new ArrayList<>();
//        people.add("player1");
//        people.add("player2");
//
//        when(roomService.getRoom(roomName)).thenReturn(new Room(roomName, people));
//
//        // When
//        gameService.setting(roomName);
//
//        // Then
//        verify(redisTemplate, times(1)).opsForHash().put(eq("game:"), eq(roomName), any(GameInfo.class));
//    }
//
//    @Test
//    public void testVote() {
//        // Given
//        String roomName = "testRoom";
//        String caster = "player1";
//        String target = "player2";
//
//        GameInfo gameInfo = new GameInfo();
//        List<Player> players = new ArrayList<>();
//        Player player1 = new Player("player1", "ROLE_CITIZEN", "", 0, true, false);
//        Player player2 = new Player("player2", "ROLE_MAFIA", "", 0, true, false);
//        players.add(player1);
//        players.add(player2);
//        gameInfo.setPlayers(players);
//
//        when(redisTemplate.opsForHash().get(eq("game:"), eq(roomName))).thenReturn(gameInfo);
//
//        // When
//        VoteDto voteDto = new VoteDto(roomName, caster, target);
//        gameService.vote(voteDto);
//
//        // Then
//        verify(redisTemplate, times(1)).opsForHash().put(eq("game:"), eq(roomName), any(GameInfo.class));
//        assertEquals(1, player2.getVoteCount());
//    }
//
//    @Test
//    public void testSkill() throws Exception {
//        // Given
//        String roomName = "testRoom";
//        String caster = "player1";
//        String target = "player2";
//
//        GameInfo gameInfo = new GameInfo();
//        List<Player> players = new ArrayList<>();
//        Player player1 = new Player("player1", "ROLE_POLICE", "", 0, true, false);
//        Player player2 = new Player("player2", "ROLE_MAFIA", "", 0, true, false);
//        players.add(player1);
//        players.add(player2);
//        gameInfo.setPlayers(players);
//
//        when(redisTemplate.opsForHash().get(eq("game:"), eq(roomName))).thenReturn(gameInfo);
//
//        // When
//        ResponseEntity<SkillResultDto> result = gameService.skill(new SkillDto(roomName, caster, target));
//
//        // Then
//        verify(redisTemplate, times(1)).opsForHash().put(eq("game:"), eq(roomName), any(GameInfo.class));
//        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
//    }
//
//    @Test
//    public void testCheckRole() {
//        // Given
//        String roomName = "testRoom";
//        String nickname = "player1";
//        GameInfo gameInfo = new GameInfo();
//        List<Player> players = new ArrayList<>();
//        Player player1 = new Player("player1", "ROLE_POLICE", "", 0, true, false);
//        players.add(player1);
//        gameInfo.setPlayers(players);
//
//        when(redisTemplate.opsForHash().get(eq("game:"), eq(roomName))).thenReturn(gameInfo);
//
//        // When
//        String role = gameService.checkRole(gameInfo, nickname);
//
//        // Then
//        assertEquals("ROLE_POLICE", role);
//    }
//
//    @Test
//    public void testSetRole() {
//        // Given
//        List<String> people = new ArrayList<>();
//        people.add("player1");
//        people.add("player2");
//
//        // When
//        List<Player> players = gameService.setRole(people);
//
//        // Then
//        assertNotNull(players);
//        assertEquals(2, players.size());
//    }
//
//
//}
