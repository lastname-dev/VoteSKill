package com.voteskill.gameserver.game.dto;

import com.voteskill.gameserver.game.domain.Vote;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 투표결과 전달용 DTO
 * */
@Getter
@Setter
@AllArgsConstructor
public class VoteResultResponseDto {
    private String roomId;
    private List<Vote> voteResult;

}
