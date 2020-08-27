package ch.visualboard.api.tictactoe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameWinner
{
    HOST,
    GUEST,
    NONE
}
