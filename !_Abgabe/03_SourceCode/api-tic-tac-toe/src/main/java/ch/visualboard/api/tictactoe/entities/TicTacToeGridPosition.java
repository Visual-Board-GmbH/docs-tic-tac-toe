package ch.visualboard.api.tictactoe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicTacToeGridPosition
{
    TOP_LEFT(0, 0),
    TOP_MID(6, 0),
    TOP_RIGHT(12, 0),
    MID_LEFT(0, 6),
    MID_MID(6, 6),
    MID_RIGHT(12, 6),
    BOTTOM_LEFT(0, 12),
    BOTTOM_MID(6, 12),
    BOTTOM_RIGHT(12, 12);

    private final int xPositionStart;
    private final int yPositionStart;
}
