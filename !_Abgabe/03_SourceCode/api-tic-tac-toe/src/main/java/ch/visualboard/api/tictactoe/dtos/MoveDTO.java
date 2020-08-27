package ch.visualboard.api.tictactoe.dtos;

import ch.visualboard.api.tictactoe.entities.GamePlayer;
import ch.visualboard.api.tictactoe.entities.TicTacToeGridPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveDTO
{
    private int count;
    private GamePlayer player;
    private TicTacToeGridPosition gridPosition;
}
