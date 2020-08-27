package ch.visualboard.api.tictactoe.mappers;

import ch.visualboard.api.tictactoe.dao.GameDAO;
import ch.visualboard.api.tictactoe.dao.LEDRGBMatrixDAO;
import ch.visualboard.api.tictactoe.dtos.GameDataDTO;
import ch.visualboard.api.tictactoe.dtos.OngoingGameDTO;
import ch.visualboard.api.tictactoe.services.PlayerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("678a7e84-2e15-4564-9e94-9932c5d9ddde")
public class OngoingGameMapper
{
    private static final Logger logger = LoggerFactory.getLogger(OngoingGameMapper.class);

    private final ObjectMapper objectMapper;
    private final PlayerService playerService;

    public OngoingGameMapper(
        final ObjectMapper objectMapper,
        final PlayerService playerService
    )
    {

        this.objectMapper = objectMapper;
        this.playerService = playerService;
    }

    public OngoingGameDTO mapGameDTOToOngoingGameDAO(GameDAO gameDAO)
    {
        try {
            return OngoingGameDTO.builder()
                .gameId(gameDAO.getId())
                .name(gameDAO.getName())
                .state(gameDAO.getState())
                .lastModified(objectMapper.writeValueAsString(gameDAO.getLastModified()))
                .matrixIds(gameDAO.getLedrgbMatrixDAOS().stream().map(LEDRGBMatrixDAO::getId).collect(Collectors.toList()))
                .gameData(objectMapper.readValue(gameDAO.getGameData(), GameDataDTO.class))
                .playerData(playerService.getHostGuestPlayerData(gameDAO.getPlayerDAOS()))
                .isServerResponse(true)
                .build();
        }
        catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return new OngoingGameDTO();
        }
    }
}
