package ch.visualboard.api.tictactoe.services;

import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;

public abstract class AbstractGameService
{
    public boolean isValidGameRequest(OngoingGameDTO ongoingGameDTO)
    {
        return ongoingGameDTO == null || ongoingGameDTO.isServerResponse();
    }
}
