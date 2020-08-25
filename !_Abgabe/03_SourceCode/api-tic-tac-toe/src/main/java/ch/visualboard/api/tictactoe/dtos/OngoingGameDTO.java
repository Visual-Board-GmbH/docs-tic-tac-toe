package ch.visualboard.api.tictactoe.dtos;

import ch.visualboard.api.tictactoe.entities.State;
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
public class OngoingGameDTO
{
    private Integer gameId;
    private String name;
    private State state;
    private String lastModified;
    private List<Integer> matrixIds;
    private GameDataDTO gameData;
    private List<PlayerDataDTO> playerData;
    private Integer statusCode;
    private int requestId;
    private boolean isServerResponse;
}
