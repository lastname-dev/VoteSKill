package com.voteskill.gameserver.dto;

import com.voteskill.gameserver.domain.Player;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class GameStartDto {
    private List<String> players; //게임에 참여한 플레이어들의 정보 nickname
}
