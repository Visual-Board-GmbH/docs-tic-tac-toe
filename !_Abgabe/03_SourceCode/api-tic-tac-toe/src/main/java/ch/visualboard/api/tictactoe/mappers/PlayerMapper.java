package ch.visualboard.api.tictactoe.mappers;

import ch.visualboard.api.tictactoe.dao.PlayerDAO;
import ch.visualboard.api.tictactoe.dtos.CreateOrUpdatePlayerDTO;
import ch.visualboard.api.tictactoe.dtos.LoggedInPlayerDTO;
import ch.visualboard.api.tictactoe.dtos.OtherPlayerDTO;
import org.springframework.stereotype.Component;

@Component("739b4f7f-2d11-477e-817d-1efd93437410")
public class PlayerMapper
{
    public PlayerDAO mapCreateNewPlayerDTOToPlayerDAO(CreateOrUpdatePlayerDTO createOrUpdatePlayerDTO, String password, String salt)
    {
        return PlayerDAO.builder()
            .name(createOrUpdatePlayerDTO.getName())
            .username(createOrUpdatePlayerDTO.getUsername())
            .nickname(createOrUpdatePlayerDTO.getNickname())
            .password(password)
            .salt(salt)
            .build();
    }

    public LoggedInPlayerDTO mapPlayerDAOToLoggedInPlayerDTO(PlayerDAO playerDAO)
    {
        return LoggedInPlayerDTO.builder()
            .id(playerDAO.getId())
            .name(playerDAO.getName())
            .username(playerDAO.getUsername())
            .nickname(playerDAO.getNickname())
            .build();
    }

    public OtherPlayerDTO mapPlayerDAOToOtherPlayerDTO(PlayerDAO playerDAO)
    {
        return OtherPlayerDTO.builder()
            .id(playerDAO.getId())
            .nickname(playerDAO.getNickname())
            .build();
    }

    public PlayerDAO mapUpdatedPlayerDTOToPlayerDAO(
        final CreateOrUpdatePlayerDTO createOrUpdatePlayerDTO,
        final Integer playerId,
        final String password,
        final String salt
    )
    {
        return PlayerDAO.builder()
            .id(playerId)
            .name(createOrUpdatePlayerDTO.getName())
            .username(createOrUpdatePlayerDTO.getUsername())
            .nickname(createOrUpdatePlayerDTO.getNickname())
            .password(password)
            .salt(salt)
            .build();
    }
}
