package ch.visualboard.api.tictactoe.dtos;

import ch.visualboard.api.tictactoe.entities.GamePlayer;
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
public class PlayerDataDTO
{
    private GamePlayer player;
    private String displayName;
}
