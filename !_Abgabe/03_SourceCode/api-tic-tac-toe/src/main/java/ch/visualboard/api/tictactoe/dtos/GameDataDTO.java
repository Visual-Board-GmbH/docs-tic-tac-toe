package ch.visualboard.api.tictactoe.dtos;

import ch.visualboard.api.tictactoe.entities.GameWinner;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDataDTO
{
    private int host;
    private int guest;
    private List<MoveDTO> moves = new ArrayList<>();
    private GameWinner winner;
}
