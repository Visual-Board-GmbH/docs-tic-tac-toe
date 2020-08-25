package ch.visualboard.api.tictactoe.dtos;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameHistoryDTO
{
    private int id;
    private String name;
    private Date datePlayed;
    private GameDataDTO gameData;
    private List<PlayerDataDTO> playerData;
}
