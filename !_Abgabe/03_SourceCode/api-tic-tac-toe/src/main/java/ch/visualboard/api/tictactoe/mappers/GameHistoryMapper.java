package ch.visualboard.api.tictactoe.mappers;

import ch.visualboard.api.tictactoe.dao.GameHistoryDAO;
import ch.visualboard.api.tictactoe.dtos.GameDataDTO;
import ch.visualboard.api.tictactoe.dtos.GameHistoryDTO;
import ch.visualboard.api.tictactoe.dtos.PlayerDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("8abc27c2-6357-4be7-a7d0-db5e62482891")
public class GameHistoryMapper
{
    private static final Logger logger = LoggerFactory.getLogger(GameHistoryMapper.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public GameHistoryMapper(final ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public GameHistoryDTO mapGameHistoryDAOToGameHistoryDTO(GameHistoryDAO gameHistoryDAO)
    {
        try {
            return GameHistoryDTO.builder()
                .id(gameHistoryDAO.getGameId())
                .name(gameHistoryDAO.getGameName())
                .datePlayed(gameHistoryDAO.getDatePlayed())
                .gameData(objectMapper.readValue(gameHistoryDAO.getGameData(), GameDataDTO.class))
                .playerData(objectMapper.readValue(gameHistoryDAO.getPlayerData(), new TypeReference<List<PlayerDataDTO>>()
                {
                }))
                .build();
        }
        catch (JsonProcessingException e) {
            logger.warn("something went wrong", e);
        }
        return null;
    }
}
