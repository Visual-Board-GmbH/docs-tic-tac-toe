package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;

public abstract class GameService
{
    public boolean isValidGameRequest(OngoingGameDTO ongoingGameDTO)
    {
        return ongoingGameDTO == null || ongoingGameDTO.isServerResponse();
    }
}
