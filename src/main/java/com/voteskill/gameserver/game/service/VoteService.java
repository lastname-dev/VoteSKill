package com.voteskill.gameserver.game.service;

import com.voteskill.gameserver.game.domain.Vote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class VoteService {


    private Map<String, Vote> voteResults = new HashMap<>(); //플레이어 별 투표수를 담고 있는 변수

    // TODO : 레디스 접근해서 확인하기 -> 로직 변경 예정
    private Set<String> validRoomIds = new HashSet<>(); // roomId
    private Set<String> validPlayers = new HashSet<>(); // players

    /**
     * 투표결과 추합하는 메서드
     * */
    public void recordVote(String roomId, String playerNickname) {
        voteResults.putIfAbsent(playerNickname, new Vote(playerNickname, 1)); //닉네임 언급됐다면 1표
        voteResults.get(playerNickname).increaseVoteCount();
    }

    public List<Vote> getVoteSummary() {
        return new ArrayList<>(voteResults.values());
    }

    public void clearVotes() {
        voteResults.clear();
    }


    //TODO : redis 에 접근하는 방식으로 변경 예정
    public boolean isValidVote(String roomId, String voterNickname, String voteeNickname) {
        // 방 이름이 유효한지 확인
        if (!validRoomIds.contains(roomId)) {
            return false;
        }
        // 해당 방에 투표한 사람이 있는지 확인
        if (!validPlayers.contains(voterNickname) || !validPlayers.contains(voteeNickname)) {
            return false;
        }
        return true;
    }


}
